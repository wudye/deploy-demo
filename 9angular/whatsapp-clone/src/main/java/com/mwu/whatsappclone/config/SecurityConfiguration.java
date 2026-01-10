package com.mwu.whatsappclone.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;

@Configuration
public class SecurityConfiguration {
    /*
       @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://whatsapp-clone.example.com"
        ));

        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }





     */

    /*
    - authorizeHttpRequests() → 配置访问权限
- csrf() → 配置 CSRF 保护
- oauth2ResourceServer() → 配置 OAuth2 资源服务器
- formLogin() → 配置表单登录
- httpBasic() → 配置 HTTP Basic 认证
- cors() → 配置 CORS
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.GET, "assets/*").permitAll()
                                .requestMatchers("/api/**").authenticated()



                )
                .cors(Customizer.withDefaults())


                .csrf(AbstractHttpConfigurer::disable)
                /*
                 OAuth2/JWT 认证
   - 提取 JWT 令牌
   - 验证令牌
   - 转换为 Authentication
    ↓
                 */
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())));


        return http.build();
    }


/*
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // 自定义 JWT 转换器
        return new KeycloakJwtAuthenticationConverter();
    }
 */

    /*
       @Bean
    public SecurityFilterChain configure(HttpSecurity http,
                                         Converter<Jwt, AbstractAuthenticationToken> jwtAuthConverter) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(HttpMethod.GET, "assets/*").permitAll()
                                .requestMatchers("/api/**").authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

        return http.build();
    }
      @Bean
    public Converter<Jwt, AbstractAuthenticationToken> keycloakJwtAuthenticationConverter() {
        return new Converter<Jwt, AbstractAuthenticationToken>() {
            @Override
            public AbstractAuthenticationToken convert(@NonNull Jwt source) {
                return new JwtAuthenticationToken(
                        source,
                        Stream.concat(
                                new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                                extractResourceRoles(source).stream()
                        ).collect(Collectors.toSet())
                );
            }
        };
    }
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return AuthenticatedUser.extractRolesFromToken(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
     */





}
