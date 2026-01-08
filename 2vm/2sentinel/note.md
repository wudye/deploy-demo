sentinal 中文名是哨兵

哨兵是 redis 集群架构中非常重要的一个组件，主要功能如下

    集群监控：负责监控 redis master 和 slave 进程是否正常工作
    消息通知：如果某个 redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员
    故障转移：如果 master node 挂掉了，会自动转移到 slave node 上
    配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址
哨兵本身也是分布式的，作为一个哨兵集群去运行，互相协同工作

故障转移时，判断一个 master node 是宕机了，需要大部分的哨兵都同意才行，涉及到了分布式选举的问题
    即使部分哨兵节点挂掉了，哨兵集群还是能正常工作的，因为如果一个作为高可用机制重要组成部分的故障转移系统本身是单点的，那就很坑爹了

哨兵的核心知识
    哨兵至少需要 3 个实例，来保证自己的健壮性
    哨兵 + redis 主从的部署架构，是不会保证数据零丢失的，只能保证 redis 集群的高可用性
    对于哨兵 + redis 主从这种复杂的部署架构，尽量在测试环境中进行测试

sdown 和 odown 是两种失败状态
    sdown 是主观宕机
    一个哨兵如果自己觉得一个 master 宕机了，那么就是主观宕机
    odown 是客观宕机 如果 quorum 数量的哨兵都觉得一个 master 宕机了，那么就是客观宕机
    sdown 达成的条件很简单，如果一个哨兵 ping 一个 master，超过了 is-master-down-after-milliseconds（在哨兵配置文件中配置的） 指定的毫秒数之后，就主观认为 master 宕机
    sdown 到 odown 转换的条件很简单，如果一个哨兵在指定时间内，收到了 quorum 指定数量的其他哨兵也认为那个 master 是 sdown 了，那么就认为是 odown 了，客观认为 master 宕机了


# 检查容器状态
docker ps

# 查看 Redis master 状态
docker exec -it redis-master redis-cli -a 123456 INFO replication

# 查看 Sentinel 状态
docker exec -it sentinel-1 redis-cli -p 26379 SENTINEL masters
docker exec -it sentinel-1 redis-cli -p 26379 SENTINEL slaves mymaster

# 停止 master 以模拟故障
docker stop redis-master

# 观察 Sentinel 日志（应该看到故障检测与 failover）
docker logs sentinel-1 -f

# 检查是否成功提升了一个 replica 为新 master（等待 ~10s）
docker exec -it redis-replica-1 redis-cli -a 123456 INFO replication
# 预期：role:master 或 role:slave（取决于提升逻辑）