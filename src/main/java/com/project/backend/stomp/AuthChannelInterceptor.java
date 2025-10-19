package com.project.backend.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.project.backend.services.JwtService;
import com.project.backend.data.UserRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("here and ye");
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor != null)
            System.out.println(message.toString());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = null;
            
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String authHeader = authorizationHeaders.get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7); 
                }
            }
            if (token == null) {
                List<String> loginHeaders = accessor.getNativeHeader("login");
                if (loginHeaders != null && !loginHeaders.isEmpty()) {
                    token = loginHeaders.get(0); // Directly use the token from 'login' header
                }
            }

            if (token != null) {
                System.out.println("Invalid JWT token for user: {}");
                try {
                    String userEmail = jwtService.extractUsername(token);
                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                        if (jwtService.isTokenValid(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            accessor.setUser(authentication);
                            String dest = message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER, String.class);
                            System.out.println("WebSocket STOMP CONNECT authenticated for user: {}" + dest + "destination");
                        } else {
                            System.out.println("Invalid JWT token for user: {}");
                            
                        }
                    }
                } catch (Exception e) {
                    // Catch any exceptions during token parsing or user loading
                    System.out.println("Error authenticating WebSocket STOMP CONNECT: {}");
                    // Throwing an exception here will prevent the STOMP connection from completing.
                    // throw new MessageDeliveryException("Authentication failed: " + e.getMessage());
                }
            } else {
                System.out.println("No JWT token found in STOMP CONNECT headers.");
                // If authentication is mandatory for all STOMP connections,
                // you would throw an exception here to reject unauthenticated connections.
                // throw new MessageDeliveryException("Authentication required for WebSocket STOMP CONNECT.");
            }
        }
        else if (StompCommand.MESSAGE.equals(accessor.getCommand())) {
            // This captures messages being sent from the server to the client
            String destination = accessor.getDestination();
            if (destination != null) {
                System.out.println("!!! ChannelInterceptor: Outbound MESSAGE to destination: {} !!!" + destination);
            }
        }
        return message; // Always return the message, even if not authenticated or an error occurred
    }

}
