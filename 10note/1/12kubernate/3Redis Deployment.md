Redis Deployment 详解
📖 文件概述
这是一个 Kubernetes Deployment 配置文件，用于部署 Redis 数据库应用。它定义了如何运行 Redis 容器，包括副本数量、存储挂载、健康检查等关键配置。

🏗️ 整体架构
┌─────────────────────────────────────────────────────────┐
│                   Kubernetes Cluster                     │
│                                                          │
│  ┌─────────────────────────────────────────────────────┐ │
│  │          Deployment: redis                         │ │
│  │                                                      │ │
│  │  spec.replicas: 1  →  运行 1 个 Pod                 │ │
│  │                                                      │ │
│  │  ┌───────────────────────────────────────────────┐  │ │
│  │  │              Pod (redis-xxxxx)               │  │ │
│  │  │                                               │  │ │
│  │  │  ┌─────────────────────────────────────────┐  │  │ │
│  │  │  │  Redis Container                       │  │  │ │
│  │  │  │  - Image: redis:latest                │  │  │ │
│  │  │  │  - Port: 6379                         │  │  │ │
│  │  │  │  - Data: /data (持久化存储)            │  │  │ │
│  │  │  │  - Health Checks                       │  │  │ │
│  │  │  └─────────────────────────────────────────┘  │  │ │
│  │  └───────────────────────────────────────────────┘  │ │
│  └─────────────────────────────────────────────────────┘ │
│                          │                               │
│                          ▼                               │
│  ┌─────────────────────────────────────────────────────┐ │
│  │          PVC: redis-data (1Gi)                      │ │
│  └─────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘

为什么用 Deployment？

✅ 自动管理 Pod 副本数量
✅ 支持滚动更新和回滚
✅ 自愈能力（Pod 崩溃自动重启）
✅ 适合无状态应用和简单的有状态应用

metadata:
  name: redis
  namespace: default
说明：

name: redis - Deployment 的唯一名称
namespace: default - 部署在默认命名空间
关联关系：

生成的 Pod 名称会包含 Deployment 名称，如 redis-7d8f9c-abc123
其他资源（如 Service）通过标签 app: redis 选择这些 Pod

selector 必须匹配 template.metadata.labels
template 定义了如何创建 Pod
每次需要创建 Pod 时，Deployment 都会使用这个模板

template.metadata.labels	Pod 的标签，必须匹配 selector
template.spec	Pod 的实际配置（容器、存储、探针等）

name: redis - 容器名称（Pod 内唯一）



持久化方式	优点	缺点
AOF (当前配置)	数据安全性高，最多丢失 1 秒数据	文件较大，性能稍低
RDB	文件小，恢复快	可能丢失较多数据

Redis 数据目录：

/data/
├── appendonly.aof    ← AOF 持久化文件（因为启用了 --appendonly yes）
└── dump.rdb         ← RDB 快照文件（如果启用了 RDB）
持久化流程：

Redis 写操作
    ↓
写入 /data/appendonly.aof
    ↓
通过 volumeMounts 映射到 Pod 存储卷
    ↓
通过 volumes 映射到 PVC (redis-data)
    ↓
存储到底层 PV（物理磁盘）
    ↓
Pod 重启后数据仍然保留 ✅

数据持久化验证：

Bash
插入
复制
新建文件
保存
运行
应用代码
# 1. 写入数据
kubectl exec -it redis-xxx -- redis-cli SET mykey "hello"

# 2. 删除 Pod
kubectl delete pod redis-xxx

# 3. Deployment 自动创建新 Pod
kubectl get pods

# 4. 验证数据仍然存在
kubectl exec -it redis-new -- redis-cli GET mykey



# 查看 Deployment 状态
kubectl get deployment redis

# 查看 Pod 状态
kubectl get pods -l app=redis

# 查看详细信息
kubectl describe deployment redis



组件	作用
Deployment	管理 Pod 副本，支持滚动更新和自愈
replicas: 1	运行 1 个 Redis 实例
selector	选择带有 app: redis 标签的 Pod
template	定义 Pod 的配置模板
--appendonly yes	启用 AOF 持久化，确保数据不丢失
volumeMounts + volumes + PVC	实现数据持久化，Pod 重启后数据保留
readinessProbe	检查容器是否就绪，避免向未就绪的 Pod 发送流量
livenessProbe	检查容器是否存活，卡死时自动重启