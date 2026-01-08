为什么 Controller 和 Service 需要特定的基类，而像 CustomSortingTest 这样的模型类测试不需要。

核心原因
Controller 和 Service 测试：需要复杂的基础设施
Controller 测试
    ├─ 需要 Spring Boot 上下文
    ├─ 需要 MockMvc（HTTP 模拟）
    ├─ 需要 ObjectMapper（JSON 处理）
    ├─ 可能需要测试容器（Redis、数据库等）
    └─ 需要依赖注入（@MockitoBean）

Service 测试
    ├─ 需要 Mockito 框架
    ├─ 需要 Mock 对象管理
    └─ 需要 LENIENT 模式配置
模型/工具类测试：不需要基础设施
CustomSorting 测试
    ├─ 不需要 Spring 上下文
    ├─ 不需要 Mock
    ├─ 不需要 HTTP 模拟
    ├─ 不需要依赖注入
    └─ 只需要测试类本身的方法


为什么不需要基类？

原因	说明
无依赖	CustomSorting 是纯 Java 类，没有外部依赖
无 Spring	不需要 @Component 、 @Service 等注解
无 Mock	不需要模拟任何对象
无容器	不需要数据库、Redis 等外部服务
快速	直接运行，无需启动任何框架

测试类型分类
类型 1: 集成测试（需要基类）
测试类型	需要	基类
Controller	Spring、MockMvc、ObjectMapper	AbstractRestControllerTest
Service（集成）	Spring、数据库、Redis	AbstractTestContainerConfiguration
特点:

⚠️ 需要启动框架
⚠️ 速度较慢
✅ 测试完整流程
类型 2: 单元测试（需要基类）
测试类型	需要	基类
Service（单元）	Mockito、@Mock、@InjectMocks	AbstractBaseServiceTest
Repository（Mock）	Mockito、Mock 数据	AbstractBaseServiceTest
特点:

⚠️ 需要 Mock 框架
✅ 速度快
✅ 隔离测试
类型 3: 纯 Java 测试（不需要基类）
测试类型	需要	基类
Model/DTO	无	不需要
Utility	无	不需要
Mapper（非 Spring）	无	不需要
Exception	无	不需要
特点:

✅ 不需要任何框架
✅ 最快
✅ 最简单

性能对比
测试启动时间
测试类型	基类	启动时间	执行时间
Controller 集成测试	AbstractRestControllerTest	2-5 秒	100-500 ms
Service 单元测试	AbstractBaseServiceTest	10-50 ms	10-50 ms
模型测试	无	< 1 ms	1-10 ms

需要测试的类是什么？
    ↓
是 Controller 吗？
    ├─ 是 → 使用 AbstractRestControllerTest
    │      - 需要 Spring 上下文
    │      - 需要 MockMvc
    │      - 需要 HTTP 模拟
    │
    ├─ 否 → 是 Service 吗？
    │         ├─ 是 → 需要集成测试吗？
    │         │         ├─ 是 → 使用 AbstractTestContainerConfiguration
    │         │         │          - 需要 Spring 上下文
    │         │         │          - 需要容器（Redis、MySQL）
    │         │         │
    │         │         └─ 否 → 使用 AbstractBaseServiceTest
    │         │                   - 需要 Mockito
    │         │                   - 需要 Mock 对象
    │         │
    │         └─ 否 → 是纯 Java 类吗？
    │                   ├─ 是 → 不使用基类
    │                   │          - Model
    │                   │          - DTO
    │                   │          - Utility
    │                   │          - Mapper（非 Spring）
    │                   │
    │                   └─ 否 → 根据具体需求
    │                              - Repository → AbstractBaseServiceTest
    │                              - Component → AbstractBaseServiceTest