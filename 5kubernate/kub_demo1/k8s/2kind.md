# Kubernetes 常見的 kind 值及其用途
1. 工作負載 (Workloads)
    kind: Pod              # 最小部署單位
    kind: Deployment       # 無狀態應用部署，支援滾動更新
    kind: ReplicaSet       # 確保指定數量的 Pod 運行
    kind: StatefulSet      # 有狀態應用（如資料庫）
    kind: DaemonSet        # 每個節點運行一個 Pod
    kind: Job              # 一次性任務
    kind: CronJob          # 定時任務
2. 服務與網路 (Service & Networking)
   kind: Service          # 暴露應用的網路端點
   kind: Ingress          # HTTP/HTTPS 路由
   kind: NetworkPolicy    # 網路訪問控制
   kind: Endpoints        # 服務端點
3. 配置與存儲 (Config & Storage)
   kind: ConfigMap        # 非敏感配置
   kind: Secret           # 敏感數據（密碼、金鑰）
   kind: PersistentVolume         # 持久化存儲
   kind: PersistentVolumeClaim    # 存儲請求
   kind: StorageClass     # 存儲類型定義
4. 權限與安全 (RBAC & Security)
   kind: ServiceAccount   # Pod 身份
   kind: Role             # 命名空間內權限
   kind: ClusterRole      # 叢集級別權限
   kind: RoleBinding      # 綁定 Role 到用戶/服務帳戶
   kind: ClusterRoleBinding  # 綁定 ClusterRole
5. 叢集管理 (Cluster)
   kind: ServiceAccount   # Pod 身份
   kind: Role             # 命名空間內權限
   kind: ClusterRole      # 叢集級別權限
   kind: RoleBinding      # 綁定 Role 到用戶/服務帳戶
   kind: ClusterRoleBinding  # 綁定 ClusterRole
6. 其他資源 (Others)
   kind: Namespace        # 資源隔離單位
   kind: HorizontalPodAutoscaler  # Pod 水平自動擴展
   kind: CustomResourceDefinition  # 自定義資源
這些是 Kubernetes 中常見的 kind 值及其用途。根據應用需求，可能還會使用其他自定義的 kind。


7. 無狀態應用（Stateless Application）是指不保存任何客戶端會話數據的應用，每個請求都是獨立的、自包含的。
    無狀態 vs 有狀態對比
   特徵	無狀態應用	有狀態應用
   數據存儲	外部（數據庫、緩存）	內部（本地文件、內存）
   會話管理	JWT Token、Redis Session	內存會話、文件會話
   擴展性	水平擴展容易	擴展複雜
   故障恢復	任意實例替換	需要狀態恢復
   例子	Web API、REST 服務	數據庫、消息隊列
   8. 根據你的應用需求選擇 kind：
      需求                            kind            說明
      無狀態應用（Web API、微服務）    Deployment ✅ 你的 Spring Boot 應用適用
      有狀態應用（資料庫、快取）        StatefulSet     需要穩定的網路標識和持久存儲
      每個節點運行一個（日誌、監控）       DaemonSet   如 Fluentd、Prometheus Node Exporter
      一次性任務                     Job             批次處理、資料遷移
      定時任務                      CronJob         定期備份、清理任務
      暴露服務                      Service         網路端點