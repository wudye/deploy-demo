apiVersion: v1 - 声明使用的 Kubernetes API 版本。ConfigMap 属于核心 API，始终使用 v1
kind: ConfigMap - 定义资源类型为 ConfigMap（配置映射）


name: geo-app-config - ConfigMap 的唯一标识符，其他资源（如 Deployment）通过此名称引用它
namespace: default - 将 ConfigMap 放置在默认命名空间中
关键组件：

命名空间隔离：允许在同一个集群中为不同环境或团队隔离配置
名称引用：Pod 通过名称挂载 ConfigMap，实现配置与代码分离

配置与代码分离

无需重新构建镜像即可修改配置
同一镜像可用于不同环境（dev/staging/prod）
集中管理

多个 Pod 共享同一配置
修改一处，全局生效
版本控制友好

ConfigMap 是 YAML 文件，可以纳入 Git
配置变更可追踪和回滚
环境隔离

通过 namespace 实现多环境配置
同一集群运行不同版本应用
🛠️ 最佳实践
敏感数据处理

❌ 不要将密码放入 ConfigMap
✅ 使用 Kubernetes Secret 存储敏感信息
配置验证

在应用启动时验证必需的配置项是否存在
使用 Spring Boot 的 @ConfigurationProperties 进行类型安全绑定
配置分层

公共配置：ConfigMap
环境特定配置：多 ConfigMap 或 ConfigMap + 环境变量覆盖
敏感配置：Secret



组件	作用	示例	约束
name	资源的唯一标识符	geo-app-config	同一 namespace 内唯一
namespace	逻辑隔离空间	default , dev , prod	集群内唯一标识
核心原则：

name + namespace = 资源的完整唯一标识
通过 namespace 实现环境隔离和资源隔离
不同 namespace 中的同名资源互不干扰


想象 Kubernetes 集群是一个大型办公楼：

Kubernetes	办公楼类比
Cluster	整个办公楼
Namespace	不同楼层或部门（研发部、市场部、财务部）
Resource Name	办公桌编号
Name + Namespace	"研发部-101号桌


# 查看 default 命名空间的所有 ConfigMap
kubectl get configmap -n default

# 查看 dev 命名空间的所有 ConfigMap
kubectl get configmap -n dev

# 查看所有命名空间中的 geo-app-config
kubectl get configmap -A | grep geo-app-config