package com.hub.course_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                // Provides metadata about the API.
                // (metadata - descriptive data about data of API)
                title = "Course Service API",
                description = "Course API documentation",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "oauth2_bearer"),
        servers = {
                // An array of Server Objects,
                // which provide connectivity information to a target server.
                @Server(
                        url = "${server.servlet.context-path}",
                        description = "Default Server URL"
                )
        }
)
@SecurityScheme(
        name = "oauth2_bearer", type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${springdoc.oauthflow.authorization-url}",
                        tokenUrl = "${springdoc.oauthflow.token-url}",
                        scopes = {@OAuthScope(name = "openid", description = "openid")})
        )
)
public class SwaggerConfig {
}
