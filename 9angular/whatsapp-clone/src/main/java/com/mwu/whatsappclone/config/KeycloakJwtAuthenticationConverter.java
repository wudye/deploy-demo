package com.mwu.whatsappclone.config;

import com.mwu.whatsappclone.security.AuthenticatedUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
合并后的权限集合放入 JwtAuthenticationToken 后，Spring Security
会将该 token 存入 SecurityContext 并用于后续的授权决策。
常见的使用方式有：在控制器/服务方法中注入 Authentication、
使用 @AuthenticationPrincipal 获取主体、
用方法级注解（@PreAuthorize / hasRole / hasAuthority）做权限校验，或直接从 SecurityContextHolder 读取。
 */
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    /*
    JwtGrantedAuthoritiesConverter: 这是 Spring 提供的工具类，用于从 JWT 中提取标准的权限（如 scope 声明）。调用 new JwtGrantedAuthoritiesConverter().convert(source) 将返回一个 Collection<GrantedAuthority>。
extractResourceRoles: 这是一个自定义方法，用于从 JWT 中提取资源角色（可能是自定义声明中的角色信息）。
权限合并: 使用 Stream.concat 将两组权限合并为一个流，并通过 collect(Collectors.toSet()) 转换为 Set<GrantedAuthority>，确保权限唯一性
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        return new JwtAuthenticationToken(source,
                Stream.concat(new JwtGrantedAuthoritiesConverter().convert(source).stream(),
                        extractResourceRoles(source).stream()).collect(Collectors.toSet()));
    }

    /*
    JwtGrantedAuthoritiesConverter: 这是 Spring 提供的工具类，用于从 JWT 中提取标准的权限（如 scope 声明）。调用 new JwtGrantedAuthoritiesConverter().convert(source) 将返回一个 Collection<GrantedAuthority>。
extractResourceRoles: 这是一个自定义方法，用于从 JWT 中提取资源角色（可能是自定义声明中的角色信息）。
权限合并: 使用 Stream.concat 将两组权限合并为一个流，并通过 collect(Collectors.toSet()) 转换为 Set<GrantedAuthority>，确保权限唯一性。
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return AuthenticatedUser.extractRolesFromToken(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}

/*

public class SecurityUtils {
    public static void inspect() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtToken) {
            // 可以访问原始 Jwt
            var jwt = jwtToken.getToken();
            var authorities = jwtToken.getAuthorities();
            System.out.println("sub=" + jwt.getSubject() + " auths=" + authorities);
        }
    }
}
 */