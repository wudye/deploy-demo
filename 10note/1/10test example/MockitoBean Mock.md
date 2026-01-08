心区别总结
特性	@Mock	@MockitoBean
适用场景	单元测试（无 Spring）	集成测试（有 Spring）
依赖注入	❌ 不注入到 Spring 容器	✅ 注入到 Spring 容器
Bean 替换	❌ 不替换 Bean	✅ 替换 Spring Bean
测试类型	AbstractBaseServiceTest	AbstractRestControllerTest
性能	⚡ 快（不启动 Spring）	🐢 慢（启动 Spring）
隔离性	✅ 完全隔离	⚠️ 部分（共享 Spring 上下文）

测试开始
    ↓
@SpringBootTest 启动 Spring 上下文
    ├─ 加载所有 Bean
    ├─ 扫描 @MockitoBean 注解
    └─ 用 Mock Bean 替换真实 Bean
        ↓
Controller 使用 Mock Bean（而不是真实 Bean）
        ↓
MockMvc 发送 HTTP 请求
        ↓
Controller 处理请求
        ↓
调用 Mock Bean（而不是真实 Service）
        ↓
返回响应

特点	说明
✅ 集成测试	测试完整的 Spring 流程
✅ Bean 替换	替换 Spring 容器中的 Bean
✅ 真实环境	接近生产环境
✅ MockMvc	可以测试 HTTP 层
⚠️ 较慢	需要启动 Spring 上下文
⚠️ 共享状态	多个测试共享 Spring 上下文

试类型是什么？
    ↓
需要 Spring 上下文吗？
    ├─ 是 → 需要替换 Bean 吗？
    │         ├─ 是 → 使用 @MockitoBean
    │         │          - Controller 测试
    │         │          - 集成测试
    │         │
    │         └─ 否 → 不需要 Mock
    │                  - 使用真实 Bean
    │
    └─ 否 → 使用 @Mock
              - Service 单元测试
              - Repository Mock 测试
              - 纯 Java 测试
总结
注解	用途	基类	Spring 容器	性能
@Mock	单元测试	AbstractBaseServiceTest	❌ 不需要	⚡ 快
@MockitoBean	集成测试	AbstractRestControllerTest	✅ 需要	🐢 慢
核心原则: