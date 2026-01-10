在原生 Spring（非 Spring Boot）中，需要显式启用事务注解支持，例如通过 @EnableTransactionManagement（或 XML 中的 <tx:annotation-driven/>），这样框架才会注册拦截器/Advisor 来识别 @Transactional。
在 Spring Boot 项目中，通常不需要手工添加 @EnableTransactionManagement：Boot 的自动配置会在检测到 PlatformTransactionManager（例如使用 spring-boot-starter-data-jpa 时）时为你启用事务支持，所以 @Transactional 可以直接工作。
如果需要特殊配置（例如使用 AspectJ 模式或强制使用 CGLIB 代理 proxyTargetClass = true），可以显式加上 @EnableTransactionManagement。
还要注意代理限制：同类内部的自调用不会经过代理，因此内部方法调用带有 @Transactional 的方法不会生效；事务通常应用在被 Spring 管理的公开（public）方法上

// Spring Boot: 通常不需要 @EnableTransactionManagement，直接使用 @Transactional 即可
@Service
public class UserService {
@Transactional
public void createUser(UserDto dto) {
// 数据库操作，Boot 自动配置 PlatformTransactionManager 时，这里会被事务管理
}
}

// 非 Spring Boot 或需要显式配置时：开启事务管理并提供 PlatformTransactionManager
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class TxConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

Spring Boot vs 传统 Spring
特性	Spring Boot	传统 Spring
需要 @EnableTransactionManagement	❌ 不需要	✅ 需要
自动配置事务管理器	✅ 自动	❌ 手动
需要手动配置 PlatformTransactionManager	❌ 不需要	✅ 需要
@Transactional 开箱即用	✅ 是	❌ 否


Spring Boot vs 传统 Spring
特性	Spring Boot	传统 Spring
需要 @EnableTransactionManagement	❌ 不需要	✅ 需要
自动配置事务管理器	✅ 自动	❌ 手动
需要手动配置 PlatformTransactionManager	❌ 不需要	✅ 需要
@Transactional 开箱即用	✅ 是	❌ 否