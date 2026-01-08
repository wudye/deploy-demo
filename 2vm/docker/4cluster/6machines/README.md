# Redis Cluster - 6 VM 完全隔离部署方案

## 📋 架构说明

**6台独立VM，每台运行1个Redis实例，保证每个Master与其Slave完全物理隔离**

```
┌─────────────────────────────────────────────────────────┐
│  VM1 (192.168.80.129)    Master A [Slot 0-5460]         │
│  VM2 (192.168.80.130)    Master B [Slot 5461-10922]     │
│  VM3 (192.168.80.131)    Master C [Slot 10923-16383]    │
│  VM4 (192.168.80.132)    Slave of Master A              │
│  VM5 (192.168.80.133)    Slave of Master B              │
│  VM6 (192.168.80.134)    Slave of Master C              │
└─────────────────────────────────────────────────────────┘

故障容忍：
  - VM1 宕机 → VM4 的 Slave 自动提升为 Master
  - VM2 宕机 → VM5 的 Slave 自动提升为 Master
  - VM3 宕机 → VM6 的 Slave 自动提升为 Master
```

## 🚀 快速部署

### 前置条件

```bash
# 6台VM (示例IP)
192.168.80.129 - Master A
192.168.80.130 - Master B
192.168.80.131 - Master C
192.168.80.132 - Slave A
192.168.80.133 - Slave B
192.168.80.134 - Slave C

# 每台VM安装Docker
sudo apt update && sudo apt install -y docker.io docker-compose
sudo systemctl enable docker && sudo systemctl start docker
```

### Step 1: 在每台VM上创建配置目录

```bash
# 在所有6台VM上执行
mkdir -p ~/redis-cluster
cd ~/redis-cluster
```

### Step 2: 复制配置文件到每台VM

**redis.conf (所有VM通用):**
```bash
cat > redis.conf << 'EOF'
port 6379
bind 0.0.0.0
protected-mode no

# Cluster 配置
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 15000

# 持久化
appendonly yes
appendfsync everysec
dir /data

# 可选：密码认证
# requirepass yourpassword
# masterauth yourpassword
EOF
```

**docker-compose.yml (所有VM通用):**
```bash
cat > docker-compose.yml << 'EOF'
version: '3.8'
services:
  redis:
    image: redis:7.0
    container_name: redis
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]
    volumes:
      - ./redis.conf:/usr/local/etc/redis/redis.conf
      - ./data:/data
    ports:
      - "6379:6379"
      - "16379:16379"
    restart: unless-stopped
    network_mode: host
EOF
```

**创建数据目录:**
```bash
# 所有VM上执行
sudo mkdir -p ./data
sudo chown -R 999:999 ./data
```

### Step 3: 启动所有Redis实例

```bash
# 在每台VM上执行
cd ~/redis-cluster
sudo docker-compose up -d

# 验证
sudo docker logs redis | grep "Ready to accept"
```

### Step 4: 验证网络连通性

```bash
# 从任意VM测试所有节点
for ip in 129 130 131 132 133 134; do
  redis-cli -h 192.168.80.$ip -p 6379 ping
done
# 所有应返回 PONG
```

### Step 5: 创建集群

**从任意VM执行一次:**

```bash
redis-cli --cluster create \
  192.168.80.129:6379 \
  192.168.80.130:6379 \
  192.168.80.131:6379 \
  192.168.80.132:6379 \
  192.168.80.133:6379 \
  192.168.80.134:6379 \
  --cluster-replicas 1
```

输入 `yes` 确认配置。

### Step 6: 验证集群

```bash
# 查看集群状态
redis-cli -c -h 192.168.80.129 -p 6379 cluster info

# 期望输出:
# cluster_state:ok
# cluster_slots_assigned:16384
# cluster_known_nodes:6

# 查看节点详情
redis-cli -c -h 192.168.80.129 -p 6379 cluster nodes
```

## ✅ 测试

### 读写测试

```bash
# 写入数据
redis-cli -c -h 192.168.80.129 -p 6379 SET user:1000 "Alice"
redis-cli -c -h 192.168.80.130 -p 6379 SET user:2000 "Bob"

# 从任意节点读取
redis-cli -c -h 192.168.80.131 -p 6379 GET user:1000
redis-cli -c -h 192.168.80.132 -p 6379 GET user:2000
```

### 故障转移测试

```bash
# 1. 停止Master A (VM1)
ssh 192.168.80.129 "cd ~/redis-cluster && sudo docker-compose stop"

# 2. 等待10秒，检查集群状态
redis-cli -c -h 192.168.80.130 -p 6379 cluster nodes
# Slave (VM4) 应提升为新 Master

# 3. 验证数据仍可访问
redis-cli -c -h 192.168.80.130 -p 6379 GET user:1000

# 4. 恢复原Master
ssh 192.168.80.129 "cd ~/redis-cluster && sudo docker-compose start"
```

## 🔧 故障排查

### 问题1: "Waiting for the cluster to join" 卡住

**原因:** Cluster总线端口未开放

**解决:**
```bash
# 检查端口
sudo docker ps --format "table {{.Names}}\t{{.Ports}}"
# 应看到 6379 和 16379 都已映射

# 检查防火墙
sudo ufw allow 6379/tcp
sudo ufw allow 16379/tcp
```

### 问题2: Permission denied on nodes.conf

**原因:** 数据目录权限不正确

**解决:**
```bash
# 查看Redis容器UID
sudo docker run --rm redis:7.0 id redis

# 设置权限 (通常是999)
sudo chown -R 999:999 ./data
```

### 问题3: cluster_state:fail

**原因:** 集群创建失败

**解决:**
```bash
# 清理所有节点
for ip in 129 130 131 132 133 134; do
  ssh 192.168.80.$ip "cd ~/redis-cluster && sudo docker-compose down && sudo rm -f ./data/nodes.conf"
done

# 重启所有节点
for ip in 129 130 131 132 133 134; do
  ssh 192.168.80.$ip "cd ~/redis-cluster && sudo docker-compose up -d"
done

# 重新创建集群
redis-cli --cluster create ...
```

## 📊 性能指标

| 指标 | 值 |
|------|-----|
| **总QPS** | 30万+ (每Master约10万) |
| **故障恢复时间** | 秒级自动 |
| **最大容忍故障** | 任意1个Master或Slave |
| **数据分片** | 16384个Hash Slot均分 |

## 🎯 优势

✅ **完全物理隔离** - Master与Slave永不同VM  
✅ **最高可用性** - 任意VM宕机不影响服务  
✅ **自动故障转移** - 无需人工干预  
✅ **线性扩展** - 可随时添加新Master/Slave对  
✅ **数据分片** - 支持TB级数据存储  

## 📝 运维命令

```bash
# 查看集群信息
redis-cli -c -h 192.168.80.129 -p 6379 cluster info

# 查看节点状态
redis-cli -c -h 192.168.80.129 -p 6379 cluster nodes

# 手动故障转移
redis-cli -h 192.168.80.132 -p 6379 cluster failover

# 添加新节点
redis-cli --cluster add-node 192.168.80.135:6379 192.168.80.129:6379

# 重新分配slot
redis-cli --cluster reshard 192.168.80.129:6379

# 删除节点
redis-cli --cluster del-node 192.168.80.129:6379 <node-id>
```
