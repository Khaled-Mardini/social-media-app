package com.beno.social_media_app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.authentication.logout.LogoutHandler;
// import static org.springframework.http.HttpMethod.*;
// import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    // final JwtAuthenticationFilter jwtAuthFilter;
    // final AuthenticationProvider authenticationProvider;
    // final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {
            "**"
            // "/api/auth/**",
            // "/api-docs/**",
            // "/swagger-resources",
            // "/swagger-resources/**",
            // "/swagger-ui/**",
            // "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL).permitAll());
        return http.build();
    }
}
