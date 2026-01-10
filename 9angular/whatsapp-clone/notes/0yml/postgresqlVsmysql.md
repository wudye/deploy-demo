连接池配置对比
配置项	PostgreSQL	MySQL	说明
maximum-pool-size	20	30	MySQL 建议稍大
minimum-idle	10	15	MySQL 建议稍大
connection-timeout	30000	30000	相同
idle-timeout	600000	600000	相同
max-lifetime	1800000	1800000	相同
批量配置对比
配置项	PostgreSQL	MySQL	说明
batch_size	25	25	相同
batch_versioned_data	true	false	PostgreSQL 支持
rewriteBatchedStatements	reWriteBatchedInserts	rewriteBatchedStatements	参数名不同

✅ 使用 HikariCP 连接池
✅ 禁用自动提交，由事务管理器控制
✅ 配置合适的批量大小（25）
✅ 根据数据库选择正确的方言
✅ 使用环境变量管理敏感信息
✅ 生产环境禁用统计信息
✅ 禁用 Open Session In View
✅ 配置连接池监控
✅ 使用虚拟线程提升并发性能