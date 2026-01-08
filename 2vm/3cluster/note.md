redis 的集群架构
    redis cluster

    支撑 N 个 redis master node，每个 master node 都可以挂载多个 slave node

    简单说：redis cluster = 多 master + 读写分离 + 高可用

redis cluster vs replication + sentinal
    如果你的数据量很少，主要是承载高并发高性能的场景，比如你的缓存一般就几个 G，单机足够了

    replication

    一个 mater，多个 slave，要几个 slave 跟你的要求的读吞吐量有关系，然后自己搭建一个 sentinal 集群，去保证 redis 主从架构的高可用性，就可以了

    redis cluster

    主要是针对海量数据+高并发+高可用的场景，海量数据，如果你的数据量很大，那么建议就用 redis cluster

分布式数据存储的核心算法，数据分布的算法

    hash 算法 -> 一致性 hash 算法（memcached） -> redis cluster 的 hash slot 算法

在 redis cluster 架构下，每个 redis 要放开两个端口号，比如一个是 6379，另外一个就是加 10000 的端口号，比如 16379

16379 端口号是用来进行节点间通信的，通过 cluster bus（集群总线）。cluster bus 的通信是用来进行故障检测，配置更新，故障转移授权

cluster bus 用了另外一种二进制的协议，主要用于节点间进行高效的数据交换，占用更少的网络带宽和处理时间

redis cluster 集群要求至少 3 个 master，去组成一个高可用，健壮的分布式的集群，每个 master 都建议至少给一个 slave，3 个 master，3 个 slave，最少的要求

正式环境下，建议都是说在 6 台机器上去搭建，至少 3 台机器，保证每个 master 都跟自己的 slave 不在同一台机器上，如果是 6 台自然更好