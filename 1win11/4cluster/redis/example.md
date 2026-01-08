# docker/redis/redis-6379.conf
port 6379
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
requirepass 123456789
masterauth 123456789
protected-mode no
bind 0.0.0.0
dir /data

# 对外公布为容器名（若应用与 Redis 在同一 Docker network）
cluster-announce-ip redis-6379
cluster-announce-port 6379
cluster-announce-bus-port 16379

# service and redis instance names must match the port number in the same network