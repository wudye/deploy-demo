package com.mwu.whatsappclone.security;

import com.mwu.whatsappclone.exceptions.security.NotAuthenticatedUserException;
import com.mwu.whatsappclone.exceptions.security.UnknownAuthenticationException;
import com.mwu.whatsappclone.model.enums.AssertionErrorType;
import com.mwu.whatsappclone.model.enums.Role;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
AuthenticatedUser（工具类）
    ↓
功能 1：获取用户名（username）
    ↓
功能 2：获取用户角色（roles）
    ↓
功能 3：获取令牌属性（attributes）
    ↓
功能 4：从 JWT 提取角色（extractRolesFromToken）
 */
/**
 * This is an utility class to get authenticated user information
 */
public final class AuthenticatedUser {

    public static final String PREFERRED_USERNAME = "email";

    private AuthenticatedUser() {
    }

    /**
     * Get the authenticated user username
     *
     * @return The authenticated user username
     * @throws NotAuthenticatedUserException  if the user is not authenticated
     * @throws UnknownAuthenticationException if the user uses an unknown authentication scheme
     */
    public static Username username() {
        return optionalUsername().orElseThrow(NotAuthenticatedUserException::new);
    }

    /**
     * Get the authenticated user username
     *
     * @return The authenticated user username or empty if the user is not authenticated
     * @throws UnknownAuthenticationException if the user uses an unknown authentication scheme
     */

    /*
步骤 1: authentication()
返回: Optional<Authentication>
结果: Optional[JwtAuthenticationToken{...}]

步骤 2: .map(AuthenticatedUser::readPrincipal)
  - 提取 Authentication 对象
  - 调用 readPrincipal(authentication)
  - 提取用户名字符串
返回: Optional<String>
结果: Optional["user@example.com"]

步骤 3: .flatMap(Username::of)
  - 提取字符串 "user@example.com"
  - 调用 Username.of("user@example.com")
  - 创建 Username 对象
返回: Optional<Username>
结果: Optional[Username{email="user@example.com"}]
     */
    public static Optional<Username> optionalUsername() {
        return authentication().map(AuthenticatedUser::readPrincipal).flatMap(Username::of);
    }

    /**
     * Read user principal from authentication
     *
     * @param authentication authentication to read the principal from
     * @return The user principal
     * @throws UnknownAuthenticationException if the authentication can't be read (unknown token type)
     */
    public static String readPrincipal(Authentication authentication) {
        Assert.notNull("authentication", authentication);

        if (authentication.getPrincipal() instanceof UserDetails details) {
            return details.getUsername();
        }

        if (authentication instanceof JwtAuthenticationToken token) {
            return (String) token.getToken().getClaims().get(PREFERRED_USERNAME);
        }

        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return (String) oidcUser.getAttributes().get(PREFERRED_USERNAME);
        }

        if (authentication.getPrincipal() instanceof String principal) {
            return principal;
        }

        throw new UnknownAuthenticationException();
    }

    /**
     * Get the authenticated user roles
     *
     * @return The authenticated user roles or empty roles if the user is not authenticated
     */
    public static Roles roles() {
        return authentication().map(toRoles()).orElse(Roles.EMPTY);
    }

    /*
    1. 获取认证权限列表
   authentication.getAuthorities()
   ↓
   返回: Collection<GrantedAuthority>
   示例: [ROLE_ADMIN, ROLE_USER]

2. 转换为 Stream
   .stream()
   ↓
   返回: Stream<GrantedAuthority>

3. 提取权限字符串
   .map(GrantedAuthority::getAuthority)
   ↓
   返回: Stream<String>
   示例: ["ROLE_ADMIN", "ROLE_USER"]

4. 转换为 Role 枚举
   .map(Role::from)
   ↓
   返回: Stream<Role>
   示例: [Role.ADMIN, Role.USER]

5. 收集为 Set
   .collect(Collectors.toSet())
   ↓
   返回: Set<Role>
   示例: {ADMIN, USER}

6. 包装为 Roles 对象
   new Roles(set)
   ↓
   返回: Roles
     */
    /*
    private static Function<Authentication, Roles> toRoles() {
    return new Function<Authentication, Roles>() {
        @Override
        public Roles apply(Authentication authentication) {
            var rolesSet = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(Role::from)
                    .collect(Collectors.toSet());
            return new Roles(rolesSet);
        }
    };
}

// 调用示例：
Function<Authentication, Roles> func = toRoles();
Roles roles = func.apply(authenticationInstance);
     */
    private static Function<Authentication, Roles> toRoles() {
        return authentication ->
                new Roles(
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                // 只处理角色类权限，避免把 SCOPE\_xxx 等当作角色解析
                                .filter(a -> a != null && a.startsWith("ROLE_"))
                                .map(Role::from)
                                // 防御：Role.from 解析失败会返回 UNKNOWN，则丢弃
                                .filter(r -> r != Role.UNKNOWN)
                                .collect(Collectors.toSet())
                );
    }

    /**
     * Get the authenticated user token attributes
     *
     * @return The authenticated user token attributes
     * @throws NotAuthenticatedUserException  if the user is not authenticated
     * @throws UnknownAuthenticationException if the authentication scheme is unknown
     */
    public static Map<String, Object> attributes() {
        Authentication token = authentication().orElseThrow(NotAuthenticatedUserException::new);

        if (token instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }

        throw new UnknownAuthenticationException();
    }

    /*
    1. 获取 SecurityContext
   SecurityContextHolder.getContext()
   ↓
2. 获取 Authentication 对象
   .getAuthentication()
   ↓
3. 包装为 Optional
   Optional.ofNullable(...)
   ↓
4. 返回 Optional<Authentication>
     */
    private static Optional<Authentication> authentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public static List<String> extractRolesFromToken(Jwt jwtToken) {
        LinkedTreeMap<String, List<String>> realmAccess =
                (LinkedTreeMap<String, List<String>>) jwtToken.getClaims().get("realm_access");
//        return realmAccess.get("roles").stream().filter(role -> role.contains("ROLE_")).toList();

        Object raObj = jwtToken.getClaims().get("realm_access");
        if (!(raObj instanceof Map<?, ?> ra)) {
            return List.of();
        }

        Object rolesObj = ra.get("roles");
        if (!(rolesObj instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                // 兼容 Keycloak: ADMIN/USER 以及已带前缀的 ROLE_ADMIN/ROLE_USER
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .toList();
    }
}

