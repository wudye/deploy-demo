# ❄️ 冷备 (Cold Backup) 完全指南

## 📖 什么是冷备？

冷备 (Cold Backup) 是指在系统**离线状态下**进行的数据备份。这是一种传统但极其可靠的数据保护方式。

---

## 🔧 冷备的核心特点

### 1. 系统状态特性

| 特性 | 说明 |
|------|------|
| **离线备份** | Redis服务需要停止或处于非活跃状态 |
| **静态数据** | 备份的是某个时间点的数据快照 |
| **强一致性** | 保证数据文件在备份时不会被修改 |

### 2. RDB 为什么完美适合冷备？

从你的 Redis 配置可以看到 RDB 的工作机制：

```bash
save 900 1      # 900秒内至少1次写入触发保存
save 300 10     # 300秒内10次写入触发保存  
save 60 10000   # 60秒内10000次写入触发保存

dbfilename dump.rdb
dir /data
```

#### 🎯 RDB 冷备的四大优势

| 优势 | 说明 | 价值 |
|------|------|------|
| **单一文件** | 只需备份 `dump.rdb` 一个文件 | 简化备份流程 |
| **压缩格式** | 二进制压缩格式，占用空间小 | 节省存储成本 |
| **完整性** | 包含完整数据库快照，非增量 | 数据恢复可靠 |
| **跨版本** | 可在不同 Redis 版本间迁移 | 兼容性强 |

---

## 🆚 冷备 vs 热备对比

| 特性 | ❄️ 冷备 (Cold Backup) | 🔥 热备 (Hot Backup) |
|------|----------------------|---------------------|
| **系统状态** | 停机/离线 | 运行中/在线 |
| **数据一致性** | 强一致 | 最终一致 |
| **业务影响** | 有服务中断 | 无服务中断 |
| **备份速度** | 快 | 相对慢 |
| **恢复速度** | 快 | 快 |
| **实现复杂度** | 简单 | 复杂 |
| **数据完整性** | 100% 保证 | 可能丢失少量数据 |

---

## 🎯 实际应用场景

### ❄️ 冷备适用场景

| 场景 | 使用时机 | 优势 |
|------|----------|------|
| **定期备份** | 每天凌晨业务低峰期 | 不影响业务 |
| **灾难恢复** | 异地存储离线备份 | 数据安全隔离 |
| **数据迁移** | 升级版本或迁移服务器 | 数据完整性保证 |
| **合规要求** | 需要 immutable 备份数据 | 满足审计要求 |

### 🔥 热备适用场景

| 场景 | 使用时机 | 优势 |
|------|----------|------|
| **实时同步** | 主从复制、哨兵模式 | 数据实时性 |
| **高可用** | 集群模式数据同步 | 服务不中断 |
| **连续备份** | 业务不能中断的场景 | 7×24小时运行 |

---

## 🛠️ 项目中的冷备实践

### 当前 Docker 配置

```yaml
volumes:
  - ./redis1_data:/data  # RDB文件存储在宿主机
```

### 📋 冷备操作实战

#### 方案一：安全冷备（推荐）

```bash
# 1. 停止Redis服务（确保数据完整性）
docker stop cluster_redis1

# 2. 创建带时间戳的备份
mkdir -p ./backup
cp ./redis1_data/dump.rdb ./backup/redis_$(date +%Y%m%d_%H%M%S).rdb

# 3. 验证备份文件
ls -lh ./backup/

# 4. 启动Redis服务
docker start cluster_redis1

# 5. 验证服务状态
docker logs cluster_redis1 | tail -10
```

#### 方案二：热备份（无服务中断）

```bash
# 1. 触发RDB快照
docker exec cluster_redis1 redis-cli BGSAVE

# 2. 等待快照完成
docker exec cluster_redis1 redis-cli LASTSAVE

# 3. 复制快照文件
cp ./redis1_data/dump.rdb ./backup/redis_$(date +%Y%m%d_%H%M%S).rdb

# 4. 验证备份完整性
redis-cli --rdb ./backup/redis_$(date +%Y%m%d_%H%M%S).rdb
```

### 📊 自动化备份脚本

```bash
#!/bin/bash
# redis_backup.sh - Redis 冷备自动化脚本

BACKUP_DIR="/data/redis_backup"
REDIS_CONTAINER="cluster_redis1"
REDIS_DATA_DIR="./redis1_data"
RETENTION_DAYS=30

# 创建备份目录
mkdir -p $BACKUP_DIR

# 执行备份
echo "开始 Redis 冷备..."
docker exec $REDIS_CONTAINER redis-cli BGSAVE

# 等待备份完成
sleep 5
LASTSAVE=$(docker exec $REDIS_CONTAINER redis-cli LASTSAVE)
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

# 复制备份文件
cp $REDIS_DATA_DIR/dump.rdb $BACKUP_DIR/redis_$TIMESTAMP.rdb

# 压缩备份文件
gzip $BACKUP_DIR/redis_$TIMESTAMP.rdb

# 清理过期备份
find $BACKUP_DIR -name "redis_*.rdb.gz" -mtime +$RETENTION_DAYS -delete

echo "备份完成: redis_$TIMESTAMP.rdb.gz"
```

### 🕐 定时备份配置

```bash
# 添加到 crontab
crontab -e

# 每天凌晨2点执行备份
0 2 * * * /path/to/redis_backup.sh >> /var/log/redis_backup.log 2>&1

# 每小时执行增量备份（可选）
0 * * * * /path/to/redis_backup.sh incremental >> /var/log/redis_backup.log 2>&1
```

---

## 🎯 最佳实践总结

### ✅ 冷备最佳实践

1. **备份时机选择**
   - 业务低峰期执行
   - 定期验证备份完整性
   - 异地存储多重备份

2. **文件管理策略**
   - 使用时间戳命名
   - 定期清理过期备份
   - 压缩节省存储空间

3. **监控与告警**
   - 备份成功/失败监控
   - 磁盘空间使用监控
   - 备份文件完整性检查

### 🚨 注意事项

1. **服务影响评估**
   - 冷备会导致服务中断
   - 需要评估业务容忍度
   - 考虑使用主从架构分离

2. **数据一致性**
   - 确保备份期间无数据写入
   - 验证备份文件的完整性
   - 定期测试恢复流程

---

## 💡 为什么选择 RDB 冷备？

RDB 冷备提供了**简单、可靠、高效**的离线备份方案：

- **简单性**: 单一文件，操作简单
- **可靠性**: 完整快照，数据一致性强  
- **高效性**: 压缩格式，恢复速度快
- **经济性**: 存储成本低，维护简单

这就是为什么在企业级 Redis 运维中，RDB 冷备仍然是不可或缺的数据保护策略。