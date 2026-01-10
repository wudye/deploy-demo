# auto-commit	false	禁用自动提交，由事务管理器控制
auto-commit = true：每条语句执行后立即提交，无法在后续操作失败时回滚（不保证原子性）。
auto-commit = false：在 @Transactional 或显式事务开始/提交期间，所有操作在同一事务内，异常时可回滚。
好处：保证原子性和一致性（多步操作要么全部成功要么全部回滚），减少部分提交导致的数据不一致风险。
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

5. connection.provider_disables_autocommit: true
   hibernate:
   connection:
   provider_disables_autocommit: true
   作用： 告诉 Hibernate 连接池提供者（如 HikariCP）已经禁用了自动提交，Hibernate 不需要再次禁用。

自动提交（Auto-commit）基础概念
什么是自动提交？
自动提交（Auto-commit） 是数据库连接的一个特性，当设置为 true 时，每条 SQL 语句执行后都会立即提交到数据库，无需显式调用 COMMIT 。

数据库连接的自动提交模式
Yaml
插入
复制
新建文件
保存
应用代码
datasource:
hikari:
auto-commit: false  # HikariCP 禁用自动提交

hibernate:
connection:
provider_disables_autocommit: true  # ✅ Hibernate 知道
执行流程：

1. HikariCP 创建连接
   ↓
2. HikariCP 设置 auto-commit = false
   ↓
3. Hibernate 获取连接
   ↓
4. Hibernate 知道 HikariCP 已禁用自动提交
   ↓
5. Hibernate 跳过 conn.setAutoCommit(false)  ← 优化！
   ↓
6. 执行业务逻辑
   ↓
7. Hibernate 调用 conn.commit()
   优势：

避免重复设置
减少数据库调用
轻微的性能提升