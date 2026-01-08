Kind: Ingress 详解
📖 基本定义
Ingress 是 Kubernetes 的一种资源类型，用于管理外部访问集群内服务的 HTTP/HTTPS 路由规则。

简单理解：Ingress 是 Kubernetes 的"网关"或"反向代理"，负责将外部流量路由到集群内的 Service。

🏗️ 架构对比
没有 Ingress（直接暴露 Service）
外部用户
    │
    ▼
┌─────────────────────────────────────────────┐
│  Kubernetes Cluster                          │
│                                              │
│  ┌──────────┐      ┌──────────┐              │
│  │ Service 1│ ◄───┤ LoadBalancer │ ◄─── NodePort
│  │ (App A)  │      │ (每个服务) │     (每个服务)             │
│  └──────────┘      └──────────┘              │
│                                              │
│  ┌──────────┐                                │
│  │ Service 2│ ◄─── LoadBalancer (IP: 10.0.0.2)
│  │ (App B)  │                                │
│  └──────────┘                                │
│                                              │
│  ⚠️ 问题：每个服务都需要一个公网 IP          │
└─────────────────────────────────────────────┘
有 Ingress（统一入口）
外部用户
    │
    ▼ https://example.com
┌─────────────────────────────────────────────┐
│  Kubernetes Cluster                          │
│                                              │
│  ┌─────────────────────────────────────────┐│
│  │         Ingress Controller (Nginx)       ││
│  │    ┌─────────────────────────────────┐   ││
│  │    │  /api/users  →  service-users    │   ││
│  │    │  /api/orders →  service-orders   │   ││
│  │    │  /          →  service-frontend  │   ││
│  │    └─────────────────────────────────┘   ││
│  └─────────────────────────────────────────┘│
│           │           │           │          │
│           ▼           ▼           ▼          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │Service A │  │Service B │  │Service C │   │
│  │(users)   │  │(orders)  │  │(frontend)│   │
│  └──────────┘  └──────────┘  └──────────┘   │
│                                              │
│  ✅ 好处：只需一个公网 IP                    │
└─────────────────────────────────────────────┘
📋 Ingress 配置示例
基础配置
Yaml
插入
复制
新建文件
保存
应用代码
apiVersion: networking.k8s.io/v1
kind: Ingress                    ← 资源类型
metadata:
  name: geodistance-ingress      ← Ingress 名称
  namespace: default
spec:
  rules:
  - host: geodistance.example.com    ← 域名（可选）
    http:
      paths:
      - path: /                       ← URL 路径
        pathType: Prefix              ← 匹配类型
        backend:
          service:
            name: geodistance         ← 目标 Service 名称
            port:
              number: 1441            ← 目标 Service 端口
完整配置（包含 TLS）
Yaml
插入
复制
新建文件
保存
应用代码
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: geodistance-ingress
  namespace: default
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  ingressClassName: nginx                  ← 使用 Nginx Ingress Controller
  tls:                                     ← HTTPS 配置
  - hosts:
    - geodistance.example.com
    secretName: geodistance-tls           ← TLS 证书 Secret
  rules:
  - host: geodistance.example.com
    http:
      paths:
      - path: /api/v1/geo
        pathType: Prefix
        backend:
          service:
            name: geodistance
            port:
              number: 1441
      - path: /api/v1/health
        pathType: Exact
        backend:
          service:
            name: geodistance
            port:
              number: 1441
🔍 关键字段详解
1. metadata
Yaml
插入
复制
新建文件
保存
应用代码
metadata:
  name: geodistance-ingress      ← Ingress 的唯一名称
  namespace: default             ← 所在命名空间
  annotations:                   ← 自定义配置（Ingress Controller 特定）
    nginx.ingress.kubernetes.io/rewrite-target: /
常用 Annotations（Nginx Ingress）：

Annotation	作用
nginx.ingress.kubernetes.io/rewrite-target: /	重写路径
nginx.ingress.kubernetes.io/ssl-redirect: "true"	自动重定向到 HTTPS
nginx.ingress.kubernetes.io/cors-enable: "true"	启用 CORS
nginx.ingress.kubernetes.io/rate-limit: "100"	限流（每秒 100 请求）
2. spec.ingressClassName
Yaml
插入
复制
新建文件
保存
应用代码
spec:
  ingressClassName: nginx        ← 指定使用的 Ingress Controller
常见的 Ingress Controller：

nginx - Nginx Ingress Controller（最常用）
traefik - Traefik
haproxy - HAProxy
gce - Google Cloud Load Balancer
3. spec.rules
Yaml
插入
复制
新建文件
保存
应用代码
rules:
  - host: geodistance.example.com    ← 域名匹配
    http:
      paths:
      - path: /api/geo               ← 路径匹配
        pathType: Prefix             ← 匹配类型
        backend:
          service:
            name: geodistance       ← 转发到的 Service
            port:
              number: 1441
pathType（路径匹配类型）：
类型	说明	示例
Exact	精确匹配	/api/geo 只匹配 /api/geo ，不匹配 /api/geo/distance
Prefix	前缀匹配	/api/geo 匹配 /api/geo 、 /api/geo/distance 、 /api/geo/nearby
ImplementationSpecific	由 Ingress Controller 决定	默认行为因 Controller 而异
4. spec.tls
Yaml
插入
复制
新建文件
保存
应用代码
spec:
  tls:
  - hosts:
    - geodistance.example.com        ← 此域名使用 HTTPS
    secretName: geodistance-tls       ← 存储 TLS 证书的 Secret
创建 TLS Secret：

Bash
插入
复制
新建文件
保存
运行
应用代码
kubectl create secret tls geodistance-tls \
  --cert=tls.crt \
  --key=tls.key \
  -n default
🔄 路由示例场景
场景：一个域名，多个路径
Yaml
插入
复制
新建文件
保存
应用代码
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: multi-service-ingress
spec:
  rules:
  - host: app.example.com
    http:
      paths:
      # /api/geo/* → geodistance 服务
      - path: /api/geo
        pathType: Prefix
        backend:
          service:
            name: geodistance
            port:
              number: 1441
      
      # /api/users/* → user-service
      - path: /api/users
        pathType: Prefix
        backend:
          service:
            name: user-service
            port:
              number: 8080
      
      # / → 前端服务
      - path: /
        pathType: Prefix
        backend:
          service:
            name: frontend
            port:
              number: 80
请求流程：

外部请求	路由到
app.example.com/api/geo/distance	geodistance:1441
app.example.com/api/users/profile	user-service:8080
app.example.com/index.html	frontend:80
场景：多个域名
Yaml
插入
复制
新建文件
保存
应用代码
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: multi-host-ingress
spec:
  rules:
  # 域名 1
  - host: api.geodistance.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: geodistance-api
            port:
              number: 1441
  
  # 域名 2
  - host: admin.geodistance.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: geodistance-admin
            port:
              number: 1442
🛠️ 常用命令
创建 Ingress
Bash
插入
复制
新建文件
保存
运行
应用代码
kubectl apply -f ingress.yml
查看 Ingress
Bash
插入
复制
新建文件
保存
运行
应用代码
# 列出所有 Ingress
kubectl get ingress -A

# 查看详细信息
kubectl describe ingress geodistance-ingress -n default

# 查看 Ingress YAML
kubectl get ingress geodistance-ingress -o yaml -n default
删除 Ingress
Bash
插入
复制
新建文件
保存
运行
应用代码
kubectl delete ingress geodistance-ingress -n default
⚠️ 重要前提
Ingress 需要部署 Ingress Controller 才能工作！

Ingress 只是定义路由规则，实际的路由执行由 Ingress Controller 完成。

安装 Nginx Ingress Controller
Bash
插入
复制
新建文件
保存
运行
应用代码
# 使用 Helm 安装
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx --create-namespace
📊 对比总结
特性	Service (NodePort/LoadBalancer)	Ingress
协议	支持 TCP/UDP	支持 HTTP/HTTPS
路由能力	四层（IP + 端口）	七层（HTTP 路径、域名）
公网 IP	每个 Service 需要	共享一个 IP
SSL/TLS	需要在每个 Service 配置	统一在 Ingress 配置
适用场景	非HTTP服务（数据库、缓存）	HTTP/HTTPS 服务（Web API）
💡 最佳实践
生产环境

✅ 使用 Ingress 暴露 HTTP 服务
✅ 启用 HTTPS（TLS）
✅ 配置限流和访问控制
✅ 使用健康检查
开发环境

可以使用 NodePort 或 Port Forwarding
简化部署流程
路径设计

使用有意义的路径前缀（如 /api/v1/geo ）
避免路径冲突
Ingress 是 Kubernetes 中管理 HTTP 流量的核心组件，它提供了灵活的路由规则和统一的流量入口，是生产环境中暴露 Web 应用的标准方式。

12 个文件
检查点 8