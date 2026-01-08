什么是 build-info 目标？
build-info 目标会生成一个 build-info.properties 文件，包含项目的构建信息，通常位于 target/classes/META-INF/build-info.properties 。

如何使用它？
自动执行：在 Maven 构建过程中自动执行
访问构建信息：通过 Spring Boot Actuator 的 /info 端点访问
生成的信息包括：
项目版本 (version)
构建时间 (time)
Git 分支和提交信息 (如果项目是 Git 仓库)
示例访问：
启动应用后，访问：

http://localhost:8080/actuator/info
会返回类似：

Json
    {
    "build": {
    "version": "0.0.1-SNAPSHOT",
    "time": "2025-12-11T10:30:00Z",
    "artifact": "kub_demo1",
    "group": "com.mwu",
    "name": "kub_demo1"
    }
    }
这对于生产环境的应用监控和版本追踪很有用。