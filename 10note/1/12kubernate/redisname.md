 完整的数据流
1. Deployment 创建
   metadata.name: redis
   
2. Deployment 创建 Pod
   Pod 名称: redis-xxxxx
   Pod 标签: app=redis
   
3. Service 选择 Pod
   metadata.name: redis
   selector.app: redis
   → 找到 Pod (app=redis)
   
4. Spring Boot 应用连接
   环境变量: REDIS_HOST="redis"
   ↓
   DNS 解析: redis → Service IP
   ↓
   Service 转发: redis:6379 → Pod:6379
   ↓
   Redis 容器接收请求

   命名空间关系图
default namespace
│
├── ConfigMap: geo-app-config
│   └── REDIS_HOST: "redis"
│
├── Service: redis
│   ├── selector.app: redis
│   └── ports: 6379
│
├── Deployment: redis
│   ├── metadata.name: redis
│   ├── selector.matchLabels.app: redis
│   └── template.labels.app: redis
│
└── Pod: redis-7d8f9c-abc123
    └── labels.app: redis



    # 查看 Deployment
kubectl get deployment redis

# 查看 Service
kubectl get service redis

# 查看 Pod
kubectl get pods -l app=redis

# 查看 ConfigMap
kubectl get configmap geo-app-config



名称	类型	用途	在配置中的位置
metadata.name: redis	Deployment 名称	标识 Deployment 资源	Deployment metadata
selector.matchLabels.app: redis	选择器	选择要管理的 Pod	Deployment spec
template.labels.app: redis	Pod 标签	Pod 的标识	Pod template metadata
containers.name: redis	容器名称	Pod 内标识容器的名称	Container spec
REDIS_HOST: "redis"	环境变量值	连接 Redis 的主机名（Service 名称）


关键关系：

REDIS_HOST: "redis" (ConfigMap)
    ↓ 解析为
Service: redis (metadata.name)
    ↓ 选择
Pod: app=redis (labels.app)
    ↓ 来自
Deployment: selector.app=redis (matchLabels)
    ↓ 创建时使用
Deployment: template.labels.app=redis (labels)
    ↓ 管理
Deployment: redis (metadata.name)