# with docker desktop
    skaffold dev --kube-context docker-desktop
#  选择 Profile 的语法
    单个 Profile
    # 使用 knative profile（Knative Serverless 部署）
    skaffold dev --kube-context docker-desktop -p knative
    
    # 使用 buildpacks profile（Buildpacks 构建）
    skaffold dev --kube-context docker-desktop -p buildpacks
    多个 Profile 组合
    # 同时使用 buildpacks 构建 + knative 部署
    skaffold dev --kube-context docker-desktop -p buildpacks,knative
    
    # 注意顺序：先用 buildpacks 构建，再用 knative 部署

    # 默认行为（无 Profile）
    skaffold dev --kube-context docker-desktop


# Dockerfile 流程：先打包再用 Dockerfile 构建并运行
mvn -DskipTests package
docker build -t kub_demo1:0.0.1 .
docker run -p 8080:8080 -p 8081:8081 kub_demo1:0.0.1