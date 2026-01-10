Keycloak 自动设置 Authentication
Keycloak 集成的工作流程
当使用 Keycloak 作为认证提供者时，Spring Security 的 KeycloakAuthenticationProvider 会自动将认证信息设置到 SecurityContext 中。

GET /api/users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

HTTP Request
↓
SecurityFilterChain
↓
1. SecurityContextPersistenceFilter
    - 清空旧的 SecurityContext
    - 准备存储新的 SecurityContext
      ↓
2. JwtAuthenticationFilter（自定义）
    - 提取 JWT 令牌
    - 验证令牌
    - 创建 Authentication 对象
    - ❌ 不在这里设置 SecurityContext
      ↓
3. KeycloakAuthenticationFilter（如果配置）
    - 检测 Keycloak 令牌
    - 验证令牌
    - 创建 Authentication 对象
      ↓
4. ExceptionTranslationFilter
    - 处理认证异常
      ↓
5. FilterSecurityInterceptor
    - 检查访问权限
      ↓
6. 到达 Controller

1. JwtAuthenticationFilter 拦截请求
   ↓
2. 提取 Bearer Token
   String token = extractBearerToken(request);

   ↓
3. 验证 JWT 令牌
   Jwt jwt = jwtDecoder.decode(token);

   ↓
4. 调用 JwtAuthenticationConverter
   AbstractAuthenticationToken authentication =
   jwtAuthenticationConverter.convert(jwt);

   ↓
5. Keycloak/JWT 认证提供者处理
   KeycloakAuthenticationProvider.authenticate(authentication)

   ↓
6. ⭐ 关键：自动设置 SecurityContext
   SecurityContextHolder.getContext().setAuthentication(authentication);

   ↓
7. 请求继续到 Controller


@Configuration
@EnableWebSecurity
public class KeycloakSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );
        
        return http.build();
    }
    
    @Bean
    public JwtDecoder jwtDecoder() {
        // 配置 Keycloak JWT 解码器
        return NimbusJwtDecoder.withJwkSetUri(
            "http://localhost:8080/realms/whatsappclone/protocol/openid-connect/certs"
        ).build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        // 配置权限前缀
        converter.setPrincipalClaimName("preferred_username");
        
        // 配置角色转换
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // 从 JWT 中提取角色
            List<String> roles = extractRolesFromJwt(jwt);
            
            return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        });
        
        return converter;
    }
}

// Spring Security 内部实现（简化）

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 1. 提取 JWT 令牌
        String token = extractToken(request);
        
        if (token != null) {
            try {
                // 2. 解码 JWT
                Jwt jwt = jwtDecoder.decode(token);
                
                // 3. 转换为 Authentication
                AbstractAuthenticationToken authentication = 
                    jwtAuthenticationConverter.convert(jwt);
                
                // 4. ⭐ 认证并自动设置 SecurityContext
                Authentication authenticated = 
                    authenticationManager.authenticate(authentication);
                
                // ⭐ 这里会自动调用：
                // SecurityContextHolder.getContext().setAuthentication(authenticated);
                
            } catch (JwtException e) {
                // 令牌无效
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        
        // 5. 继续过滤器链
        filterChain.doFilter(request, response);
    }
}

┌─────────────────────────────────────────────────────────────┐
│ 1. 客户端发送请求                                       │
│ GET /api/users/me                                         │
│ Authorization: Bearer <JWT_TOKEN>                         │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 2. JwtAuthenticationFilter 拦截                          │
│ - 提取 Bearer Token                                      │
│ - 验证 JWT 签名和有效期                                  │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 3. JwtAuthenticationConverter 转换                       │
│ - 将 JWT 转换为 Authentication 对象                        │
│ - 提取用户名、角色、权限                                   │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 4. AuthenticationManager 认证                             │
│ - 验证 Authentication 对象                                │
│ - ⭐ 自动设置 SecurityContext                             │
│   SecurityContextHolder.getContext()                         │
│       .setAuthentication(authenticated);                    │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 5. 到达 Controller                                       │
│ - AuthenticatedUser.username() ✅ 可用                      │
│ - AuthenticatedUser.roles() ✅ 可用                        │
│ - AuthenticatedUser.attributes() ✅ 可用                    │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 6. 返回响应                                              │
│ HTTP 200 OK                                               │
│ {"email": "user@example.com", "roles": ["ADMIN", "USER"]}   │
└─────────────────────────────────────────────────────────────┘
↓
┌─────────────────────────────────────────────────────────────┐
│ 7. SecurityContextPersistenceFilter 清理                 │
│ - 清空 SecurityContext（为下一个请求准备）                     │
│ SecurityContextHolder.clearContext();                        │
└─────────────────────────────────────────────────────────────┘

为什么 Keycloak 会自动设置？
Spring Security 的设计原则
// Spring Security 的核心设计理念：

1. 声明式安全
    - 通过配置定义安全规则
    - 不需要手动管理认证过程

2. 自动化认证
    - 认证提供者自动处理令牌验证
    - 自动设置 SecurityContext

3. 透明化
    - 开发者无需关心认证细节
    - 专注于业务逻辑
- Keycloak 自动认证的完整流程
1. 客户端发送请求（带有 JWT）
   ↓
2. JwtAuthenticationFilter 拦截
   ↓
3. 提取并验证 JWT
   ↓
4. JwtAuthenticationConverter 转换为 Authentication
   ↓
5. AuthenticationManager 认证
   ↓
6. ⭐ Keycloak 自动设置 SecurityContext
   SecurityContextHolder.getContext().setAuthentication(authentication);
   ↓
7. 到达 Controller
   ↓
8. AuthenticatedUser 可以直接获取认证信息
   何时需要手动设置
   场景	是否需要手动设置	原因
   Keycloak/JWT 认证	❌ 不需要	自动设置
   表单登录	✅ 需要	手动登录
   模拟用户（测试）	✅ 需要	测试目的
   自定义认证流程	✅ 需要	自定义逻辑