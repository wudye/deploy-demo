方法返回一个 Function<Authentication, Roles> 表示它返回一个“可调用的函数对象”。调用时用 apply(authentication) 传入 Authentication，得到 Roles。下面示例展示如何定义返回 Function 的方法并调用它。
```java
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionReturnExample {

    // 方法返回一个 Function<Authentication, Roles>
    public static Function<Authentication, Roles> toRolesFunc() {
        return auth -> {
            Set<Role> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(Role::from)
                    .collect(Collectors.toSet());
            return new Roles(roles);
        };
    }

    public static void main(String[] args) {
        // 简单 mock 一个 Authentication 实例（用于示例）
        Authentication mockAuth = new Authentication() {
            @Override public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList(
                        (GrantedAuthority) () -> "ROLE_USER",
                        (GrantedAuthority) () -> "ROLE_ADMIN"
                );
            }
            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Object getPrincipal() { return "user@example.com"; }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean b) throws IllegalArgumentException {}
            @Override public String getName() { return "user"; }
        };

        // 获取函数并应用它
        Function<Authentication, Roles> func = toRolesFunc();
        Roles roles = func.apply(mockAuth);
        System.out.println(roles); // 输出：Roles[ADMIN, USER]（顺序可能不同）
    }
}

class Roles {
    private final Set<Role> roles;
    public Roles(Set<Role> roles) { this.roles = roles; }
    @Override public String toString() { return "Roles" + roles; }
}

enum Role {
    USER, ADMIN;
    static Role from(String authority) {
        if (authority == null) return USER;
        return authority.contains("ADMIN") ? ADMIN : USER;
    }
}
