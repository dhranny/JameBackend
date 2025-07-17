package com.project.backend.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
        // 1. Get STOMP Header Accessor: Provides easy access to STOMP headers.
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // 2. Check if it's a CONNECT command: We only care about authenticating the initial connection.
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = null;

            // 3. Extract JWT from Headers: Clients can send JWT in various ways.
            //    a) Common: "Authorization: Bearer <token>" native header
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String authHeader = authorizationHeaders.get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7); // Extract the token after "Bearer "
                }
            }

            //    b) Alternative (sometimes used by simpler STOMP clients): "login: <token>" native header
            if (token == null) {
                List<String> loginHeaders = accessor.getNativeHeader("login");
                if (loginHeaders != null && !loginHeaders.isEmpty()) {
                    token = loginHeaders.get(0); // Directly use the token from 'login' header
                }
            }

            // 4. Authenticate the Token (if found):
            if (token != null) {
                try {
                    // Extract username (email) from the JWT
                    String userEmail = jwtService.extractUsername(token);

                    // Check if userEmail is valid and if an authentication is not already present
                    // (SecurityContextHolder.getContext().getAuthentication() == null)
                    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // Load UserDetails from your UserDetailsService (which fetches from DB)
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                        // Validate the token against the loaded UserDetails
                        if (jwtService.isTokenValid(token, userDetails)) {
                            // Create an authenticated UsernamePasswordAuthenticationToken
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                            // Set the authenticated user in the WebSocket session's principal.
                            // This is crucial! It makes the user available in @MessageMapping methods
                            // via Principal or @AuthenticationPrincipal.
                            accessor.setUser(authentication);
                            System.out.println("WebSocket STOMP CONNECT authenticated for user: {}");
                        } else {
                            System.out.println("Invalid JWT token for user: {}");
                            // If token is invalid, you could throw an exception to reject the connection
                            // throw new MessageDeliveryException("Invalid JWT token");
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
        return message; // Always return the message, even if not authenticated or an error occurred
    }

}
