测试生命周期
完整执行流程
1. 测试开始
    ↓
2. @ExtendWith(MockitoExtension.class) 激活
    ↓
3. @MockitoSettings 应用（Strictness.LENIENT）
    ↓
4. @BeforeAll 静态方法执行（如果有）
    ↓
5. 每个测试方法:
    ├─ @BeforeEach 方法执行
    ├─ @Mock 字段初始化为 Mock 对象
    ├─ @InjectMocks 字段注入 Mock 对象
    ├─ @Test 方法执行
    │   ├─ when() stub 方法
    │   ├─ 调用测试方法
    │   ├─ assert 断言
    │   └─ verify 验证（可选）
    ├─ @AfterEach 方法执行
    └─ Mock 对象重置
    ↓
6. @AfterAll 静态方法执行（如果有）
    ↓
7. 测试结束


@Mock
创建一个模拟的（Mock）对象，模拟真实对象的行为。

特性
特性	说明
类型	模拟对象
初始状态	空实现（所有方法返回默认值）
方法返回值	null / 0 / false（取决于返回类型）
用途	模拟依赖服务的行为
@Mock
private UserRepository repository;

repository.findById(1L);       // 返回 null
repository.findAll();           // 返回空列表 []
repository.existsById(1L);    // 返回 false
repository.count();            // 返回 0
repository.save(user);         // 返回 null
@injectMock
自动注入 Mock 对象到被测试类中。

工作原理
InjectMocks 扫描
    ↓
查找 UserService 的构造方法/字段
    ↓
注入 @Mock 标注的对象
    ↓
创建 UserService 实例

执行流程:

1. JUnit 测试开始
    ↓
2. @ExtendWith 激活 Mockito 扩展
    ↓
3. Mockito 扫描测试类
    ├─ 发现 @Mock 注解的 UserRepository
    └─ 发现 @InjectMocks 注解的 UserService
    ↓
4. 创建 Mock 对象
    repository = Mockito.mock(UserRepository.class)
    ↓
5. 注入 Mock 对象
    userService = new UserService(repository)  // 构造器注入
    ↓
6. 执行测试方法
    ↓
7. 清理 Mock 对象


注解	作用	特点
@Mock	创建模拟对象	返回默认值
@InjectMocks	注入 Mock 对象	自动注入到被测试类
工作流程:

@Mock 创建 Mock 对象
    ↓
@InjectMocks 注入到被测试类
    ↓
when() 设置 Mock 行为
    ↓
调用被测试方法
    ↓
assertThat() 验证结果
    ↓
verify() 验证调用（可选）
这两个注解是 Mockito 测试的核心，让测试编写变得简洁高效！