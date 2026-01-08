//package com.easybank.gatewayserver.config.demo;
//
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//
///**
// * 演示如何构造一个测试 Jwt 并用 JwtAuthenticationConverter + KeycloakRoleConverter
// * 把 JWT 内的角色转换为 Authentication，验证生成的 authorities。
// */
//public class JwtConverterExample {
//
//    public static void main(String[] args) {
//        // 构造示例 claims
//        Map<String, Object> realmAccess = Map.of("roles", List.of("ACCOUNTS"));
//        Map<String, Object> gatewayRoles = Map.of("roles", List.of("ADMIN"));
//        Map<String, Object> resourceAccess = Map.of("gatewayserver", gatewayRoles);
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("sub", "user1");
//        claims.put("realm_access", realmAccess);
//        claims.put("resource_access", resourceAccess);
//
//        Map<String, Object> headers = Map.of("alg", "none");
//
//        // 新建一个简单的 Jwt（用于测试）
//        Jwt jwt = new Jwt("token-value", Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
//
//        System.out.println("JWT: " + jwt.getTokenValue());
//        // 配置 JwtAuthenticationConverter 使用 KeycloakRoleConverter
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//       converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverterDemo());
//
//        // 转换并输出 authorities
//        AbstractAuthenticationToken auth = converter.convert(jwt);
//        System.out.println("Principal: " + auth.getName());
//        System.out.println("Authorities: " + auth.getAuthorities());
//    }
//}
