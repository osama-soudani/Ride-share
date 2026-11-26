package com.rideshare.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configure ce microservice comme Resource Server OAuth2 : chaque requête doit
 * porter un JWT valide émis par Keycloak (vérifié via sa clé publique / JWKS,
 * configurée dans application.properties par spring.security.oauth2...issuer-uri).
 *
 * Les rôles Keycloak (realm_access.roles) sont mappés vers des GrantedAuthority
 * préfixées "ROLE_", pour pouvoir écrire @PreAuthorize("hasAuthority('ROLE_ADMIN')").
 */
@Configuration
@EnableMethodSecurity
@Profile("!dev")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);
        return converter;
    }

    /**
     * Keycloak place les rôles dans le claim imbriqué realm_access.roles.
     * On les extrait et on les transforme en ROLE_xxx (en majuscules) pour
     * rester compatible avec les conventions Spring Security.
     */
    @SuppressWarnings("unchecked")
    private Collection<org.springframework.security.core.GrantedAuthority> extractAuthorities(Jwt jwt) {
        JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
        Collection<org.springframework.security.core.GrantedAuthority> defaultAuthorities =
                defaultConverter.convert(jwt);

        var realmAccess = jwt.getClaimAsMap("realm_access");
        List<String> realmRoles = realmAccess != null && realmAccess.get("roles") != null
                ? (List<String>) realmAccess.get("roles")
                : List.of();

        Stream<org.springframework.security.core.GrantedAuthority> realmAuthorities = realmRoles.stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + role.toUpperCase()));

        return Stream.concat(
                defaultAuthorities != null ? defaultAuthorities.stream() : Stream.empty(),
                realmAuthorities
        ).collect(Collectors.toSet());
    }
}
