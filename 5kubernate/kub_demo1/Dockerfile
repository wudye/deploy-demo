FROM eclipse-temurin:21-jre-ubi9-minimal
# for docker test no kubernates
WORKDIR /app

COPY target/kub_demo1-0.0.1-SNAPSHOT.jar app.jar

# 以 root 安装用户管理工具，创建用户并清理缓存
USER root
RUN microdnf install -y shadow-utils && \
    groupadd -g 1001 appuser && \
    useradd -u 1001 -g appuser -M -s /sbin/nologin appuser && \
    chown -R appuser:appuser /app && \
    microdnf clean all && rm -rf /var/cache/dnf /var/cache/yum

# 切换到非 root 用户运行
USER appuser

EXPOSE 8080 8081

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# mvn -DskipTests package
  #docker build -t kub_demo1:0.0.1 .
  # Buildpacks 流程：生成并运行镜像（指定镜像名）
# mvn spring-boot:build-image -Dspring-boot.build-image.imageName=kub_demo1:0.0.1
