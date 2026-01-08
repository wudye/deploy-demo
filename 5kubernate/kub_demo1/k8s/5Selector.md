1. 基本概念

    selector:
        matchLabels:
            app: sample-spring-boot-on-kubernetes
   目的：讓 Deployment 找到並管理它應該控制的 Pod

    類比：
    
    selector 就像管理員的工牌識別器
    它確保 Deployment 只管理正確的 Pod
    避免管理錯誤或無關的 Pod
2. 工作原理
   標籤匹配機制
 Deployment 中的選擇器
   spec:
   selector:
   matchLabels:
   app: sample-spring-boot-on-kubernetes
   template:
   metadata:
   labels:
   app: sample-spring-boot-on-kubernetes  # ← 必須匹配！
   匹配規則：

Deployment 的 selector.matchLabels 必須與 Pod 模板的 labels 完全匹配
這是強制要求，如果不匹配會報  
3. 完整的標籤流程
   從 Deployment 到 Pod
# deployment.yaml
    apiVersion: apps/v1
    kind: Deployment
    metadata:
        name: sample-spring-boot-on-kubernetes
        labels:
            app: sample-spring-boot-on-kubernetes
    spec:
        replicas: 3
        selector:
            matchLabels:
                app: sample-spring-boot-on-kubernetes  # ← 選擇器：找這個標籤的 Pod
        template:
            metadata:
                labels:
                    app: sample-spring-boot-on-kubernetes  # ← Pod 標籤：必須與上面匹配
            spec:
                 containers:
                    - name: spring-boot-app
                    image: sample-spring-boot-on-kubernetes:latest
                    ports:
                        - containerPort: 8080
    # service.yaml
    apiVersion: v1
    kind: Service
    metadata:
        name: sample-spring-boot-service
    spec:
        type: NodePort
        selector:
            app: sample-spring-boot-on-kubernetes  # ← 選擇相同標籤的 Pod（無需 matchLabels）
        ports:
            - port: 80
            targetPort: 8080
            nodePort: 30080

Deployment 創建 3 個 Pod：
┌─────────────────────────────────────────────────────┐
│ Pod: webapp-deployment-abc12345                      │
│ Labels: app=sample-spring-boot-on-kubernetes      │
│         tier=frontend                              │
│         version=v1.0                               │
├─────────────────────────────────────────────────────┤
│ Pod: webapp-deployment-def67890                      │
│ Labels: app=sample-spring-boot-on-kubernetes      │
│         tier=frontend                              │
│         version=v1.0                               │
├─────────────────────────────────────────────────────┤
│ Pod: webapp-deployment-ghi09876                      │
│ Labels: app=sample-spring-boot-on-kubernetes      │
│         tier=frontend                              │
│         version=v1.0                               │
└─────────────────────────────────────────────────────

元件      選擇器         目標
Deployment selector.matchLabels 管理 Pod
Service     selector    路由到 Pod
Pod         labels      被識別的標籤
┌─────────────────────────────────────────────────────────────┐
│                        Deployment                           │
│  selector.matchLabels: app=sample-spring-boot-on-kubernetes │
└─────────────────────┬───────────────────────────────────────┘
│ 管理
▼
┌─────────────────────────────────┐
│  Pod (labels: app=sample-...)   │ × 3 個副本
└─────────────────────────────────┘
▲
│ 路由流量
┌─────────────────────┴───────────────────────────────────────┐
│                         Service                              │
│     selector: app=sample-spring-boot-on-kubernetes          │
└─────────────────────────────────────────────────────────────┘

replicas: 3 是指 3 個獨立的 Pod，不是一個 Pod 裡有 3 個 app
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   Pod 1     │  │   Pod 2     │  │   Pod 3     │
│ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │
│ │  App    │ │  │ │  App    │ │  │ │  App    │ │
│ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │
└─────────────┘  └─────────────┘  └─────────────┘
↑                ↑                ↑
└────────────────┴────────────────┘
│
Service 負載均衡
高可用性機制
情境  結果
Pod 1 掛掉    Pod 2、Pod 3 繼續運作，Service 自動將流量導向存活的 Pod
Pod 1 掛掉後   Deployment 會自動創建新的 Pod 來維持 3 個副本
好處：
高可用性：一個掛了，其他還能服務
負載均衡：流量分散到多個 Pod
自動修復：Deployment 會自動補足掛掉的 Pod
結論：是的，如果一個 Pod 掛掉，其他 Pod 會繼續工作，而且 Kubernetes 會自動創建新的 Pod 來取代。