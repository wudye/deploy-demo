1. spec 是 Specification（規格/規範） 的縮寫，用來定義資源的期望狀態。
   apiVersion: apps/v1
   kind: Deployment
   metadata:        # 資源的元數據（名稱、標籤等）
   spec:            # ← 資源的規格定義（你想要什麼）
   selector:      # Deployment 的規格之一
   template:      # Deployment 的規格之一
   replicas:      # Deployment 的規格之一
2. spec 的角色
   層級   說明
   metadata 描述資源是什麼（名稱、標籤）
   spec  描述資源應該如何運作（期望狀態）
   status    描述資源實際狀態（由 Kubernetes 自動填寫
3. spec:
   type: NodePort
   type          說明                      存取方式
   ClusterIP     預設，只在叢集內部可存取    service-name:port
   NodePort      透過節點 IP + 固定端口存取    <NodeIP>:30080
   LoadBalancer  雲端負載均衡器             外部 IP