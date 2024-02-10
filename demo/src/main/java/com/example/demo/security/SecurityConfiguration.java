package com.example.demo.security;

import com.example.demo.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String SPECTATOR = Role.SPECTATOR.name();
    private static final String COACH = Role.COACH.name();
    private static final String ADMIN = Role.ADMIN.name();

    private static final String[] WHITE_LIST = {
            "/api/v1/players",
            "/api/v1/players/{playerId}",
            "/api/v1/teams",
            "/api/v1/teams/{teamId}",
            "/api/v1/events",
            "/api/v1/events/{eventId}",
            "/api/v1/events/{eventId}/score",
            "/api/v1/users/auth/**",
    };

    private final AuthenticationProvider authProvider;

    @Autowired
    public SecurityConfiguration(AuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(GET, "/api/v1/appusers",
                                "/api/v1/appusers/{userId}").hasAuthority(ADMIN)
                        .requestMatchers(POST, "/api/v1/players/create",
                                "/api/v1/teams/create",
                                "/api/v1/events/create",
                                "/api/v1/appusers/create",
                                "/api/v1/events/{eventId}/start",
                                "/api/v1/events/{eventId}/stop",
                                "/api/v1/events/{eventId}/resume",
                                "/api/v1/events/{eventId}/finish",
                                "/api/v1/events/{eventId}/cancel").hasAuthority(ADMIN)
                        .requestMatchers(DELETE, "/api/v1/players/{playerId}/delete",
                                "/api/v1/teams/{teamId}/delete",
                                "/api/v1/events/{eventId}/delete",
                                "/api/v1/appusers/{userId}/delete").hasAuthority(ADMIN)
                        .requestMatchers(PUT, "/api/v1/players/{playerId}/update",
                                "/api/v1/teams/{teamId}/update",
                                "/api/v1/events/{eventId}/update",
                                "/api/v1/appusers/{userId}/update",
                                "/api/v1/events/{eventId}/score/update").hasAuthority(ADMIN)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authProvider)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
