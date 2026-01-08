java
package com.mwu.geodistance.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiProgrammaticConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("my")
                        .version("1.0")
                        .description("study (java 25)")
                        .contact(new Contact().name("mwu").email("<EMAIL>"))
                );
    }
}
