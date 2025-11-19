package com.hub.course_service.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    // localhost:9002/course-service/swagger-ui/index.html
    private static final String ACCESS_REALM_CLAIM = "realm_access";
    private static final String ROLE_CLAIM = "roles";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authorizeHttpRequests(
                author ->
                    author
                        .requestMatchers("/swagger-ui", "/swagger-ui/**","/v3/api-docs/**").permitAll()
                        .requestMatchers("/storefront/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/backoffice/courses").hasRole("lecturer")
                        .requestMatchers("/backoffice/courses/pending").hasRole("admin")
                        .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                    oauth2.jwt(Customizer.withDefaults()))
            .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        /*
            - JWT included Header, Payload, Signature
            Claim is a key-value pair in payload
         */

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim(ACCESS_REALM_CLAIM);
            return realmAccess.get(ROLE_CLAIM)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        };

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
