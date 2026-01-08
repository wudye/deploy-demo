架构关系
┌─────────────────────────────────────────────────────────┐
│                      Kubernetes 集群                     │
│                                                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │              应用层 (App Layer)                  │   │
│  │                                                  │   │
│  │  app-service.yml                               │   │
│  │  ┌────────────────────────────────────────┐    │   │
│  │  │  geospatial-location-with-redis Service │    │   │
│  │  │  (ClusterIP, Port 1441)                  │    │   │
│  │  └────────────────────────────────────────┘    │   │
│  │                        ▲                        │   │
│  │                        │                        │   │
│  │  app-deployment.yml    │  app-config.yml       │   │
│  │  ┌──────────────────┐   │  ┌────────────────┐   │   │
│  │  │  Pod (副本 1)    │───┼──│  ConfigMap     │   │   │
│  │  │  应用容器        │   │  │  环境变量      │   │   │
│  │  └──────────────────┘   │  └────────────────┘   │   │
│  └────────────────────────┼────────────────────────┘   │
│                           │                              │
│                           ▼                              │
│  ┌──────────────────────────────────────────────────┐   │
│  │              数据层 (Data Layer)                 │   │
│  │                                                  │   │
│  │  redis-service.yml                               │   │
│  │  ┌────────────────────────────────────────┐    │   │
│  │  │         redis Service                  │    │   │
│  │  │  (ClusterIP, Port 6379)                 │    │   │
│  │  └────────────────────────────────────────┘    │   │
│  │                        ▲                        │   │
│  │                        │                        │   │
│  │  redis-deployment.yml   │  redis-pvc.yml        │   │
│  │  ┌──────────────────┐   │  ┌───────────────┐   │   │
│  │  │  Pod (副本 1)    │───┼──│  Persistent    │   │   │
│  │  │  Redis 容器      │   │  │  VolumeClaim   │   │   │
│  │  │  ┌────────────┐  │   │  │  (1Gi 存储)   │   │   │
│  │  │  │  数据卷    │──┼───┘  └───────────────┘   │   │
│  │  │  └────────────┘  │                         │   │
│  │  └──────────────────┘                         │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘

# 1. 创建 Redis 持久化存储
kubectl apply -f k8s/redis-pvc.yml

# 2. 部署 Redis
kubectl apply -f k8s/redis-deployment.yml

# 3. 创建 Redis 服务
kubectl apply -f k8s/redis-service.yml

# 4. 创建应用配置
kubectl apply -f k8s/app-config.yml

# 5. 部署应用
kubectl apply -f k8s/app-deployment.yml

# 6. 创建应用服务
kubectl apply -f k8s/app-service.yml


Kubernetes 核心概念总结
1. Pod
K8s 最小部署单元
一个或多个容器的集合
共享网络和存储
2. Deployment
管理 Pod 的副本和更新
支持滚动更新和回滚
确保指定数量的 Pod 始终运行
3. Service
为 Pod 提供稳定的网络地址
负载均衡和发现
类型：ClusterIP（内部）、NodePort（节点）、LoadBalancer（外部）
4. ConfigMap
存储非敏感配置
以环境变量或卷的形式注入 Pod
5. PersistentVolumeClaim
申请持久化存储
数据与 Pod 生命周期解耦


与 Docker Compose 的对比
特性	Docker Compose	Kubernetes
适用场景	本地开发、单机	生产环境、多节点
扩展性	有限	水平扩展
自愈能力	重启容器	自动重建 Pod
服务发现	网络名	Service
配置管理	.env 文件	ConfigMap/Secret


完整编写顺序图
依赖关系图：
┌─────────────────┐
│ app-config.yml  │ ◀──── 无依赖，先规划配置
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ redis-pvc.yml   │ ◀──── 无依赖
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ redis-depl...   │ ◀──── 引用 redis-pvc.yml
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ redis-service   │ ◀──── 引用 redis-deployment 标签
└─────────────────┘
         
         ▼ (并行)
         
┌─────────────────┐
│ app-deployment  │ ◀──── 引用 app-config + redis-service
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ app-service     │ ◀──── 引用 app-deployment 标签


依赖关系总结表
文件	依赖项	被谁依赖
app-config.yml	无	app-deployment.yml
redis-pvc.yml	无	redis-deployment.yml
redis-deployment.yml	redis-pvc.yml	redis-service.yml
redis-service.yml	redis-deployment.yml 标签	app-config.yml（通过 REDIS_HOST）
app-deployment.yml	app-config.yml, redis-service.yml	app-service.yml
app-service.yml	app-deployment.yml 标签	外部访问
最佳实践建议
开发时的思考顺序
先想配置 → app-config.yml

需要哪些环境变量？
再想存储 → redis-pvc.yml

数据是否需要持久化？
然后部署数据库 → redis-deployment.yml

数据库运行配置
暴露数据库服务 → redis-service.yml

应用如何连接数据库？
部署应用 → app-deployment.yml

引用配置和数据库服务
暴露应用服务 → app-service.yml

用户如何访问应用？
快速检查清单
每次写完一个文件，问自己：

✅ 这个文件引用了哪些资源？
✅ 被引用的资源是否已经定义？
✅ 标签名称是否一致？