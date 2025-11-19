package com.hub.course_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Configuration
@EntityScan("com.hub.course_service.model")
@EnableJpaRepositories("com.hub.course_service.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DatabaseAutoConfig {

    private static final String FIRST_NAME = "given_name";
    private static final String LAST_NAME = "family_name";

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null)
                return Optional.of("system");

            /*
                log.info("Auth class: " + auth.getClass());
                Auth class: class org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
                log.info("Principal class: " + auth.getPrincipal().getClass());
                Principal class: class org.springframework.security.oauth2.jwt.Jwt
            */

            if (auth instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                Jwt jwt = (Jwt) auth.getPrincipal();
                String fullName = jwt.getClaimAsString(FIRST_NAME) + " " + jwt.getClaimAsString(LAST_NAME);
                return Optional.of(fullName);
            }

            return Optional.of(auth.getName()); // Returns the id of this principal.

        };
    }

}

/*
        -- USEFUL: This class used for keeping track of who created or changed an entity
        and when the change happened (Auditing) - implementation by AuditorAware<T>



        -- @EnableJpaAuditing: Enable Spring Data JPA Auditing to autopopulate
        @CreatedBy / @LastModifiedBy information based on auditorAware method

        -- @EntityScan: Tell Spring where entities are located,
        so that JPA can map the corresponding tables.

        -- @EnableJpaRepositories: Tell Spring where repository interface are located
        to create proxy JPA

        -- The principal is the currently logged-in user
        Principal.getName()      Returns the name of this principal (username).
*/