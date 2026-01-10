package com.mwu.whatsappclone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsFilterConfiguration {

/*
    // 使用 @ConfigurationProperties 将 application.cors.* 绑定到 CorsConfiguration
    @Bean
    @ConfigurationProperties(prefix = "application.cors", ignoreUnknownFields = false)
    public CorsConfiguration corsConfiguration() {
        return new CorsConfiguration();
    }

    // 将上面绑定的 CorsConfiguration 注册到 UrlBasedCorsConfigurationSource 并作为 CorsConfigurationSource 暴露
    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
*/
    /*
        private final CorsConfiguration corsConfiguration;

    public CorsFilterConfiguration(CorsConfiguration corsConfiguration) {
        this.corsConfiguration = corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfiguration);
        source.registerCorsConfiguration("/**", corsConfiguration);
        source.registerCorsConfiguration("http://localhost:4200", corsConfiguration);
        return new CorsFilter(source);
    }
     */

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许前端源（开发环境）
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // 预检请求需要放行 OPTIONS
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允许常用请求头（含 Authorization，会触发预检）
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

        // 如需前端读取响应头可在此暴露
        config.setExposedHeaders(List.of("Content-Type"));

        // 如果你不使用 Cookie/Session，可保持 false；若使用则需 true 且 allowedOrigins 不能是 "*"
        config.setAllowCredentials(false);

        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}
