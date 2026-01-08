java
package com.mwu.geodistance.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiCustomizerConfig {

    @Bean
    public OpenApiCustomiser addInfo() {
        return openApi -> openApi.info(new Info()
                .title("my")
                .version("1.0")
                .description("study (java 25)")
                .contact(new Contact().name("mwu").email("<EMAIL>"))
        );
    }
}
