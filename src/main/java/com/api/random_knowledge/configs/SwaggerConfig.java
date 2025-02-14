package com.api.random_knowledge.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Random Knowledge API")
                        .version("v1")
                        .description("REST API to provide and manage random knowledge. " +
                                "Allows fetching curiosities and performing CRUD operations on curiosities and categories.")
                )
                .components(new Components()
                        .addSecuritySchemes("RandomKnowledgeSecurityScheme", new SecurityScheme()
                        .name("RandomKnowledgeSecurityScheme")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        )
                );
    }
}
