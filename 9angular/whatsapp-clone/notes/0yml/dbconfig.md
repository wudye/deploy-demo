# auto-commit	false	禁用自动提交，由事务管理器控制
    // 自动提交开启（不推荐）
    @Service
    public class UserService {
        public void updateUser(User user) {
            userRepository.save(user);   // 立即提交
            emailService.sendEmail(user); // 如果失败，用户已保存
    }
    }
    
    // 自动提交关闭（当前配置）
    @Service
    @Transactional  // 由事务管理器控制提交
    public class UserService {
        public void updateUser(User user) {
            userRepository.save(user);   // 同一事务内
            emailService.sendEmail(user); // 失败则全部回滚
        }  // 方法结束自动提交
    }


data:
    jpa:
    repositories:
        bootstrap-mode: deferred
三种启动模式对比：

模式	说明	启动时间	内存占用
lazy	延迟，首次使用时初始化	最快	最低
deferred	延迟，在应用上下文刷新后初始化	快	低
default	立即初始化

jpa:
    properties:
        hibernate:
            jdbc:
                time_zone: UTC
                batch_size: 25
                query:
                    fail_on_pagination_over_collection_fetch: true
                    in_clause_parameter_padding: true
                generate_statistics: false
                order_updates: true
                connection:
                    provider_disables_autocommit: true
                order_inserts: true
                default_schema: whatsappclone

配置项	                                    值	                        作用	性能影响
time_zone	                                UTC	                        统一时区，避免跨时区问题	无
batch_size	                                25	                        批量插入/更新时，每批 25 条	⬆️ 提升 3-10 倍
fail_on_pagination_over_collection_fetch	true	                    遇到 N+1 查询时报错，防止性能问题	⬆️ 避免慢查询
in_clause_parameter_padding	                true	                    IN 查询时固定参数数量，避免 SQL 解析缓存失效	⬆️ 提升 20-30%
generate_statistics	                        false	            不生成统计信息（生产环境）	        ⬆️ 减少开销
order_updates	                            true	按 ID 顺序更新，减少死锁	⬆️ 提升并发
order_inserts	                            true	按 ID 顺序插入，减少死锁	⬆️ 提升并发
provider_disables_autocommit	            true	连接池提供者禁用自动提交	⬆️ 事务一致性
default_schema	                            whatsappclone	默认数据库模式	无


// 不使用批量（batch_size=1）
for (Message msg : messages) {
messageRepository.save(msg);  // 1000 次数据库往返
}

// 使用批量（batch_size=25）
for (Message msg : messages) {
messageRepository.save(msg);  // 自动批量，40 次数据库往返
}
// 性能提升：约 25 倍



hibernate:
ddl-auto: none
四种模式对比：

模式	说明	适用环境
none	不自动创建/更新表	✅ 生产环境
validate	验证实体与表是否一致	开发/测试
update	自动更新表结构	开发（谨慎）
create	启动时删除并重建表	⚠️ 测试（会丢失数据）


naming:
implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
physical-strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
作用：将 Java 类名和字段名转换为数据库表名和列名。

@Entity
public class WhatsAppUser {
private String firstName;
private String email;
}
转换结果：

类名 WhatsAppUser → 表名 whatsapp_user （物理策略）
字段 firstName → 列名 first_name （物理策略）
两种策略对比：

策略类型	作用	示例
implicit-strategy	处理 JPA 注解中的逻辑名称	@Table(name="users") 使用 "users"
physical-strategy	转换为实际的数据库标识符	userName → user_name


open-in-view: false
作用：禁用 Open Session In View (OSIV) 模式



// OSIV 开启（传统方式）
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
User user = userRepository.findById(id);  // 事务结束，Session 关闭
user.getMessages().size();  // ⚠️ 延迟加载异常或额外查询
}

// OSIV 关闭（当前配置）
@GetMapping("/users/{id}")
public UserDTO getUser(@PathVariable Long id) {
User user = userRepository.findById(id);  // 事务内
return UserDTO.from(user);  // 在事务内完成所有查询
}

为什么禁用 OSIV？

⬆️ 避免隐藏的 N+1 查询问题
⬆️ 明确的事务边界
⬆️ 更好的性能控制
⬆️ 符合最佳实践


liquibase:
change-log: classpath:db/changelog/master.xml
default-schema: whatsappclone
contexts: dev

配置项	值	说明
change-log	classpath:db/changelog/master.xml	主 changelog 文件路径
default-schema	whatsappclone	默认数据库模式
contexts	dev	运行上下文（dev, test, prod）


devtools:
restart:
enabled: false
livereload:
enabled: false
作用：禁用 Spring Boot DevTools 功能。

为什么禁用？

生产环境不需要热重载
减少内存占用
提高启动速度


servlet:
multipart:
enabled: true
max-file-size: 100MB
max-request-size: 100MB
作用：允许上传大文件（如聊天中的图片、视频）。

配置项	值	说明
enabled	true	启用文件上传
max-file-size	100MB	单个文件最大 100MB
max-request-size	100MB	整个请求最大 100MB


logging:
level:
ROOT: INFO
org.hibernate.SQL: INFO
com.mwu.whatsappclone: INFO
作用：设置日志级别。

包路径	级别	说明
ROOT	INFO	全局日志级别
org.hibernate.SQL	INFO	输出 SQL 语句
com.mwu.whatsappclone	INFO	应用日志级别
日志级别对比：

级别	严重程度	用途
TRACE	最详细	调试信息
DEBUG	详细	开发调试
INFO	一般	运行信息（生产）
WARN	警告	潜在问题
ERROR	错误	需要关注