计理念:

集成测试: 启动完整的 Spring 应用上下文
HTTP 模拟: 使用 MockMvc 模拟 HTTP 请求
JSON 处理: 提供 ObjectMapper 进行 JSON 序列化/反序列化
代码复用: 避免重复配置

@SpringBootTest
作用
启动完整的 Spring Boot 应用上下文，进行集成测试。

工作原理
@SpringBootTest 注解
    ↓
启动 Spring 应用上下文
    ├─ 加载所有 Bean
    ├─ 扫描 @Component、@Service、@Controller 等
    ├─ 配置数据源、Redis 等
    └─ 初始化 Spring MVC、Security 等
    ↓
测试环境就绪

特性
特性	说明
完整上下文	加载所有配置和 Bean
真实环境	接近生产环境的配置
自动配置	自动配置测试环境
Web 环境	启用 Web 功能（MockMvc）

什么是 MockMvc？
MockMvc 是 Spring 提供的 HTTP 测试工具，可以在不启动真实 HTTP 服务器的情况下测试 Controller 层。

工作原理
测试代码
    ↓
MockMvc.perform() 发送 HTTP 请求
    ↓
DispatcherServlet 处理请求
    ├─ 路由到 Controller
    ├─ 调用 Service
    ├─ 处理业务逻辑
    └─ 返回响应
    ↓
MockMvc 返回结果
    ↓
断言验证
特性
特性	说明
模拟 HTTP	不需要真实 HTTP 服务器
完整流程	模拟完整的请求-响应流程
断言支持	丰富的断言 API
性能好	比启动真实服务器快

测试生命周期
1. 测试类加载
    ↓
2. 继承 AbstractTestContainerConfiguration
    ├─ 检查 Docker 是否可用
    ├─ 启动 Redis 容器
    └─ 配置 Redis 连接
    ↓
3. 继承 AbstractRestControllerTest
    ├─ 启动 Spring 应用上下文
    ├─ 加载所有 Bean
    ├─ 配置 MockMvc
    └─ 注入 ObjectMapper
    ↓
4. 执行测试方法
    ├─ @BeforeEach 方法
    ├─ @Test 方法
    └─ @AfterEach 方法
    ↓
5. 清理
    ├─ 停止 Redis 容器
    └─ 关闭 Spring 上下文


总结表
组件	作用	关键点
@SpringBootTest	启动完整 Spring 上下文	集成测试环境
@AutoConfigureMockMvc	配置 MockMvc	HTTP 模拟
MockMvc	模拟 HTTP 请求/响应	测试 REST API
ObjectMapper	JSON 序列化/反序列化	处理 JSON 数据
AbstractTestContainerConfiguration	提供测试容器	Redis 容器
核心价值
价值	说明
集成测试	测试完整的 HTTP 层
MockMvc	不需要真实 HTTP 服务器
ObjectMapper	简化 JSON 处理
代码复用	避免重复配置
测试容器	提供 Redis 等依赖