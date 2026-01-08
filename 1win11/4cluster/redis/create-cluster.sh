#!/bin/sh
# language: bash
set -eu

PASSWORD="123456789"
NODES="redis-6379:6379 redis-6380:6380 redis-6381:6381 redis-6382:6382 redis-6383:6383 redis-6384:6384"
WAIT_RETRIES=180
WAIT_SLEEP=1
CREATE_RETRIES=5
CREATE_SLEEP=3
POST_CREATE_WAIT=2
SLOTS_CHECK_RETRIES=30
SLOTS_CHECK_SLEEP=2
LOGDIR="/tmp/redis-cluster-init"
mkdir -p "$LOGDIR"

echo "等待 redis 节点启动并确认 cluster-enabled..."
for h in $NODES; do
  host=$(echo $h | cut -d: -f1)
  port=$(echo $h | cut -d: -f2)
  n=0
  until redis-cli -h "$host" -p "$port" -a "$PASSWORD" PING >/dev/null 2>&1 || [ $n -ge $WAIT_RETRIES ]; do
    n=$((n+1))
    sleep $WAIT_SLEEP
  done
  if [ $n -ge $WAIT_RETRIES ]; then
    echo "节点 $host:$port ping 超时，退出"
    exit 1
  fi

  # 检查 cluster-enabled 配置（若返回非 yes，提醒）
  cluster_enabled=$(redis-cli -h "$host" -p "$port" -a "$PASSWORD" CONFIG GET cluster-enabled 2>/dev/null | awk 'NR==2{print $0}' || echo "no")
  if [ "$cluster_enabled" != "yes" ]; then
    echo "警告: 节点 $host:$port 未启用 cluster（cluster-enabled != yes）。请确认 `redis-*.conf` 包含：cluster-enabled yes、bind 0.0.0.0、protected-mode no 等。"
  else
    echo "节点 $host:$port 已启用 cluster"
  fi
done

# 检查是否已有槽分配，若已有则跳过创建
check_slots() {
  host=$1; port=$2
  redis-cli -h "$host" -p "$port" -a "$PASSWORD" CLUSTER SLOTS 2>/dev/null | sed -n '1,1p' | grep -q '\[' || return 1
  return 0
}

echo "检查是否已存在集群（有已分配的槽）..."
if check_slots redis-6379 6379; then
  echo "检测到已有槽分配，跳过创建集群。"
  redis-cli -h redis-6379 -p 6379 -a "$PASSWORD" CLUSTER NODES || true
  exit 0
fi

echo "在所有节点执行 CLUSTER RESET 清理旧状态..."
for h in $NODES; do
  host=$(echo $h | cut -d: -f1)
  port=$(echo $h | cut -d: -f2)
  redis-cli -h "$host" -p "$port" -a "$PASSWORD" CLUSTER RESET >/dev/null 2>&1 || true
  echo "已重置 $host:$port"
done

echo "尝试创建集群（最多 ${CREATE_RETRIES} 次重试），日志保存在 ${LOGDIR}"
created=1
i=0
while [ $i -lt $CREATE_RETRIES ]; do
  i=$((i+1))
  echo "创建尝试 #$i ..."
  CREATE_LOG="$LOGDIR/create-attempt-$i.log"
  # 兼容不同 redis-cli 版本：使用 yes yes 管道确认
  if yes yes | redis-cli --cluster create $NODES --cluster-replicas 1 -a "$PASSWORD" >"$CREATE_LOG" 2>&1; then
    echo "redis-cli --cluster create 命令执行完成，查看日志 $CREATE_LOG"
  else
    echo "redis-cli --cluster create 返回非0，查看日志 $CREATE_LOG"
  fi

  # 等待短时间再检查槽
  sleep $POST_CREATE_WAIT

  # 检查 slots 是否被分配
  j=0
  while [ $j -lt $SLOTS_CHECK_RETRIES ]; do
    if check_slots redis-6379 6379; then
      echo "检测到槽已分配（CLUSTER SLOTS 非空）"
      created=0
      break
    fi
    j=$((j+1))
    sleep $SLOTS_CHECK_SLEEP
  done

  if [ $created -eq 0 ]; then
    # 额外再用 redis-cli --cluster check 验证（若可用）
    if command -v redis-cli >/dev/null 2>&1; then
      echo "运行 redis-cli --cluster check 进行更完整的校验..."
      redis-cli --cluster check redis-6379:6379 -a "$PASSWORD" >"$LOGDIR/cluster-check-$i.log" 2>&1 || true
      echo "cluster-check 日志: $LOGDIR/cluster-check-$i.log"
    fi
    break
  else
    echo "本次创建未完成槽分配，重试..."
    sleep $CREATE_SLEEP
  fi
done

if [ $created -ne 0 ]; then
  echo "所有创建尝试均失败，收集每个节点的调试信息："
  for h in $NODES; do
    host=$(echo $h | cut -d: -f1)
    port=$(echo $h | cut -d: -f2)
    echo "---- $host:$port ----"
    redis-cli -h "$host" -p "$port" -a "$PASSWORD" INFO REPLICATION 2>/dev/null || true
    redis-cli -h "$host" -p "$port" -a "$PASSWORD" CLUSTER INFO 2>/dev/null || true
    redis-cli -h "$host" -p "$port" -a "$PASSWORD" CLUSTER NODES 2>/dev/null || true
    redis-cli -h "$host" -p "$port" -a "$PASSWORD" CLUSTER SLOTS 2>/dev/null || true
  done
  echo "查看创建命令日志目录： $LOGDIR"
  exit 1
fi

echo "创建完成，显示 CLUSTER NODES:"
redis-cli -h redis-6379 -p 6379 -a "$PASSWORD" CLUSTER NODES || true
echo "完成。若仍看到 'Got no slots in CLUSTER SLOTS'，请检查："
echo " - 每个 redis 配置文件是否包含：cluster-enabled yes、bind 0.0.0.0、protected-mode no、cluster-config-file nodes.conf"
echo " - 容器间网络是否使用 service 名称而非 localhost，集群总线端口 (port+10000) 是否可达"
echo " - 若 nodes.conf 存在冲突，考虑删除数据卷中的 nodes.conf 并重启节点后再运行本脚本"
