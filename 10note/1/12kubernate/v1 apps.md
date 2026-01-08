 Kubernetes API 组结构
Kubernetes 的 API 按功能分组到不同的 API 组中：

Kubernetes API
│
├── 核心组 (Core API)
│   └── apiVersion: v1
│       ├── Pod
│       ├── Service
│       ├── ConfigMap
│       ├── Secret
│       ├── PersistentVolume
│       ├── PersistentVolumeClaim
│       ├── Namespace
│       └── Node
│
├── apps 组
│   └── apiVersion: apps/v1
│       ├── Deployment
│       ├── StatefulSet
│       ├── DaemonSet
│       └── ReplicaSet
│
├── networking.k8s.io 组
│   └── apiVersion: networking.k8s.io/v1
│       ├── Ingress
│       └── NetworkPolicy
│
├── batch 组
│   └── apiVersion: batch/v1
│       ├── Job
│       └── CronJob
│
├── storage.k8s.io 组
│   └── apiVersion: storage.k8s.io/v1
│       ├── StorageClass
│       └── VolumeAttachment
│
└── rbac.authorization.k8s.io 组
    └── apiVersion: rbac.authorization.k8s.io/v1
        ├── Role
        ├── ClusterRole
        ├── RoleBinding
        └── ClusterRoleBinding


不同资源使用的 apiVersion
核心资源 ( apiVersion: v1 )
资源类型	用途
Pod	容器实例
Service	服务发现和负载均衡
ConfigMap	配置管理
Secret	敏感信息存储
PersistentVolume (PV)	存储卷
PersistentVolumeClaim (PVC)	存储申请
Namespace	命名空间
Node	工作节点


Apps 组资源 ( apiVersion: apps/v1 )
资源类型	用途
Deployment	无状态应用部署
StatefulSet	有状态应用部署
DaemonSet	守护进程集
ReplicaSet	副本集

Networking 组资源 ( apiVersion: networking.k8s.io/v1 )
资源类型	用途
Ingress	HTTP 路由规则
NetworkPolicy	网络策略


Batch 组资源 ( apiVersion: batch/v1 )
资源类型	用途
Job	一次性任务
CronJob	定时任务

早期 Kubernetes
    │
    ├─ 只有核心资源 (v1)
    │   └─ Pod, Service, ConfigMap
    │
    └─ 随着发展，新增资源被分组
        ├─ apps 组 (应用编排)
        ├─ networking 组 (网络)
        └─ batch 组 (批处理)

 功能分类
API 组	功能	资源示例
核心 (v1)	基础构建块	Pod, Service, ConfigMap
apps/v1	应用编排	Deployment, StatefulSet
networking.k8s.io/v1	网络功能	Ingress, NetworkPolicy
batch/v1	任务调度	Job, CronJob
storage.k8s.io/v1	存储管理	StorageClass
rbac.authorization.k8s.io/v1	权限控制	Role, RoleBinding


3️⃣ 版本控制
不同 API 组可以独立演进，互不影响：

核心组 (v1)
    └─ 稳定，很少变化

apps 组 (v1)
    └─ 从 v1beta1 → v1beta2 → v1

networking.k8s.io 组 (v1)
    └─ 从 v1beta1 → v1

快速参考表
常用资源	apiVersion	API 组
Pod	v1	核心
Service	v1	核心
ConfigMap	v1	核心
Secret	v1	核心
PVC	v1	核心
PV	v1	核心
Deployment	apps/v1	apps
StatefulSet	apps/v1	apps
ReplicaSet	apps/v1	apps
DaemonSet	apps/v1	apps
Ingress	networking.k8s.io/v1	networking
NetworkPolicy	networking.k8s.io/v1	networking
Job	batch/v1	batch
CronJob	batch/v1	batch
Role	rbac.authorization.k8s.io/v1	rbac
ClusterRole	rbac.authorization.k8s.io/v1	rbac
💡 如何记住？
记忆技巧
基础资源用 v1

Pod、Service、ConfigMap、Secret
这些是最基础的，在核心 API 组
应用编排用 apps/v1

Deployment、StatefulSet
管理应用的生命周期
网络功能用 networking.k8s.io/v1

Ingress、NetworkPolicy
名字就告诉你是网络相关的
任务调度用 batch/v1

Job、CronJob
批处理任务


 总结
问题	答案
为什么 Service 用 v1 ？	Service 属于核心 API 组
为什么 Deployment 用 apps/v1 ？	Deployment 属于 apps API 组
核心资源有哪些？	Pod, Service, ConfigMap, Secret, PVC, PV
apps 组资源有哪些？	Deployment, StatefulSet, ReplicaSet, DaemonSet
如何查询资源的 apiVersion？	kubectl api-resources 或 kubectl explain