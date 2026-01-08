# 查看 Pod 存储使用
kubectl exec redis -- df -h /data

# 监控 PVC 使用情况
kubectl top pod redis


spec 详解
📖 基本定义
spec 是 Kubernetes 资源配置中的规格说明部分，全称是 "Specification"。

简单理解： spec 就像产品说明书或配置清单，用来定义你想要创建什么样的资源以及它的工作方式。

🏗️ Kubernetes 资源结构

部分	说明	是否可编辑	示例内容
metadata	元数据，描述"这是谁"	✅ 可编辑	name , namespace , labels
spec	规格，定义"我想要什么样的"	✅ 可编辑	replicas , containers , resources
status	状态，显示"当前是什么"	❌ 不可编辑（系统自动生成）	phase , conditions , availableReplicas


📋 常见的 spec 字段
通用字段
字段	资源类型	说明
spec.replicas	Deployment, StatefulSet	副本数量
spec.selector	Deployment, Service, Ingress	选择器
spec.template	Deployment, StatefulSet	Pod 模板
容器相关
字段	资源类型	说明
spec.containers	Pod, Deployment	容器列表
spec.initContainers	Pod	初始化容器
spec.volumes	Pod	存储卷
spec.restartPolicy	Pod	重启策略
网络相关
字段	资源类型	说明
spec.type	Service	服务类型
spec.ports	Service	端口配置
spec.rules	Ingress	路由规则
存储相关
字段	资源类型	说明
spec.accessModes	PVC	访问模式
spec.resources	PVC, Pod	资源请求/限制
spec.storageClassName	PVC	存储类
💡 理解 spec 的关键要点
1️⃣ spec 是你告诉 Kubernetes "我想要什么"