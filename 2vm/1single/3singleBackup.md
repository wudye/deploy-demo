# 企业级 Redis 持久化配置策略

## 🔧 持久化配置

在企业环境中，RDB 生成策略基本可以使用默认配置，如需调整可考虑以下参数：

### RDB 配置优化
```bash
save 60 10000  # 如果希望 RDB 最多丢失 1 分钟数据，可设置每分钟生成快照
              # 注意：低峰期数据量少时，频繁快照意义不大
```

### AOF 强制配置
```bash
appendonly yes           # 必须开启 AOF
appendfsync everysec     # 每秒同步一次，平衡性能与安全性

auto-aof-rewrite-percentage 100  # AOF 文件大小膨胀到上次的两倍时重写
auto-aof-rewrite-min-size 64mb   # 根据数据量调整（16mb/32mb/64mb）
```

---

## 💾 企业级数据备份方案

### 核心原则
- **RDB 非常适合做冷备**：每次生成后不再修改，文件稳定
- **定时调度脚本**：使用 crontab 实现自动化备份(if not with docker)

### 备份层级策略

| 备份层级 | 频率 | 保留策略 | 说明 |
|---------|------|---------|------|
| **小时级** | 每小时 | 保留最近 48 小时 | 防止短时间内数据丢失 |
| **日级** | 每天 | 保留最近 1 个月 | 提供长期恢复点 |
| **云备份** | 每晚 | 永久保存 | 异地灾备保障 |

```bash
# 示例：crontab 备份脚本
# 每小时备份
0 * * * * /scripts/backup_hourly.sh
# 每天备份到云服务
0 2 * * * /scripts/backup_daily_cloud.sh
```

---

## 🔄 数据恢复方案

### 📋 恢复场景分类

#### 场景 1：Redis 进程挂掉
```bash
# 重启 Redis 进程即可
systemctl restart redis
# 自动基于 AOF 日志恢复数据
```

#### 场景 2：Redis 机器宕机
```bash
# 1. 重启机器
reboot

# 2. 尝试重启 Redis 进程
systemctl restart redis

# 3. 基于 AOF 文件恢复（如果未破损）
```

#### 场景 3：AOF 文件破损
```bash
# 使用官方工具修复 AOF 文件
redis-check-aof --fix appendonly.aof
```

#### 场景 4：AOF 和 RDB 文件丢失/损坏

**🚨 错误做法：**
```bash
# ❌ 这样做无法恢复数据
systemctl stop redis
rm appendonly.aof
cp backup/dump.rdb /data/
systemctl start redis  # 不会恢复 RDB 数据！
```

**✅ 正确做法：**
```bash
# 1. 停止 Redis
systemctl stop redis

# 2. 临时关闭 AOF
redis config set appendonly no

# 3. 拷贝 RDB 备份
cp backup/dump.rdb /data/

# 4. 重启 Redis 并确认数据恢复
systemctl start redis
redis-cli info keyspace

# 5. 热开启 AOF
redis-cli config set appendonly yes
```

> **⚠️ 重要提醒**：不要停止 Redis 后修改配置文件再启动，因为此时 AOF 文件未生成，数据会丢失。

#### 场景 5：所有 RDB 文件损坏
```bash
# 从远程云服务拉取最新 RDB 快照
scp cloud-server:/backups/redis/latest.rdb /data/dump.rdb

# 按场景 4 的正确步骤恢复
```

---

## 🎯 数据污染恢复实战

### 问题场景
```
12:00 上线新代码 → 发现 Bug → 缓存数据全部错误
```

### 恢复步骤
1. **定位问题时间点**：确定 12:00 是问题开始时间
2. **选择恢复点**：找到 11:00 的 RDB 冷备
3. **执行恢复**：按照场景 4 的正确步骤操作
4. **验证数据**：确认数据恢复到 11:00 状态

---

## 📊 最佳实践总结

### 配置原则
- ✅ **AOF 必须开启**：确保数据安全性
- ✅ **RDB 定期备份**：提供快速恢复能力
- ✅ **混合模式**：兼顾性能与安全

### 运维建议
- 🔧 **监控备份状态**：确保备份脚本正常运行
- 🔧 **定期测试恢复**：验证备份数据的可用性
- 🔧 **异地备份**：防止单点故障
- 🔧 **文档完善**：建立标准操作流程 (SOP)

### 性能权衡
| 配置 | 数据安全性 | 性能影响 | 恢复速度 |
|------|-----------|---------|---------|
| 仅 RDB | 一般 | 低 | 快 |
| 仅 AOF | 高 | 中 | 慢 |
| RDB + AOF | 最高 | 中高 | 中 |