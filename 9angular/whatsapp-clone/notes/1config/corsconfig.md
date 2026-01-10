在 Spring Boot 中，确实有三个不同的层级来配置 CORS。让我为你详细解释每种方式并提供示例。

📊 三种层级对比
层级	配置位置	作用范围	优先级	适用场景
Servlet 层	CorsFilter / WebMvcConfigurer	全局，最早拦截	低	完全自定义 CORS 处理
Security 层	HttpSecurity.cors()	Security 过滤器链	中	使用 Spring Security 的场景
MVC 层	@CrossOrigin	控制器/方法级别	高	细粒度控制特定端点
1️⃣ Servlet 层配置（全局配置）
方式 1.1: 使用 CorsFilter
@Configuration
public class CorsFilterConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 配置允许的源
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://example.com"));
        
        // 允许的 HTTP 方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允许的请求头
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // 允许携带凭证
        config.setAllowCredentials(true);
        
        // 预检请求缓存时间（秒）
        config.setMaxAge(3600L);
        
        // 注册路径映射
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
方式 1.2: 使用 WebMvcConfigurer
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "https://example.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        
        // 可以添加多个映射
        registry.addMapping("/public/**")
                .allowedOrigins("*")
                .allowedMethods("GET");
    }
}

Security 层配置（Spring Security）
方式 2: 在 Security 配置中启用 CORS

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 启用 Spring Security 对 CORS 的支持
            .cors().and()
            
            // 禁用 CSRF（通常前后端分离应用需要）
            .csrf().disable()
            
            // 配置认证规则
            .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            
            // 添加 JWT 认证过滤器
            .addFilterBefore(jwtAuthenticationFilter, 
                          UsernamePasswordAuthenticationFilter.class);
    }
    
    // 提供 CorsConfigurationSource
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
}
工作原理:

在 Security 过滤器链 中处理 CORS
Spring Security 会使用提供的 CorsConfigurationSource
CORS 检查发生在认证和授权之前
使用场景:

使用了 Spring Security 的应用
需要将 CORS 与认证授权集成
希望利用 Spring Security 的 CORS 处理能力

3️⃣ MVC 层配置（注解方式）
方式 3.1: 在控制器类级别使用 @CrossOrigin
@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200",
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class MessageController {

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.findAll();
    }
    
    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        return messageService.save(message);
    }
    
    @DeleteMapping("/{id}")
    public Message deleteMessage(@PathVariable Long id) {
        return messageService.delete(id);
    }
}

方式 3.2: 在方法级别使用 @CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    // 只允许 GET 方法跨域
    @GetMapping
    @CrossOrigin(origins = "http://localhost:4200")
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    // 允许多个源
    @GetMapping("/{id}")
    @CrossOrigin(origins = {"http://localhost:4200", "https://example.com"})
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    // 允许携带凭证
    @PostMapping
    @CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST}
    )
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
    
    // 未添加 @CrossOrigin 的方法不允许跨域访问
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }
}

方式 3.3: 类级别和方法级别组合使用

@RestController
@RequestMapping("/api/conversations")
// 类级别：全局配置
@CrossOrigin(
origins = "http://localhost:4200",
allowedHeaders = "*"
)
public class ConversationController {

    @GetMapping
    // 方法级别：覆盖类级别配置
    @CrossOrigin(origins = {"http://localhost:4200", "https://example.com"})
    public List<Conversation> getAllConversations() {
        return conversationService.findAll();
    }
    
    @PostMapping
    // 方法级别：继承类级别配置
    public Conversation createConversation(@RequestBody Conversation conversation) {
        return conversationService.save(conversation);
    }
    
    @GetMapping("/{id}/messages")
    // 方法级别：添加额外的源
    @CrossOrigin(
        origins = "http://localhost:4200",
        maxAge = 3600
    )
    public List<Message> getConversationMessages(@PathVariable Long id) {
        return conversationService.getMessages(id);
    }
}
场景	推荐方案	原因
所有 API 都需要相同的 CORS 策略	Security 层或 Servlet 层	一次配置，全局生效
使用了 Spring Security	Security 层	无缝集成，统一管理
不同 API 需要不同的 CORS 策略	MVC 层 @CrossOrigin	细粒度控制
部分公开 API，部分私有 API	组合使用（全局 + 注解）	灵活配置
静态资源也需要 CORS	Servlet 层 CorsFilter	覆盖所有请求
总结
配置层级	实现方式	配置位置	作用范围	适用场景
Servlet 层	CorsFilter / WebMvcConfigurer	@Configuration 类	全局	不使用 Security，静态资源 CORS
Security 层	HttpSecurity.cors()	SecurityConfig	安全过滤器链	使用 Spring Security
MVC 层	@CrossOrigin	Controller 类/方法	端点级别	细粒度控制
推荐实践:

使用 Spring Security → Security 层配置
不使用 Spring Security → Servlet 层配置
需要特殊策略 → MVC 层注解补充





Invoke-RestMethod -Uri 'http://localhost:8080/api/users/me' -Method GET -Headers @{ Authorization = 'Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJCQ3l2R1k3dmhtNnBGOGF4SDNXbzdMalc4NXZ5WFV1d2x2LXc1WGc4UFNZIn0.eyJleHAiOjE3Njc5NzAzMzQsImlhdCI6MTc2Nzk2NjczNCwianRpIjoib25ydHJvOjUyY2I5YTQ1LWRlZGItNjM2Ny01ZmYxLTQ2OTA0NWYwNzU1OCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA4MC9yZWFsbXMvd2hhdHNhcHAtY2xvbmUiLCJzdWIiOiJlMmExMGI2OC1jZjFkLTRiMzgtOGY1Ni00NTA3NzVlNmU1NWUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3aGF0c2FwcC1jbG9uZS1jbGllbnQiLCJzaWQiOiJfTEwtYmdIQWJwMGFuMjllSGlYWk4yTEIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiIsIkFETUlOIiwiVVNFUiJdfSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInJlYWxtX2FjY2VzcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIiwiQURNSU4iLCJVU0VSIl0sIm5hbWUiOiJBZG1pbiBVc2VyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW51c2VyIiwiZ2l2ZW5fbmFtZSI6IkFkbWluIiwiZmFtaWx5X25hbWUiOiJVc2VyIiwiZW1haWwiOiJhZG1pbnVzZXJAZXhhbXBsZS5jb20ifQ.UMjwGHqq3Zvxa8yT9wWMRB0BLUUvavUF4xoYqNV_FjsFsqWJhizEYHIF2Pt_XlWJZFy0ztYw9cYP6FFaqV2PToKZFVKz46U0yXe1fI2uMrq6cfho9-_IqdHE4Mu_mYglthznvYSriYbXNsNV5ADU7DYvlY0B1CI4AFIljMfDeE1KD3Q1XO_YFNVTeEH2FindJ0bmMuXZ7Bbj6_zNd6FLA5okKWgAt043aagVXSkB6ljROTSkLhoZOcZMdylphciv0x9rP-hi4TeIRYC_JDdCQt4T0rVLU3Tjbn6vtsRZRYLBBLzH1OHmTd_EBGquQX92oprFSwzySARr8Z-Tx-hfAQ' } -Verbose  