package com.easybank.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRoleConverter  implements Converter<Jwt, Collection<GrantedAuthority>> {

    /*

    realm_access 是 Keycloak 在颁发的 JWT（通常是 access token）
    里用来表示“realm 级别角色”的一个 claim。它的作用是把用户在整个
    realm（全局）上被赋予的角色列出来，方便认证/授权组件根据这些角色做访问控制。
        {
          "sub": "user1",
          "realm_access": {
            "roles": ["user", "admin"]
          }
        }
        Keycloak 颁发的 token（通常是 access token 和 id token）在
         payload 中包含若干常用 claim：例如 sub（用户标识）、iss（签发者）、
         aud（受众）、exp/iat（过期/签发时间）、scope、azp（授权客户端）、
         以及用户信息（preferred_username、email 等）。用于授权的角色通常出现在两个地方：
         全局 realm 角色在 realm_access.roles，按客户端划分的角色在
         resource_access.<client>.roles。access token 更常包含
         realm_access/resource_access，id token 则侧重身份信息。

        {
          "sub": "12345678",
          "iss": "https://auth.example.com/realms/demo",
          "aud": "account",
          "exp": 1710000000,
          "iat": 1709996400,
          "azp": "my-client",
          "scope": "openid email",
          "preferred_username": "alice",
          "session_state": "abcd-1234",
          "realm_access": {
            "roles": ["viewer", "operator"]
          },
          "resource_access": {
            "my-client": {
              "roles": ["admin", "user"]
            }
          }
        }



     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        System.out.println("test in gateway------------------");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        System.out.println(returnValue.toString());
        System.out.println("aaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbb");
        return returnValue;
    }
}
