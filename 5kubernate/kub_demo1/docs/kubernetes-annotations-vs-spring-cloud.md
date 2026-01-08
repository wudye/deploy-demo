# Kubernetes 注解替代 Spring Cloud 配置指南

## 概述

Kubernetes 注解可以替代传统 Spring Cloud 项目中的大部分配置，提供更原生、更强大的云原生解决方案。
Kubernetes 注解替代 Spring Cloud 的各种配置，包括：

服务发现、负载均衡、配置管理
API 网关、断路器、监控追踪
安全认证、消息队列、CI/CD
性能优化、资源管理
## 1. 服务发现 (Service Discovery)

### Spring Cloud 方式
```yaml
# application.yml
spring:
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: user-service
        health-check-path: /actuator/health
        health-check-interval: 10s
        tags: production,rest-api
```

### Kubernetes 方式
```yaml
apiVersion: v1
kind: Service
metadata:
  name: user-service
  labels:
    app: user-service
    version: v1.0.0
    environment: production
  annotations:
    prometheus.io/scrape: "true"           # 自动发现监控端点
spec:
  selector:
    app: user-service                     # 自动匹配 Pod
  ports:
    - port: 8080
      name: http
    - port: 8081
      name: management
  type: ClusterIP
```

**优势**：
- 无需额外注册中心组件
- 原生 DNS 解析
- 自动健康检查

## 2. 负载均衡 (Load Balancing)

### Spring Cloud 方式
```yaml
# Ribbon 配置
user-service:
  ribbon:
    listOfServers: server1:8080,server2:8080,server3:8080
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RoundRobinRule
    ConnectTimeout: 5000
    ReadTimeout: 5000
    MaxTotalHttpConnections: 200
    MaxConnectionsPerHost: 50
```

### Kubernetes 方式
```yaml
apiVersion: v1
kind: Service
metadata:
  name: user-service
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"     # AWS NLB
    service.beta.kubernetes.io/aws-load-balancer-cross-zone-load-balancing-enabled: "true"
    service.beta.kubernetes.io/aws-load-balancer-ssl-ports: "443"
    service.beta.kubernetes.io/aws-load-balancer-backend-protocol: "tcp"
    # 或使用 NGINX Ingress
    nginx.ingress.kubernetes.io/load-balance: "round_robin"
    nginx.ingress.kubernetes.io/upstream-hash-by: "$remote_addr"
spec:
  selector:
    app: user-service
  type: LoadBalancer                      # 云原生负载均衡
  ports:
    - port: 80
      targetPort: 8080
      name: http
    - port: 443
      targetPort: 8443
      name: https
```

**优势**：
- 云厂商原生集成
- 自动故障转移
- 支持多种算法

## 3. 配置管理 (Configuration Management)

### Spring Cloud Config 方式
```yaml
# bootstrap.yml
spring:
  cloud:
    config:
      uri: http://config-server:8888
      name: user-service
      profile: prod
      label: master
      fail-fast: true
      retry:
        initial-interval: 1000
        max-attempts: 6
        max-interval: 2000
```

### Kubernetes ConfigMap 方式
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: user-service-config
  labels:
    app: user-service
  annotations:
    configmanagement.gke.io/managed: "enabled"    # GKE 配置管理
data:
  application.yml: |
    spring:
      datasource:
        url: jdbc:postgresql://postgres:5432/userdb
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
      jpa:
        hibernate:
          ddl-auto: validate
    logging:
      level:
        com.example.userservice: DEBUG
  logback-spring.xml: |
    <configuration>
      <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
          <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
      </appender>
      <root level="INFO">
        <appender-ref ref="STDOUT"/>
      </root>
    </configuration>
---
apiVersion: v1
kind: Secret
metadata:
  name: user-service-secrets
  annotations:
    sealedsecrets.bitnami.com/managed: "true"    # 密钥管理
type: Opaque
data:
  DB_USERNAME: dXNlcm5hbWU=      # base64 编码
  DB_PASSWORD: cGFzc3dvcmQ=
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  template:
    spec:
      containers:
        - name: user-service
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: DB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: user-service-secrets
                  key: DB_USERNAME
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: user-service-secrets
                  key: DB_PASSWORD
          volumeMounts:
            - name: config-volume
              mountPath: /config
            - name: secret-volume
              mountPath: /etc/secrets
      volumes:
        - name: config-volume
          configMap:
            name: user-service-config
        - name: secret-volume
          secret:
            secretName: user-service-secrets
```

**优势**：
- 配置版本化
- 密钥安全管理
- 热更新支持
- 环境隔离

## 4. API 网关 (API Gateway)

### Spring Cloud Gateway 方式
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
            - Method=GET,POST
            - Header=X-Request-ID, \d+
          filters:
            - StripPrefix=2
            - AddRequestHeader=X-Source, gateway
            - Retry=3
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 10
                redis-rate-limiter.burstCapacity: 20
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
```

### Kubernetes Ingress 方式
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: api-gateway
  annotations:
    kubernetes.io/ingress.class: "nginx"
    # URL 重写
    nginx.ingress.kubernetes.io/rewrite-target: /$2
    # CORS 配置
    nginx.ingress.kubernetes.io/enable-cors: "true"
    nginx.ingress.kubernetes.io/cors-allow-origin: "*"
    nginx.ingress.kubernetes.io/cors-allow-methods: "GET, POST, PUT, DELETE"
    nginx.ingress.kubernetes.io/cors-allow-headers: "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization"
    # 限流配置
    nginx.ingress.kubernetes.io/rate-limit: "100"
    nginx.ingress.kubernetes.io/rate-limit-window: "1m"
    # 认证配置
    nginx.ingress.kubernetes.io/auth-url: "https://oauth.example.com/oauth2/auth"
    nginx.ingress.kubernetes.io/auth-signin: "https://oauth.example.com/oauth2/start"
    nginx.ingress.kubernetes.io/auth-response-headers: "X-User-ID,X-User-Email"
    # SSL 配置
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
    # 重试配置
    nginx.ingress.kubernetes.io/retry: "3"
    nginx.ingress.kubernetes.io/service-upstream: "true"
spec:
  tls:
    - hosts:
        - api.example.com
      secretName: api-tls
  rules:
    - host: api.example.com
      http:
        paths:
          - path: /api/users(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: user-service
                port:
                  number: 8080
          - path: /api/orders(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: order-service
                port:
                  number: 8080
```

**优势**：
- 高性能反向代理
- 丰富的插件生态
- 云原生安全集成
- 自动 SSL 证书

## 5. 断路器和容错 (Circuit Breaker)

### Spring Cloud Hystrix 方式
```yaml
feign:
  hystrix:
    enabled: true
hystrix:
  command:
    default:
      circuitBreaker:
        requestVolumeThreshold: 20
        sleepWindowInMilliseconds: 5000
        errorThresholdPercentage: 50
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 30000
      metrics:
        rollingStats:
          timeInMilliseconds: 10000
```

### Kubernetes + Istio 方式
```yaml
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: user-service
  annotations:
    annotations.istio.io/ignore: "false"    # Istio 配置管理
spec:
  host: user-service
  trafficPolicy:
    connectionPool:
      tcp:
        maxConnections: 100
        connectTimeout: 30s
      http:
        http1MaxPendingRequests: 50
        maxRequestsPerConnection: 10
    loadBalancer:
      simple: ROUND_ROBIN
      localityLbSetting:
        enabled: true
    circuitBreaker:
      consecutiveErrors: 5                 # 连续错误阈值
      interval: 30s                        # 检查间隔
      baseEjectionTime: 30s                # 基础熔断时间
      maxEjectionPercent: 50               # 最大熔断比例
      minHealthPercent: 50                 # 最小健康比例
    outlierDetection:
      consecutive5xxErrors: 3              # 5xx 错误阈值
      interval: 10s
      baseEjectionTime: 30s
      maxEjectionPercent: 50
      minHealthPercent: 40
---
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: user-service
spec:
  hosts:
    - user-service
  http:
    - fault:                              # 故障注入
      delay:
        percentage:
          value: 0.1                      # 10% 请求延迟
        fixedDelay: 5s
    - retries:                            # 重试配置
        attempts: 3
        perTryTimeout: 2s
        retryOn: 5xx,gateway-error,connect-failure,refused-stream
    timeout: 30s                          # 超时配置
    route:
      - destination:
          host: user-service
          subset: v1
```

**优势**：
- 语言无关的容错机制
- 细粒度的流量控制
- 实时故障注入测试
- 分布式追踪集成

## 6. 监控和追踪 (Monitoring & Tracing)

### Spring Cloud Sleuth 方式
```yaml
spring:
  sleuth:
    zipkin:
      base-url: http://zipkin:9411
      enabled: true
    sampler:
      probability: 1.0
  application:
    name: user-service
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### Kubernetes Annotations 方式
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  template:
    metadata:
      annotations:
        # Prometheus 监控
        prometheus.io/scrape: "true"           # 启用指标抓取
        prometheus.io/port: "8081"             # Actuator 端口
        prometheus.io/path: "/actuator/prometheus"
        # Jaeger 分布式追踪
        sidecar.jaegertracing.io/inject: "true"           # 自动注入 Jaeger
        jaegertracing.io/inject-java-agent: "true"
        # Grafana 仪表板
        grafana.io/dashboard: "user-service-metrics"      # 自动关联仪表板
        # 日志收集
        fluentd.org/include: "true"                      # Fluentd 收集
        fluentd.org/exclude: "false"
        # 应用性能监控
        datadoghq.com/checks: '["redis","postgres"]'     # Datadog 监控
        datadoghq.com/logs.enabled: "true"
        # APM 集成
        appdynamics.com/java.agent.enabled: "true"
        newrelic.com/java.agent.enabled: "true"
        # 安全扫描
        container.apparmor.security.beta.kubernetes.io/user-service: "runtime/default"
        seccomp.security.alpha.kubernetes.io/pod: "runtime/default"
    spec:
      containers:
        - name: user-service
          env:
            - name: SPRING_APPLICATION_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.labels['app']
            - name: POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
            - name: JAEGER_SERVICE_NAME
              value: "user-service"
            - name: JAEGER_AGENT_HOST
              value: "localhost"
            - name: JAEGER_SAMPLER_TYPE
              value: "const"
            - name: JAEGER_SAMPLER_PARAM
              value: "1"
```

**优势**：
- 原生监控集成
- 多种 APM 工具支持
- 自动服务发现
- 零配置追踪

## 7. 安全认证 (Security & Authentication)

### Spring Security OAuth2 方式
```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://keycloak:8080/auth/realms/myrealm
        registration:
          keycloak:
            client-id: user-service
            client-secret: ${KEYCLOAK_SECRET}
            scope: openid,profile,email
      resourceserver:
        jwt:
          issuer-uri: http://keycloak:8080/auth/realms/myrealm
```

### Kubernetes + OIDC 方式
```yaml
apiVersion: v1
kind: Service
metadata:
  name: user-service
  annotations:
    # OAuth2 Proxy 认证
    nginx.ingress.kubernetes.io/auth-url: "https://oauth.example.com/oauth2/auth"
    nginx.ingress.kubernetes.io/auth-signin: "https://oauth.example.com/oauth2/start"
    nginx.ingress.kubernetes.io/auth-response-headers: "X-Auth-Request-User,X-Auth-Request-Email,X-Auth-Request-Access-Token"
    # JWT 验证
    nginx.ingress.kubernetes.io/configuration-snippet: |
      auth_request_set $access_token $upstream_http_x_auth_request_access_token;
      proxy_set_header Authorization "Bearer $access_token";
    # Keycloak 集成
    nginx.ingress.kubernetes.io/auth-url: "http://keycloak-keycloak.default.svc.cluster.local/auth/realms/myrealm/protocol/openid-connect/auth"
    nginx.ingress.kubernetes.io/auth-signin: "http://keycloak-keycloak.default.svc.cluster.local/auth/realms/myrealm/protocol/openid-connect/token"
    # Rate Limiting
    nginx.ingress.kubernetes.io/rate-limit: "100"
    nginx.ingress.kubernetes.io/rate-limit-window: "1m"
---
apiVersion: v1
kind: Secret
metadata:
  name: keycloak-client-secret
  annotations:
    sealedsecrets.bitnami.com/managed: "true"
type: Opaque
data:
  client-secret: <base64-encoded-secret>
---
# Istio 认证策略
apiVersion: security.istio.io/v1beta1
kind: AuthorizationPolicy
metadata:
  name: user-service-authz
spec:
  selector:
    matchLabels:
      app: user-service
  rules:
    - from:
        - source:
            principals: ["cluster.local/ns/default/sa/frontend-sa"]
    - when:
        - key: request.auth.claims[role]
          values: ["admin", "user"]
---
# JWT 验证策略
apiVersion: security.istio.io/v1beta1
kind: RequestAuthentication
metadata:
  name: user-service-jwt
spec:
  selector:
    matchLabels:
      app: user-service
  jwtRules:
    - issuer: "http://keycloak.example.com/auth/realms/myrealm"
      jwksUri: "http://keycloak.example.com/auth/realms/myrealm/protocol/openid-connect/certs"
      forwardOriginalToken: true
```

**优势**：
- 统一认证网关
- 零信任网络
- 细粒度授权控制
- 自动证书轮换

## 8. 消息队列集成 (Message Queue Integration)

### Spring Cloud Stream 方式
```yaml
spring:
  cloud:
    stream:
      bindings:
        user-events:
          destination: user-events
          binder: rabbit1
          producer:
            required-groups: audit-group
            error-channel-enabled: true
        user-commands:
          destination: user-commands
          binder: kafka1
          consumer:
            group: user-service-group
            max-attempts: 3
            back-off-initial-delay: 1000
      binders:
        rabbit1:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: rabbitmq
                port: 5672
                username: guest
                password: guest
        kafka1:
          type: kafka
          environment:
            spring:
              kafka:
                bootstrap-servers: kafka:9092
                consumer:
                  group-id: user-service
```

### Kubernetes Operator 方式
```yaml
# RabbitMQ 集群
apiVersion: rabbitmq.com/v1beta1
kind: RabbitmqCluster
metadata:
  name: user-service-rabbitmq
  annotations:
    rabbitmq.com/operator: "managed"
spec:
  replicas: 3
  rabbitmq:
    additionalConfig: |
      cluster_formation.peer_discovery_backend = rabbit_peer_discovery_k8s
      cluster_formation.k8s.host = kubernetes.default.svc
      cluster_formation.k8s.address_type = hostname
      cluster_partition_handling = autoheal
  persistence:
    storage: 10Gi
    storageClassName: fast-ssd
---
# Kafka 集群
apiVersion: kafka.strimzi.io/v1beta2
kind: Kafka
metadata:
  name: user-service-kafka
  annotations:
    strimzi.io/kind: "kafka"
spec:
  kafka:
    replicas: 3
    storage:
      type: jbod
      volumes:
        - id: 0
          type: persistent-claim
          size: 100Gi
          class: fast-ssd
  zookeeper:
    replicas: 3
    storage:
      type: persistent-claim
      size: 10Gi
---
# Topic 配置
apiVersion: kafka.strimzi.io/v1beta2
kind: KafkaTopic
metadata:
  name: user-events
  labels:
    strimzi.io/cluster: user-service-kafka
  annotations:
    strimzi.io/kind: "topic"
spec:
  partitions: 3
  replicas: 2
  config:
    retention.ms: 604800000      # 7 days
    segment.bytes: 1073741824    # 1GB
---
# 配置映射
apiVersion: v1
kind: ConfigMap
metadata:
  name: user-service-messaging
data:
  application-messaging.yml: |
    spring:
      rabbitmq:
        host: user-service-rabbitmq
        port: 5672
        username: guest
        password: guest
        virtual-host: /
      kafka:
        bootstrap-servers: user-service-kafka-bootstrap:9092
        consumer:
          group-id: user-service-group
          auto-offset-reset: earliest
          key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
        producer:
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
```

**优势**：
- 运维级消息集群
- 自动故障恢复
- 监控和告警集成
- 弹性伸缩

## 9. CI/CD 和部署自动化

### Spring Cloud Config + Git 方式
```yaml
# 传统方式依赖 Git hooks 手动刷新
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/company/config-repo
          search-paths: '{application}'
          clone-on-start: true
          force-pull: true
```

### Kubernetes GitOps 方式
```yaml
# ArgoCD 配置
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: user-service
  annotations:
    argocd.argoproj.io/sync-options: "CreateNamespace=true"
    argocd.argoproj.io/compare-options: "IgnoreExtraneous"
spec:
  project: default
  source:
    repoURL: https://github.com/company/k8s-manifests
    targetRevision: main
    path: services/user-service
  destination:
    server: https://kubernetes.default.svc
    namespace: production
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
      - CreateNamespace=true
    retry:
      limit: 5
      backoff:
        duration: 5s
        factor: 2
        maxDuration: 3m
---
# Flux 配置
apiVersion: kustomize.toolkit.fluxcd.io/v1beta2
kind: Kustomization
metadata:
  name: user-service
  annotations:
    kustomize.toolkit.fluxcd.io/checksum: "abc123def456"
spec:
  interval: 10m
  path: ./services/user-service
  prune: true
  sourceRef:
    kind: GitRepository
    name: git-repo
  postBuild:
    substitute:
      ENVIRONMENT: production
      CLUSTER: us-west-2
---
# Tekton Pipeline
apiVersion: tekton.dev/v1beta1
kind: Pipeline
metadata:
  name: user-service-build
  annotations:
    tekton.dev/pipelines.minVersion: "0.12.1"
spec:
  tasks:
    - name: build
      taskRef:
        name: maven-build
      params:
        - name: GOALS
          value: ["clean", "package", "-DskipTests"]
    - name: test
      taskRef:
        name: maven-test
    - name: build-image
      taskRef:
        name: buildah
      params:
        - name: DOCKERFILE
          value: Dockerfile
        - name: IMAGE
          value: "registry.example.com/user-service:$(tasks.build.results.commit)"
---
# HelmRelease
apiVersion: helm.toolkit.fluxcd.io/v2beta1
kind: HelmRelease
metadata:
  name: user-service
  annotations:
    helm.toolkit.fluxcd.io/rollback-revision: "3"
spec:
  releaseName: user-service
  chart:
    spec:
      chart: ./charts/user-service
      sourceRef:
        kind: GitRepository
        name: charts-repo
  valuesFrom:
    - kind: ConfigMap
      name: user-service-values
    - kind: Secret
      name: user-service-secrets
  test:
    enable: true
```

**优势**：
- GitOps 工作流
- 声明式部署
- 自动化测试集成
- 版本化基础设施

## 10. 性能优化和资源管理

### Spring Cloud 方式
```yaml
# application.yml
spring:
  task:
    execution:
      pool:
        core-size: 8
        max-size: 32
        queue-capacity: 100
    scheduling:
      pool:
        size: 4
  jpa:
    hibernate:
      jdbc:
        batch_size: 50
        order_inserts: true
        order_updates: true
```

### Kubernetes 方式
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  template:
    metadata:
      annotations:
        # JVM 调优
        io.kubernetes.cri-o.cgroup.memory: "true"
        # 性能分析
        kubectl.kubernetes.io/default-container: "user-service"
        # 资源限制建议
        vpa.kubernetes.io/managed-by: "vertical-pod-autoscaler"
        # 节点亲和性
        scheduler.alpha.kubernetes.io/node-selector: "node-type=compute-optimized"
    spec:
      containers:
        - name: user-service
          env:
            - name: JAVA_OPTS
              value: >-
                -Xms512m -Xmx1g
                -XX:+UseG1GC
                -XX:MaxGCPauseMillis=200
                -XX:+UseContainerSupport
                -XX:MaxRAMPercentage=75.0
                -XX:+UnlockExperimentalVMOptions
                -XX:+UseCGroupMemoryLimitForHeap
            - name: SPRING_PROFILES_ACTIVE
              value: "prod"
            - name: SERVER_TOMCAT_THREADS_MAX
              value: "200"
            - name: SERVER_TOMCAT_ACCEPT_COUNT
              value: "100"
            - name: SERVER_TOMCAT_CONNECTION_TIMEOUT
              value: "20000"
          resources:
            requests:
              memory: "512Mi"
              cpu: "500m"
            limits:
              memory: "1Gi"
              cpu: "2"
          startupProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8081
            initialDelaySeconds: 30
            periodSeconds: 10
            timeoutSeconds: 5
            failureThreshold: 30
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8081
            initialDelaySeconds: 60
            periodSeconds: 30
            timeoutSeconds: 10
            failureThreshold: 3
---
# 垂直 Pod 自动伸缩
apiVersion: autoscaling.k8s.io/v1
kind: VerticalPodAutoscaler
metadata:
  name: user-service-vpa
spec:
  targetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: user-service
  updatePolicy:
    updateMode: "Auto"
  resourcePolicy:
    containerPolicies:
      - containerName: user-service
        maxAllowed:
          cpu: 4
          memory: 2Gi
        minAllowed:
          cpu: 200m
          memory: 256Mi
---
# 水平 Pod 自动伸缩
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: user-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: user-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
    - type: Pods
      pods:
        metric:
          name: http_requests_per_second
        target:
          type: AverageValue
          averageValue: "100"
```

**优势**：
- 自动资源调优
- 智能伸缩
- 性能监控集成
- 成本优化

## 迁移策略

### 阶段 1：基础监控迁移
```yaml
# 保留 Spring Boot Actuator，添加 Kubernetes 监控
metadata:
  annotations:
    prometheus.io/scrape: "true"           # 替代 Micrometer 注册
    prometheus.io/port: "8081"
    prometheus.io/path: "/actuator/prometheus"
```

### 阶段 2：服务发现迁移
```yaml
# 移除 Eureka/Consul 依赖
# 使用 Kubernetes 原生 Service
apiVersion: v1
kind: Service
metadata:
  name: user-service
spec:
  selector:
    app: user-service
```

### 阶段 3：配置外部化
```yaml
# 将 application.yml 迁移到 ConfigMap
apiVersion: v1
kind: ConfigMap
metadata:
  name: user-service-config
data:
  application.yml: |
    # 原配置内容
```

### 阶段 4：网关和安全迁移
```yaml
# 使用 Kubernetes Ingress 替代 Spring Cloud Gateway
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  annotations:
    kubernetes.io/ingress.class: "nginx"
    nginx.ingress.kubernetes.io/auth-url: "https://oauth.example.com/auth"
```

### 阶段 5：容错和流量管理
```yaml
# 使用 Istio 替代 Hystrix
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
spec:
  trafficPolicy:
    circuitBreaker:
      consecutiveErrors: 5
```

## 对比总结

| 功能领域 | Spring Cloud | Kubernetes | 迁移收益 |
|----------|--------------|------------|----------|
| **服务发现** | Eureka/Consul | Service/Endpoints | 减少组件复杂度 |
| **负载均衡** | Ribbon | Service/Ingress | 云原生集成 |
| **配置管理** | Config Server | ConfigMap/Secret | 版本化管理 |
| **API 网关** | Gateway/Zuul | Ingress Controller | 高性能插件 |
| **断路器** | Hystrix | Istio Circuit Breaker | 语言无关 |
| **监控追踪** | Sleuth/Prometheus | Annotations + APM | 零配置集成 |
| **安全认证** | Security OAuth | OIDC/Istio Auth | 统一身份管理 |
| **消息队列** | Stream | Kafka/RabbitMQ Operator | 运维级可靠性 |
| **CI/CD** | Git + Jenkins | GitOps (ArgoCD/Flux) | 声明式部署 |
| **资源管理** | 手动配置 | HPA/VPA | 自动优化 |

## 最佳实践

### 1. 渐进式迁移
- 优先迁移监控和日志
- 逐步替换配置管理
- 最后迁移复杂的服务治理

### 2. 兼容性考虑
```yaml
# 保持 Spring Boot 特性，利用 Kubernetes 基础设施
spring:
  application:
    name: user-service    # 用于 Kubernetes 标签
  management:
    endpoints:
      web:
        exposure:
          include: health,info,metrics,prometheus  # Kubernetes 监控端点
```

### 3. 多环境管理
```yaml
# 使用 Kustomize 管理环境差异
bases:
  - base/
overlays:
  - path: dev/
    - path: staging/
    - path: prod/
```

### 4. 安全加固
```yaml
# 安全注解模板
metadata:
  annotations:
    seccomp.security.alpha.kubernetes.io/pod: "runtime/default"
    container.apparmor.security.beta.kubernetes.io/container: "runtime/default"
    pod-security.policy/privileged: "false"
```

通过这种迁移方案，可以显著简化 Spring Cloud 项目的复杂度，同时获得更强大的云原生能力。