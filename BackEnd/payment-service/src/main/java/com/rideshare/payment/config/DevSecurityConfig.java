package com.rideshare.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * TEST-ONLY security configuration: disables JWT verification so you can
 * test the business logic before Keycloak is set up by the team.
 *
 * Activation: mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
 *
 * NEVER use this profile outside your local machine. Once Keycloak is
 * available, go back to the default profile (SecurityConfig) which
 * actually validates tokens.
 */
@Configuration
@EnableMethodSecurity
@Profile("dev")
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain devFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}