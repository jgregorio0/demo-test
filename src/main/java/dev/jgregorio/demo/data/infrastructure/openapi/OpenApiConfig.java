package dev.jgregorio.demo.data.infrastructure.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Authentication Bearer JWT token";

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.description}")
    private String appDescription;

    @Value("${spring.application.version}")
    private String appVersion;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement())
                .components(apiComponents())
                .servers(List.of(localServer()));
    }

    private Server localServer() {
        return new Server().url(contextPath);
    }

    private Info apiInfo() {
        return new Info().title(appName).version(appVersion).description(appDescription);
    }

    private Components apiComponents() {
        return new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
    }
}
