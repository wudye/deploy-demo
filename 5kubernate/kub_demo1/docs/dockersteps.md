# 1) 打包（生成 jar）
mvn -DskipTests package

# 2) 构建镜像（镜像名要和 docker-compose 中的 image 匹配，或在 compose 使用 build）
docker build -t kub_demo1:0.0.1 .

# 3a) 启动（如果 docker-compose.yml 指向本地镜像）
docker compose up -d

# 3b) 或者让 compose 自动构建并启动（无需单独 docker build）
docker compose up --build -d

# 可选：如果 compose 使用了其他 image 名称，直接打 tag 让名字一致
docker tag kub_demo1:0.0.1 piomin/sample-spring-boot-on-kubernetes:0.0.1-SNAPSHOT
docker compose up -d

# 检查状态与日志
docker compose ps
docker compose logs -f spring-boot-app
