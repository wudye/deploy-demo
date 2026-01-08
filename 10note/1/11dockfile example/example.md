完整工作流程
构建流程
1. 拉取基础镜像
   ↓
2. 设置工作目录: /build
   ↓
3. 复制 pom.xml（可缓存）
   ↓
4. 复制 .mvn（可缓存）
   ↓
5. 复制 src（不可缓存，源代码变化）
   ↓
6. Maven 编译打包
   ↓
7. 生成 JAR 文件
   ↓
8. 复制到运行阶段
   ↓
9. 删除构建阶段（只保留运行时）
运行流程
1. 启动容器
   ↓
2. 执行 ENTRYPOINT: java --enable-preview -jar geodistance.jar
   ↓
3. Spring Boot 应用启动
   ↓
4. 监听 1441 端口
   ↓
5. 接收 HTTP 请求


# 阶段 1: 构建
FROM maven:3.9.11-amazoncorretto-25 AS build
...
RUN mvn clean package

# 阶段 2: 运行
FROM amazoncorretto:25
COPY --from=build ... geodistance.jar
...
优点:

✅ 镜像体积小: 只包含 JAR 和 JDK
✅ 安全: 源代码不在最终镜像中
✅ 快速: 构建阶段可以缓存



构建方式	镜像大小	说明
单阶段（含 Maven）	~800 MB	包含 Maven、源代码、缓存
多阶段（当前）	~400 MB	只包含 JDK 和 JAR
优化后	~200 MB	使用 JRE、多阶段

总结
组件	作用
FROM maven	构建阶段基础镜像
FROM amazoncorretto	运行阶段基础镜像
AS build	命名构建阶段
--from=build	从构建阶段复制文件
EXPOSE	声明端口
ENTRYPOINT	启动命令
-DskipTests	跳过测试（生产构建）
--enable-preview	启用 JDK 25 预览特性


RUN mvn -q clean package -DskipTests
1.5 编译打包
参数	说明
mvn	Maven 命令
-q	Quiet 模式（减少输出）
clean	清理之前的构建
package	打包为 JAR 文件
-DskipTests	跳过测试（生产构建）


Docker 容器启动 (Maven 3.9.11)
    ↓
执行 mvn clean package
    ↓
Maven 读取 pom.xml
    ↓
发现 maven-compiler-plugin 3.11.0
    ↓
下载并使用 maven-compiler-plugin 3.11.0
    ↓
编译代码


版本对比
位置	组件	版本	说明
Dockerfile	Maven 基础镜像	3.9.11	Docker 容器中的 Maven 版本
pom.xml	maven-compiler-plugin	3.11.0	编译时使用的 Maven 插件版本
pom.xml	maven-surefire-plugin	3.5.2	测试时使用的 Maven 插件版本
是否会造成问题？
答案：不会造成问题！
原因 1: 不同的概念
Dockerfile 中的 Maven 版本:

Dockerfile
插入
复制
新建文件
保存
应用代码
FROM maven:3.9.11-amazoncorretto-25 AS build
这是 Maven 工具本身 的版本，用于：

执行 mvn 命令
管理 Maven 生命周期
解析依赖
pom.xml 中的插件版本:

Xml
插入
复制
新建文件
保存
应用代码
<properties>
    <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
    <maven-surfire-plugin.version>3.5.2</maven-surfire-plugin.version>
</properties>
这是 Maven 插件 的版本，用于：

编译代码（maven-compiler-plugin）
运行测试（maven-surefire-plugin）