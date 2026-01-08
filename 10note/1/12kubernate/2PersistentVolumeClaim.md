PersistentVolumeClaim (PVC) 是 Kubernetes 中用于申请存储资源的声明。你可以把它理解为"存储订单"或"存储申请表"。

 核心概念对比
┌─────────────────────────────────────────────────────────┐
│                    Kubernetes 存储体系                    │
│                                                          │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  应用 (Pod)                                         │ │
│  │  "我需要存储来保存数据"                              │ │
│  └─────────────────────┬───────────────────────────────┘ │
│                        │                                  │
│                        ▼                                  │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  PersistentVolumeClaim (PVC) ← 本文件               │ │
│  │  "我申请 1GB 的存储空间"                             │ │
│  │  - 名称: redis-data                                 │ │
│  │  - 大小: 1Gi                                        │ │
│  │  - 访问模式: ReadWriteOnce                          │ │
│  └─────────────────────┬───────────────────────────────┘ │
│                        │                                  │
│                        ▼                                  │
│  ┌─────────────────────────────────────────────────────┐ │
│  │  PersistentVolume (PV)                              │ │
│  │  "我提供实际的存储空间（磁盘、云盘等）"               │ │
│  └─────────────────────────────────────────────────────┘ │
│                                                          │
└─────────────────────────────────────────────────────────┘

三种访问模式：
访问模式	缩写	说明	支持的存储类型
ReadWriteOnce	RWO	单节点读写（一个 Pod 挂载）	大部分存储类型
ReadOnlyMany	ROX	多节点只读（多个 Pod 只读）	NFS, CephFS
ReadWriteMany	RWX	多节点读写（多个 Pod 读写）	NFS, CephFS, GlusterFS


存储单位：
单位	大小	说明
Ti	1024 Gi	太字节
Gi	1024 Mi	吉字节
Mi	1024 Ki	兆字节
Ki	1024 bytes	千字节


数据流程：

Redis 容器
    │
    │ 写入数据到 /data/dump.rdb
    │
    ▼
volumeMount: redis-storage (挂载到 /data)
    │
    ▼
volume: redis-storage (Pod 存储卷)
    │
    ▼
PVC: redis-data (1Gi)
    │
    ▼
PV: 实际存储（如 AWS EBS、本地磁盘等）
    │
    ▼
    物理存储


Redis 存储需求评估：

场景	建议存储	说明
开发/测试	1-2 Gi	数据量小
小型生产	5-10 Gi	中等数据量
大型生产	50+ Gi	大数据量，需监控


何时使用其他模式：

场景	推荐模式	示例
单实例数据库	ReadWriteOnce	Redis, PostgreSQL
共享静态文件	ReadOnlyMany	多个 Pod 读取同一配置
共享文件系统	ReadWriteMany	多个 Pod 写入同一目录



📊 PVC 生命周期
1. Pending (等待绑定)
   │
   │  PVC 创建完成，等待 PV 绑定
   │
   ▼
2. Bound (已绑定)
   │
   │  PVC 与 PV 绑定成功，Pod 可以使用
   │
   ▼
3. In Use (使用中)
   │
   │  Pod 挂载 PVC，正在读写数据
   │
   ▼
4. Released (已释放)
   │
   │  Pod 删除，PVC 解除挂载，但数据保留
   │
   ▼
5. Terminating (正在删除)
   │
   │  删除 PVC（可选保留数据）
   │
   ▼
6. Deleted (已删除)


# 列出所有 PVC
kubectl get pvc -A

# 查看详细信息
kubectl describe pvc redis-data -n default

# 查看 YAML
kubectl get pvc redis-data -o yaml -n default

# 查看绑定状态
kubectl get pvc, pv