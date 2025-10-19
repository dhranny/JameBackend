package com.project.backend.services;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable method-level security (e.g., @PreAuthorize)
public class SecurityConfiguration {

        @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs (JWT)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/auth/**", // Public endpoints for authentication
                                "/h2-console/**",  // Allow access to H2 console
                                "/v2/api-docs",
                                "/app/**",
                                "/error",
                                "/gs-guide-websocket/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "ws/**" //TODO: remove when you want to regain security
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // For H2 console frame
        return http.build();
    }
}
