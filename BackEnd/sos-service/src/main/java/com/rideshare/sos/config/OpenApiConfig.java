package com.rideshare.sos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sosOpenApi() {
        return new OpenAPI().info(new Info()
                .title("RideShare SOS API")
                .description("Roadside assistance microservice: mechanics, SOS requests and interventions")
                .version("1.0.0"));
    }
}