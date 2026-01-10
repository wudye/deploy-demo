EnableJpaRepositories
Spring Boot 自动配置机制
Spring Boot 自动扫描
在 Spring Boot 应用中， @EnableJpaRepositories 是自动配置的，无需手动添加。
启动应用
↓
检测到 spring-boot-starter-data-jpa 依赖
↓
检测到 @Entity 注解的类
↓
自动配置 JpaRepositoriesAutoConfiguration
↓
自动添加 @EnableJpaRepositories
↓
自动扫描主应用类所在包及其子包
↓
为所有 JpaRepository 接口创建代理实现

场景对比表
场景	@EnableJpaRepositories	Repository 位置	结果
Spring Boot，Repository 在主包	自动	com.example.app	✅ 正常
Spring Boot，Repository 在子包	自动	com.example.app.repositories	✅ 正常
Spring Boot，Repository 在其他包	需要	com.example.repositories	❌ 失败，需要配置
传统 Spring，Repository 在主包	需要	com.example.app	❌ 失败，需要配置
传统 Spring，Repository 在子包	需要	com.example.app.repositories	❌ 失败，需要配置

Spring Boot 默认扫描路径
默认扫描规则
Spring Boot 默认扫描主应用类所在包及其子包：

主应用类: com.example.app.Application
↓
默认扫描路径: com.example.app
↓
扫描所有子包:
- com.example.app
- com.example.app.repositories  ← ✅ 自动扫描
- com.example.app.services    ← ✅ 自动扫描
- com.example.app.controllers  ← ✅ 自动扫描