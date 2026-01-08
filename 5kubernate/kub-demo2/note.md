# 可选：安装 skaffold（如果使用 Chocolatey）
choco install skaffold

# 确保 kubectl 指向 docker-desktop 集群
kubectl config use-context docker-desktop
kubectl get nodes

# 在项目根目录（包含 `skaffold.yaml`）执行一次性构建并部署
skaffold run

# 或者用于开发循环（自动重建/重部署）
skaffold dev

# 查看资源 / 日志
kubectl get pods
kubectl get svc
kubectl logs -f <pod-name>

# 端口转发到本机（替换 <service-name> 和端口）
kubectl port-forward svc/<service-name> 8080:<service-port>


# 删除当前 namespace 的所有 Service
kubectl delete svc --all

# 删除所有 namespace 的所有 Service
kubectl delete svc --all --all-namespaces

# 删除单个 Service
kubectl delete svc sample-spring-boot-on-kubernetes
# 或带 namespace
kubectl delete svc sample-spring-boot-on-kubernetes -n <namespace>

# 删除当前 namespace 的所有“常见”资源（pod、svc、deploy 等）
kubectl delete all --all

# 根据本地 manifest 删除（例如项目中的 k8s/ 目录）
kubectl delete -f k8s/

# 查看当前 context / 命名空间
kubectl config current-context
kubectl get ns
kubectl get all

# 预览将被删除的资源（只列名称）
kubectl get all -o name

# 在当前命名空间删除所有常见资源（Pod/Service/Deployment/...）
kubectl delete all --all
kubectl delete configmap --all
kubectl delete pvc --all
kubectl delete secret --all

# 或者只删除 Service / Pod
kubectl delete svc --all
kubectl delete pod --all

# 根据本地清单删除（如果项目有 `k8s/` 目录）
kubectl delete -f `k8s/`

# 在所有命名空间删除（危险，谨慎使用）
kubectl delete all --all --all-namespaces
kubectl delete configmap --all --all-namespaces
kubectl delete pvc --all --all-namespaces
