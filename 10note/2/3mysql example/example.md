📋 Kubernetes MySQL 配置文件
文件	作用	类型
mysql-pv.yml	持久化存储卷（PV）	PersistentVolume
mysql-statefulset.yml	MySQL 应用部署	StatefulSet + Service
mysql-config.yml	配置信息	ConfigMap
mysql-secret.yml	敏感信息	Secret
🎯 核心概念：为什么需要多个文件？
1. 关注点分离
Kubernetes 最佳实践是将 不同资源类型 分开定义：

mysql-pv.yml           → 存储层（数据持久化）
mysql-statefulset.yml  → 应用层（MySQL 容器）
mysql-config.yml       → 配置层（数据库名）
mysql-secret.yml       → 安全层（密码）
2. mysql-pv.yml 分析
Yaml
apiVersion: v1
kind: PersistentVolume          # 持久化存储卷
metadata:
  name: mysql-pv
spec:
  capacity:
    storage: 10Gi              # 存储容量 10GB
  accessModes:
    - ReadWriteOnce            # 只能被一个节点读写
  hostPath:
    path: /data/mysql         # 宿主机路径

---
    
kind: PersistentVolumeClaim    # 存储声明（PVC）
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi          # 申请 10GB 存储
作用：

PV (PersistentVolume) = 实际存储资源（硬盘空间）
PVC (PersistentVolumeClaim) = 应用申请存储（像申请停车位）
类比：

PV = 停车场车位（真实存在的）
PVC = 停车申请单（我要一个车位）
MySQL = 车（使用车位）
3. mysql-statefulset.yml 分析
Yaml
apiVersion: apps/v1
kind: StatefulSet          # 有状态应用控制器
metadata:
  name: mysql
spec:
  replicas: 1
  template:
    spec:
      containers:
        - name: mysql
          image: mysql:latest
          volumeMounts:
            - name: mysql-data
              mountPath: /var/lib/mysql    # 挂载存储到容器
      volumes:
        - name: mysql-data
          persistentVolumeClaim:
            claimName: mysql-pvc           # 使用 PVC

---
            
kind: Service              # 服务暴露
metadata:
  name: mysql
spec:
  selector:
    app: mysql
  ports:
    - port: 3307
      targetPort: 3307
作用：

StatefulSet = 管理有状态应用（如数据库）
Service = 提供网络访问入口
4. mysql-config.yml 分析
Yaml
apiVersion: v1
kind: ConfigMap      # 配置管理
metadata:
  name: mysql-config
data:
  MYSQL_DATABASE: "zipdatabase"   # 数据库名称
作用：

存储非敏感配置信息
与应用代码分离
易于修改和管理
5. mysql-secret.yml 分析
Yaml
apiVersion: v1
kind: Secret          # 敏感信息管理
metadata:
  name: mysql-secret
type: Opaque
data:
  DATABASE_USERNAME: cm9vdA==              # base64 编码: root
  DATABASE_PASSWORD: aXBwYXZsb3ZhXzE5OTA=   # base64 编码: ippavlova_1990
作用：

存储敏感信息（密码、密钥）
数据经过 base64 编码
比 ConfigMap 更安全
🔗 工作流程
完整关系图
mysql-pv.yml (PV + PVC)
    ↓ 提供存储
mysql-statefulset.yml (StatefulSet + Service)
    ↓ 引用配置
mysql-config.yml (ConfigMap) + mysql-secret.yml (Secret)
    ↓ 启动容器
MySQL Pod
部署顺序
Bash
# 1. 先创建存储（PV/PVC）
kubectl apply -f mysql-pv.yml

# 2. 创建配置和密钥
kubectl apply -f mysql-config.yml
kubectl apply -f mysql-secret.yml

# 3. 部署 MySQL 应用
kubectl apply -f mysql-statefulset.yml
🆚 为什么不合并成一个文件？
可以合并，但不推荐
Yaml
# 合并后的文件（不推荐）
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mysql-pv
---
apiVersion: v1
kind: StatefulSet
metadata:
  name: mysql
---
# ... 所有资源都在一个文件
分开的优势
方面	分开文件	合并文件
维护性	✅ 每个文件职责明确	❌ 文件过大难以维护
复用性	✅ ConfigMap/Secret 可共享	❌ 难以复用
版本控制	✅ 改动清晰可见	❌ 不易追踪变更
独立性	✅ 可单独部署/更新	❌ 必须一起部署
团队协作	✅ 多人并行开发	❌ 容易冲突
📊 类比理解
就像搭建房子
mysql-pv.yml        → 地基和土地（存储）
mysql-config.yml    → 装修图纸（配置）
mysql-secret.yml    → 钥匙和密码（安全）
mysql-statefulset   → 房子本身（应用）
你需要分开准备这些材料，才能成功建成房子！

✅ 总结
为什么有"两个"（实际是多个）MySQL 配置？

这是 Kubernetes 的设计理念：

1. 关注点分离
   ├─ 存储 → PV/PVC
   ├─ 应用 → StatefulSet
   ├─ 配置 → ConfigMap
   └─ 密钥 → Secret

2. 灵活性
   ├─ 可单独更新每个组件
   ├─ 可跨应用共享配置
   └─ 易于维护和管理

3. 最佳实践
   ├─ 符合云原生标准
   ├─ 提高代码可读性
   └─ 便于团队协作
核心思想：Kubernetes 通过将不同关注点分离，实现更灵活、可维护的部署方案！ 🎯