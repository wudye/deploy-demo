//package com.easybank.gatewayserver.config.demo;
//
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//
///**
// * 从 Keycloak 风格的 JWT 提取角色并转换为 GrantedAuthority。
// * - realm_access.roles -> ROLE_{role}
// * - resource_access.{client}.roles -> ROLE_{CLIENT}_{role} (可根据需要调整)
// */
//public class KeycloakRoleConverterDemo implements Converter<Jwt, Collection<GrantedAuthority>> {
//
//    @Override
//    public Collection<GrantedAuthority> convert(Jwt jwt) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        // realm_access.roles
//        Object realmAccessObj = jwt.getClaim("realm_access");
//        System.out.println("realm_access: " + realmAccessObj);
//        if (realmAccessObj instanceof Map) {
//            Object roles = ((Map<?, ?>) realmAccessObj).get("roles");
//            System.out.println("roles: " + roles);
//            if (roles instanceof List) {
//                ((List<?>) roles).forEach(r -> authorities.add(
//                        new SimpleGrantedAuthority("ROLE_" + r.toString())));
//            }
//        }
//
//        // resource_access.{client}.roles
//        Object resourceAccessObj = jwt.getClaim("resource_access");
//        if (resourceAccessObj instanceof Map) {
//            Map<?, ?> resourceAccessMap = (Map<?, ?>) resourceAccessObj;
//            resourceAccessMap.forEach((client, clientObj) -> {
//                if (clientObj instanceof Map) {
//                    Object clientRoles = ((Map<?, ?>) clientObj).get("roles");
//                    System.out.println("clientRoles: " + clientRoles);
//                    if (clientRoles instanceof List) {
//                        ((List<?>) clientRoles).forEach(r ->
//                                authorities.add(new SimpleGrantedAuthority(
//                                        "ROLE_" + client.toString().toUpperCase() + "_" + r.toString()))
//                        );
//                    }
//                }
//            });
//        }
//
//        return authorities;
//    }
//}
