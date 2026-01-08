Jenkins 做的正是将您手动执行的操作自动化。

让我对比一下手动操作和 Jenkins 自动化的差异：

🔄 对比：手动 vs Jenkins 自动化
手动操作流程
Bash
# 1. 拉取代码
git clone https://github.com/Rapter1990/xlsxziptotxtzip.git
cd xlsxziptotxtzip

# 2. Maven 构建
mvn clean install

# 3. 构建 Docker 镜像
docker build -t noyandocker/xlsxziptotxtzip-jenkins:latest .

# 4. 登录 Docker Hub
docker login --username noyandocker

# 5. 推送镜像
docker push noyandocker/xlsxziptotxtzip-jenkins:latest
Jenkins 自动化流程
点击 Build Now → Jenkins 自动执行同样的步骤，无需人工干预。

🎯 本质区别
方面	手动操作	Jenkins 自动化
触发方式	手动输入命令	点击按钮或 Webhook
执行频率	想起来才做	每次提交代码自动执行
人为错误	容易出错	流程标准化，减少错误
历史记录	需要自己记录	自动保存构建历史
通知	需要主动检查	失败自动通知
环境一致性	依赖本地环境	Docker 确保一致性
耗时	每次重复操作	一次配置，多次复用
💡 Jenkins 的价值
1. 自动化重复工作
Bash
# 手动：每次都要执行这 5 步
git pull && mvn clean install && docker build ... && docker push ...

# Jenkins：只点一次 "Build Now"
2. 触发方式灵活
手动触发：点击 Build Now
Webhook 自动触发：Git 推送代码时自动构建
定时触发：每天凌晨 2 点自动构建
条件触发：某个分支合并时触发
3. 构建历史和回溯
每次构建都有记录
可以查看哪次构建出了问题
可以回滚到之前的版本
4. 多人协作
团队成员都可以触发构建
不需要每个人都配置 Maven、Docker
统一的构建环境
🎯 类比理解
手动操作 就像：

每天早上手动煮咖啡、烤面包
每次都要记得步骤和火候
Jenkins 自动化 就像：

设置咖啡机定时煮咖啡
早上起来咖啡已经好了
✅ 总结
是的，Jenkins 做的就是您手动做的事情：

✅ Maven 构建 JAR 文件
✅ 构建 Docker 镜像
✅ 推送到 Docker Hub
但 Jenkins 增加了：

🚀 自动化：不用每次手动输入命令
🔄 持续集成：代码提交自动构建
📊 可追溯：记录每次构建历史
🔔 通知：构建失败自动通知
👥 协作：团队共享构建流程
一句话总结：Jenkins 把手动执行变成自动化流程，让您专注于写代码，而不是重复的构建工作！ 🎉


jenkins/
├── docker-compose.yml    # Docker Compose 配置
├── Dockerfile           # Jenkins 容器定义
├── plugins.txt          # 预安装插件列表
└── init.groovy.d/       # Jenkins 初始化脚本
    └── create-pipeline.groovy  # 自动创建 Pipeline 任务


2. 预装插件（plugins.txt）
workflow-aggregator - Pipeline 支持
git - Git 版本控制
job-dsl - 任务自动创建
docker-plugin / docker-workflow - Docker 集成
ws-cleanup - 工作区清理
pipeline-stage-view - Pipeline 可视化


3. 自动创建任务（create-pipeline.groovy）
Jenkins 启动时自动创建名为 xlsxziptotxtzip 的 Pipeline 任务，配置如下：

仓库： https://github.com/Rapter1990/xlsxziptotxtzip.git
分支： main
Jenkinsfile：自动从仓库根目录读取