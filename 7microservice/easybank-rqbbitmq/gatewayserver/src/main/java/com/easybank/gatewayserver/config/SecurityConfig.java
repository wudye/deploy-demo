package com.easybank.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/eazybank/accounts/**").hasRole("ACCOUNTS")
                        .pathMatchers("/eazybank/cards/**").hasRole("CARDS")
                        .pathMatchers("/eazybank/loans/**").hasRole("LOANS"))
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
                (new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
//                        // Permit actuator and eureka endpoints
//                        .pathMatchers("/actuator/**", "/eureka/**").permitAll()
//                        // Secure other specific routes
////                        .pathMatchers("/eazybank/accounts/**").permitAll()
//
//                     .pathMatchers("/eazybank/accounts/**").hasRole("ACCOUNTS")
//                        .pathMatchers("/eazybank/cards/**").hasRole("CARDS")
//                        .pathMatchers("/eazybank/loans/**").hasRole("LOANS")
//                        // All other requests must be authenticated
//                        .anyExchange().authenticated())
//                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
//                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
//        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
//        return serverHttpSecurity.build();
//    }
//
//    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
//        JwtAuthenticationConverter jwtAuthenticationConverter =
//                new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
//                (new KeycloakRoleConverter());
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }

}

/*
curl --location --request POST 'http://192.168.80.129:7080/realms/demo-realm/protocol/openid-connect/token' \
        --header 'Content-Type: application/x-www-form-urlencoded' \
        --data-urlencode 'grant_type=password' \
        --data-urlencode 'client_id=demo-client' \
        --data-urlencode 'client_secret=7h4aIOYL5PGQGmuxRqoQ9F0Tttp8yqzP' \
        --data-urlencode 'username=test2' \
        --data-urlencode 'password=123456'
how can do this with powershell
Invoke-RestMethod -Uri 'http://192.168.80.129:7080/realms/demo-realm/protocol/openid-connect/token' -Method POST -ContentType 'application/x-www-form-urlencoded' -Body 'grant_type=password&client_id=demo-client&client_secret=7h4aIOYL5PGQGmuxRqoQ9F0Tttp8yqzP&username=test2&password=123456'

# Paste the complete token string inside the quotes



 */
