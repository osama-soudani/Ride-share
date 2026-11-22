package com.rideshare.rating.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/actuator/health").permitAll()

                // ----- Évaluations -----
                .requestMatchers(HttpMethod.POST, "/api/evaluations").hasAnyRole("PASSAGER", "CHAUFFEUR")
                .requestMatchers(HttpMethod.PUT, "/api/evaluations/*").hasAnyRole("PASSAGER", "CHAUFFEUR")
                // DELETE : auteur ou ADMIN (contrôlé dans le service)
                .requestMatchers("/api/evaluations", "/api/evaluations/**").authenticated()

                // ----- Signalements -----
                .requestMatchers(HttpMethod.POST, "/api/signalements").hasAnyRole("PASSAGER", "CHAUFFEUR")
                .requestMatchers(HttpMethod.GET, "/api/signalements/mes-signalements").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/signalements/*").hasAnyRole("PASSAGER", "CHAUFFEUR")
                // GET liste/détail, PATCH traiter, DELETE : ADMIN
                .requestMatchers("/api/signalements", "/api/signalements/**").hasRole("ADMIN")

                .anyRequest().denyAll())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
