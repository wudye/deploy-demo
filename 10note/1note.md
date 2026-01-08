original from https://github.com/Rapter1990/geospatial-location-with-redis

# maven plugins config
# application.yml file import sensitive values from env
# redis config
    方式 2 是更好的选择，它充分利用了 Spring Boot 的自动配置，减少了重复代码，提供了更好的性能和可维护性。
    redisStandaloneConfiguration 是一个配置对象，用于描述 Redis 的主机、端口、数据库、密码等连接参数（只是数据/配置，不会建立连接）。
    LettuceConnectionFactory 是实际的连接工厂，会创建 Redis 客户端连接（使用 Lettuce）。可以用 new LettuceConnectionFactory(RedisStandaloneConfiguration) 把配置传进去，也有直接用 new LettuceConnectionFactory(host, port) 的快捷构造，但前者更灵活（方便设置密码、数据库、客户端名称等）。
    在 Spring 中以 @Bean 返回 LettuceConnectionFactory 时，Spring 会管理其生命周期并初始化连接工厂；推荐使用 RedisStandaloneConfiguration 构建 LettuceConnectionFactory。
    
    方式	优点	缺点
    RedisStandaloneConfiguration	灵活，支持更多配置（密码、数据库、超时等）	代码稍多
    直接传递 host/port	简单	配置选项有限

# lettuce vs jedis

特性	Lettuce	Jedis
架构	异步、非阻塞	同步、阻塞
连接池	支持	支持
线程安全	是	否
Spring 默认	✅	❌

# GeoOperations 是 Spring Data Redis 提供的地理空间操作接口，用于处理地理位置数据，如计算距离、查找附近地点等。


# 注解	注册为 Bean	绑定配置属性	可以包含 @Bean	用途
@ConfigurationProperties	❌ 否	✅ 是	❌ 否	仅标记配置属性类
@Component	✅ 是	✅ 是	❌ 否	注册为 Bean + 绑定配置
@Configuration	✅ 是	✅ 是	✅ 是	配置类 + 绑定配置

方式	注册为 Bean	绑定配置	推荐	适用场景
@ConfigurationProperties + @EnableConfigurationProperties	✅	✅	⭐⭐⭐	推荐，职责分离
@Component + @ConfigurationProperties	✅	✅	⭐⭐	简单场景
@Configuration + @ConfigurationProperties	✅	✅	⭐	复杂配置（不推荐用于属性类）
@ConfigurationProperties 单独使用	❌	✅	❌	需要额外启用才能注入

# openapiconfig 4 ways

# internationalconfig
    Resource Bundle 'messages' 的含义
    什么是 Resource Bundle
    Resource Bundle（资源束） 是 Java 中用于实现国际化的标准机制，它是一组包含本地化数据的属性文件集合。

    在 IDE 中的显示
    当你在 IDE（如 IntelliJ IDEA 或 Eclipse）中看到名为 "Resource Bundle 'messages'" 的文件夹时：

    📁 这不是真实的文件系统文件夹
    📁 这是 IDE 的虚拟视图，用于方便管理多语言文件
    📁 它将所有相关的 .properties 文件分组显示

# enum vs static
特性	enum class	static class
实例数量	有限，编译时确定	无限，运行时创建
内存使用	实例共享，占用少	每次创建新实例
比较性能	引用比较（最快）	取决于 equals()
灵活性	低（固定常量）	高（动态创建）
适用场景	常量、状态机	Builder、配置、辅助类


# 修饰符对比总结表
修饰符	作用	适用场景	示例
protected	允许子类重写	异常处理器、模板方法	protected void handle()
public	公开访问	REST API、服务方法	public ResponseEntity getData()
private	仅本类访问	辅助方法	private String formatError()
default	同包访问	包内共享方法	void packageMethod()
修饰符	作用	适用场景	示例
final 参数	防止参数重新赋值	所有方法参数	void handle(final Exception ex)
无 final	参数可修改	需要修改参数	void update(User user)
最佳实践建议

# 泛型
概念	语法	说明	示例
类级泛型	class MyClass<T>	整个类使用	CustomPage<T>
方法级泛型	<T> void method()	仅方法使用	static <T> T max(List<T>)
泛型边界	<T extends Number>	限制类型范围	<T extends Comparable<T>>
上界通配符	<? extends T>	T 或子类	List<? extends Number>
下界通配符	<? super T>	T 或父类	List<? super Integer>
无界通配符	<?>	任何类型	List<?>
类型见证	.<T>	明确指定类型	CustomPage.<T>builder()
关键点总结:

静态方法不能使用类级泛型，必须声明方法级泛型
多个泛型参数表示不同的类型关系（如输入和输出）
类型见证明确告诉编译器泛型类型，避免推断失败
泛型擦除意味着运行时泛型信息不存在

# @Builder vs @SuperBuilder
若类无继承关系或只为单个类生成 builder，使用 \@Builder`；若类有继承且希望子类 builder 能设置父类字段，使用 `@SuperBuilder``。

# MapStruct
    MapStruct 使用步骤:

    ✅ 添加依赖
    ✅ 定义实体和 DTO
    ✅ 创建 Mapper 接口
    ✅ 编译生成实现类
    ✅ 使用 Mapper 转换对
# CommandLineRunner
    @Component
    public class MyInitializer implements CommandLineRunner {
        
        @Override
        public void run(String... args) {
            // 启动后执行的任务
            // - 初始化数据
            // - 预热缓存
            // - 执行检查
        }
    }
# redis for zset
redis-cli KEYS \*（不推荐在生产环境）或 redis-cli SCAN 0。
    # 查看类型
    redis-cli TYPE vehicle_location

    # 查看成员数量
    redis-cli ZCARD vehicle_location

    # 列出所有成员及其分数
    redis-cli ZRANGE vehicle_location 0 -1 WITHSCORES

    # 原始输出（避免额外格式）
    redis-cli --raw ZRANGE vehicle_location 0 -1 WITHSCORES

    # 只列出成员（不含分数）
    redis-cli ZRANGE vehicle_location 0 -1

    # 获取某个成员的分数
    redis-cli ZSCORE vehicle_location <member>

    # 若集合很大，分批扫描
    redis-cli ZSCAN vehicle_location 0
    # 或带过滤/批量大小（注意转义星号）
    redis-cli ZSCAN vehicle_location 0 MATCH \* COUNT 100
    root@1280d7be5b43:/data# redis-cli ZRANGE vehicle_location 0 -1 WITHSCORES
    1) "car_1"
    2) "3508665439041966"
    Redis 的 GEO 命令底层把地理位置存到一个有序集合（zset）里，zset 的 score 不是经纬度或距离，而是 GeoHash 编码（以数值形式存储）。所以 ZRANGE ... WITHSCORES 会显示成员名和对应的 GeoHash 值（看起来像大整数）。要获取真实经纬度或两个点之间的距离，请用相应的 Geo 命令。
    查看经纬度：redis-cli GEOPOS vehicle_location car_1
    查看两个成员间距离：redis-cli GEODIST vehicle_location car_1 car_2 km
    查看 GeoHash 字符串：redis-cli GEOHASH vehicle_location car_1

# jacoco report

# test
 controllerbase:
    @SpringBootTest                          // ✅ 需要启动 Spring
    @AutoConfigureMockMvc                    // ✅ 需要 HTTP 模拟
servicebase:
@ExtendWith(MockitoExtension.class)           // ✅ 需要 Mockito 扩展
@MockitoSettings(strictness = Strictness.LENIENT)  // ✅ 需要配置 Mock 行为

# Dockfile

# kubernate
