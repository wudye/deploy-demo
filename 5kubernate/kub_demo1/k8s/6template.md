0. Pod 啟動
   你的模板各部分意義
   區塊                   說明
   metadata.labels      Pod 標籤，讓 Selector 識別
   metadata.annotations    Prometheus 抓取設定
   spec.containers      容器配置
   ports            容器暴露的端口（8080=應用, 8081=管理）
   env              環境變數（從 ConfigMap/Secret 讀取）
   readinessProbe   就緒探針：Pod 準備好接收流量了嗎？
   livenessProbe    存活探針：Pod 還活著嗎？
   resources        CPU/記憶體限制
   │
   ├── 讀取 env（從 ConfigMap/Secret）
   │       ├── MONGO_DATABASE
   │       ├── MONGO_USERNAME
   │       └── MONGO_PASSWORD
   │
   ├── 啟動容器，監聽 8080, 8081
   │
   ├── 等待 30 秒後開始 readinessProbe
   │       └── GET /actuator/health:8081
   │
   ├── 就緒後，Prometheus 開始抓取
   │       └── GET /actuator/prometheus:8080
   │
   └── 持續 livenessProbe 檢查健康
1. template 是 Pod 模板，定义了 Deployment 创建的 Pod 的规格
   template 就是一個完整的 Pod 定義，可以放任何 Pod 支援的欄位。
   作用和逻辑
   Pod 模板是"蓝图"：

    Deployment 根据 template 创建 Pod
    当需要扩容、缩容或替换 Pod 时，都使用这个模板
    确保所有 Pod 都有一致的配置
   template:
   metadata:          # ← Pod 的元資料
   labels: ...      # 標籤（必須）
   annotations: ... # 註解（可選）
   spec:              # ← Pod 的規格
   containers: ...  # 容器定義（必須）
   volumes: ...     # 掛載卷
   initContainers:  # 初始化容器
   serviceAccountName: ...
   nodeSelector: ...
   # ... 更多

2. Metadata 部分 
 Labels (标签)#
   目的：
    
    标识 Pod：让 Service 和其他资源找到这些 Pod
    匹配 Selector：与 Deployment 的 spec.selector.matchLabels 对应
    组织管理：用于查询、筛选、监控
   Annotations (注解)
   annotations:
   prometheus.io/path: /actuator/prometheus    # 监控端点路径
   prometheus.io/scrape: "true"               # 启用抓取
   prometheus.io/port: "8080"                 # 监控端口
   目的：

    Prometheus 监控：告诉 Prometheus 如何抓取指标
    非标识信息：存储元数据，不影响调度
    工具集成：各种 Kubernetes 工具读取注解
3. template:
   metadata:           # Pod 元数据
   labels:          # Pod 标签（用于选择）
   annotations:     # Pod 注解（用于配置）
   spec:              # Pod 规格（容器、卷等）
   template:
   metadata:
   labels: {}
   annotations: {}
   spec:
   # 容器相關
   containers: []        # 主容器
   initContainers: []    # 初始化容器

   # 存儲相關
   volumes: []           # 掛載卷

   # 調度相關
   nodeSelector: {}      # 節點選擇
   affinity: {}          # 親和性
   tolerations: []       # 容忍度

   # 安全相關
   serviceAccountName: ""
   securityContext: {}

   # 其他
   restartPolicy: Always
   terminationGracePeriodSeconds: 30
   dnsPolicy: ClusterFirst
   hostNetwork: false


4. 除了 Prometheus，Kubernetes 生态系统中有很多常用的注解。以下是主要的注解类别：annotations:
   服务网格 (Service Mesh)
   Istio 注解： 
   sidecar.istio.io/inject: "true"                    # 自动注入 sidecar
   traffic.sidecar.istio.io/includeOutboundPorts: "9090"  # 出口流量拦截
   traffic.sidecar.istio.io/excludeInboundPorts: "8080"   # 排除端口
   proxy.istio.io/config: '{"holdApplicationUntilProxyStarts": true}' # 代理配置
   Linkerd 注解：
   annotations:
   linkerd.io/inject: enabled # 注注 Linkerd proxy
   config.linkerd.io/skip-outbound-ports: "6379"      # 跳过 Redis 端口
   负载均衡器 (Load Balancer)
   AWS Load Balancer：annotations:
   service.beta.kubernetes.io/aws-load-balancer-type: "nlb"     # NLB 类型
   service.beta.kubernetes.io/aws-load-balancer-backend-protocol: "tcp"
   service.beta.kubernetes.io/aws-load-balancer-ssl-ports: "443"
   service.beta.kubernetes.io/aws-load-balancer-cross-zone-load-balancing-enabled: "true"
   NGINX Ingress：annotations:
   nginx.ingress.kubernetes.io/rewrite-target: /$2 # URL 重写
   nginx.ingress.kubernetes.io/ssl-redirect: "true"         # HTTPS 重定向
   nginx.ingress.kubernetes.io/rate-limit: "100"             # 速率限制
   nginx.ingress.kubernetes.io/cors-allow-origin: "*"
   自动伸缩 (Autoscaling)
   HPA 相关annotations:
   autoscaling.kubernetes.io/scale-target: "deployment/my-app"
   metric-config.object.type: "kubernetes.io/custom-metrics"
   安全 (Security)
   Pod Security：annotations:
   seccomp.security.alpha.kubernetes.io/pod: "runtime/default"  # 安全计算配置
   apparmor.security.beta.kubernetes.io/pod: "runtime/default"   # AppArmor 配置
   container.security.alpha.kubernetes.io/readonly-rootfs: "true" # 只读根文件系统
   网络策略 (Networking)annotations:
   kubernetes.io/ingress.class: "nginx"              # Ingress 控制器类型
   acme.cert-manager.io/http01-edit-in-place: "true" # cert-manager 配置
   监控和日志 (Monitoring & Logging)
   Grafana/ Jaeger：annotations:
   sidecar.jaegertracing.io/inject: "true"           # Jaeger 追踪
   grafana.io/dashboard: "dashboard-name"           # Grafana 仪表板
   fluentd.org/include: "true"                      # Fluentd 日志收集
   存储和卷 (Storage)annotations:
   volume.beta.kubernetes.io/storage-class: "fast-ssd"
   pv.kubernetes.io/bound-by-controller: "yes"
   volume.kubernetes.io/storage-provisioner: "kubernetes.io/aws-ebs"
   调度和资源 (Scheduling & Resources)annotations:
   scheduler.alpha.kubernetes.io/node-selector: "disktype=ssd"
   deployment.kubernetes.io/revision: "1"           # 部署版本
   kubernetes.io/changed-by: "admin@example.com"    # 变更记录
   CI/CD 和部署 (CI/CD & Deployment)
   ArgoCD：annotations:
   argocd.argoproj.io/sync-options: "CreateNamespace=true"
   argocd.argoproj.io/compare-options: "IgnoreExtraneous"
   Flux：annotations:
   kustomize.toolkit.fluxcd.io/checksum: "abc123"
   flux.weave.works/automated: "true"
   应用和元数据 (Application & Metadata)annotations:
   app.kubernetes.io/name: "my-app"
   app.kubernetes.io/version: "1.0.0"
   app.kubernetes.io/component: "frontend"
   app.kubernetes.io/managed-by: "helm"
   kubernetes.io/description: "Production web application"
   contact.email: "ops@example.com"
   调试和故障排除 (Debugging)annotations:
   kubectl.kubernetes.io/last-applied-configuration: '{"spec":{"replicas":3}}'
   deployment.kubernetes.io/desired-replicas: "3"
   deployment.kubernetes.io/max-replicas: "10"metadata:
5. 实际使用示例
   annotations:
   # 监控
   prometheus.io/scrape: "true"
   prometheus.io/port: "8080"
   prometheus.io/path: "/actuator/prometheus"

   # 服务网格
   sidecar.istio.io/inject: "true"
   linkerd.io/inject: "enabled"

   # 负载均衡
   service.beta.kubernetes.io/aws-load-balancer-type: "nlb"

   # 应用信息
   app.kubernetes.io/name: "user-service"
   app.kubernetes.io/version: "2.1.0"
   app.kubernetes.io/managed-by: "argocd"

   # 安全
   container.security.alpha.kubernetes.io/readonly-rootfs: "true"

   # CI/CD
   argocd.argoproj.io/sync-options: "CreateNamespace=true"

5. 














