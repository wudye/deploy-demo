#!/bin/bash
# Redis Cluster 清理脚本

VMS=(
  "192.168.80.129"
  "192.168.80.130"
  "192.168.80.131"
  "192.168.80.132"
  "192.168.80.133"
  "192.168.80.134"
)

echo "=== 清理所有Redis节点 ==="
echo ""

for vm in "${VMS[@]}"; do
  echo "清理 $vm ..."
  ssh root@$vm "cd ~/redis-cluster && docker-compose down && rm -rf ./data/*"
done

echo ""
echo "✓ 清理完成"
echo ""
echo "重新部署请运行: bash deploy.sh"
