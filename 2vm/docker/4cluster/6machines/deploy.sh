#!/bin/bash
# Redis Cluster 6-VM 部署脚本

# 配置VM IP地址
VMS=(
  "192.168.80.129"  # Master A
  "192.168.80.130"  # Master B
  "192.168.80.131"  # Master C
  "192.168.80.132"  # Slave A
  "192.168.80.133"  # Slave B
  "192.168.80.134"  # Slave C
)

echo "=== Redis Cluster 6-VM 部署脚本 ==="
echo ""

# Step 1: 在所有VM上创建目录和配置
echo "Step 1: 在所有VM上部署配置文件..."
for vm in "${VMS[@]}"; do
  echo "  配置 $vm ..."
  ssh root@$vm "mkdir -p ~/redis-cluster"
  scp redis.conf root@$vm:~/redis-cluster/
  scp docker-compose.yml root@$vm:~/redis-cluster/
  ssh root@$vm "cd ~/redis-cluster && mkdir -p ./data && chown -R 999:999 ./data"
done
echo "✓ 配置文件部署完成"
echo ""

# Step 2: 启动所有Redis实例
echo "Step 2: 启动所有Redis实例..."
for vm in "${VMS[@]}"; do
  echo "  启动 $vm 的Redis..."
  ssh root@$vm "cd ~/redis-cluster && docker-compose up -d"
done
echo "✓ 所有实例已启动"
echo ""

# Step 3: 等待所有实例就绪
echo "Step 3: 等待所有实例就绪..."
sleep 5
for vm in "${VMS[@]}"; do
  result=$(redis-cli -h $vm -p 6379 ping 2>/dev/null)
  if [ "$result" == "PONG" ]; then
    echo "  ✓ $vm:6379 就绪"
  else
    echo "  ✗ $vm:6379 未就绪"
    exit 1
  fi
done
echo ""

# Step 4: 创建集群
echo "Step 4: 创建Redis Cluster..."
redis-cli --cluster create \
  192.168.80.129:6379 \
  192.168.80.130:6379 \
  192.168.80.131:6379 \
  192.168.80.132:6379 \
  192.168.80.133:6379 \
  192.168.80.134:6379 \
  --cluster-replicas 1 \
  --cluster-yes

echo ""
echo "=== 部署完成 ==="
echo ""
echo "验证集群状态:"
echo "  redis-cli -c -h 192.168.80.129 -p 6379 cluster info"
echo "  redis-cli -c -h 192.168.80.129 -p 6379 cluster nodes"
