skaffold.yaml 是 Skaffold 的配置文件，用来在开发/CI 流程中自动化构建、打包镜像并部署到 Kubernetes。要点概述：
功能：自动化「构建 → 标记/推送 → 部署」循环，支持本地热重载（skaffold dev）和一次性部署（skaffold run）。
构建部分：
默认 artifact 指定镜像名为 piomin/sample-spring-boot-on-kubernetes，并配置使用 jib（通过传入 Maven 参数 -Pjib、-DskipTests）。
有一个 buildpacks profile，可切换为 Paketo buildpacks（指定 builder、buildpacks、BP_JVM_VERSION=17 等环境变量）。
部署部分：
使用 kubectl 部署，清单在 k8s/mongodb-deployment.yaml、k8s/deployment.yaml（knative profile 使用 k8s/knative-service.yaml）。
profiles：通过 knative、buildpacks 等 profile 切换不同的构建/部署策略。
注意事项：若用 Jib，确保 Maven pom.xml 中把 Jib 配置放在 <build><plugins> 或 profile 插件配置里，而不是当作依赖；Skaffold 通过传入的 Maven 参数激活对应 profile。

Skaffold 工作流程：
开发模式：

Bash
插入
复制
新建文件
保存
运行
应用代码
skaffold dev
运行模式：

Bash
插入
复制
新建文件
保存
运行
应用代码
skaffold run
删除部署：

Bash
插入
复制
新建文件
保存
运行
应用代码
skaffold delete


完整部署序列
镜像构建完成 → Jib 创建容器镜像
应用 MongoDB 配置 → kubectl apply -f k8s/mongodb-deployment.yaml
等待 MongoDB 就绪 → Pod 状态变为 Running
应用 Spring Boot 配置 → kubectl apply -f k8s/deployment.yaml
服务发现建立 → 应用连接到 MongoDB
端点暴露 → 通过 Service 访问应用




skaffold dev --kube-context docker-desktop




