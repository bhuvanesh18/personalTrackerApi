package com.bhuvi.personalTrackerAPI;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Personal Tracker API")
                        .version("1.0.0")
                        .description("API for tracking personal health and wellness metrics")
                        .contact(new Contact()
                                .name("Bhuvi")
                                .url("https://personal-tracker-pi-five.vercel.app")));
    }
}

