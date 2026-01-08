# JaCoCo (Java Code Coverage) 是一个代码覆盖率工具，用于分析代码测试的覆盖程度，帮助开发者了解哪些代码被执行了，哪些还没有测试到。
    
    核心作用:
    
    📊 量化测试覆盖率
    🔍 发现未测试的代码路径
    ✅ 质量控制门禁

<execution>
    <goals>
        <goal>prepare-agent</goal>
    </goals>
</execution>
在测试运行前准备 JaCoCo Java Agent（代理程序）。
工作原理
1. Maven 生命周期开始
   ↓
2. JaCoCo 准备 Agent
   ↓
3. 注入 JVM 参数: -javaagent:jacoco-agent.jar
   ↓
4. 测试运行时，Agent 拦截字节码执行
   ↓
5. 收集代码执行信息
   ↓
6. 生成 jacoco.exec 文件（覆盖率数据）

关键机制
Agent 注入的 JVM 参数会通过 @{argLine} 传递给 Surefire/Failsafe：

<execution>
    <id>report</id>
    <phase>prepare-package</phase>
    <goals>
        <goal>report</goal>
    </goals>
</execution>
目的
在打包前生成单元测试覆盖率报告。

执行时机: prepare-package 阶段
1. 单元测试完成
   ↓
2. 读取 jacoco.exec 文件
   ↓
3. 分析覆盖数据
   ↓
4. 生成 HTML 报告: target/site/jacoco/index.html
   ↓
5. 生成 XML 报告: target/site/jacoco/jacoco.xm
   报告内容
   生成的报告包括：

✅ 行覆盖率（Line Coverage）：每行代码是否被执行
✅ 分支覆盖率（Branch Coverage）：if/else 分支是否全覆盖
✅ 方法覆盖率（Method Coverage）：每个方法是否被调用
✅ 类覆盖率（Class Coverage）：每个类是否被实例化


<execution>
    <id>post-test-report</id>
    <phase>verify</phase>
    <goals>
        <goal>report</goal>
    </goals>
</execution>

目的
在集成测试验证后生成完整的覆盖率报告（包含单元测试 + 集成测试）。

执行时机: verify 阶段
工作流程
1. 单元测试完成
   ↓
2. 集成测试完成
   ↓
3. 合并覆盖率数据
   ↓
4. 生成最终报告
   ↓
5. 更新 target/site/jacoco/index.html

Maven 生命周期中的位置
完整的测试覆盖率收集流程:

validate
↓
initialize (prepare-agent 启动 JaCoCo Agent)
↓
compile
↓
test (单元测试，JaCoCo 收集数据)
↓
package
↓
prepare-package (report: 生成单元测试覆盖率报告)
↓
integration-test (集成测试，JaCoCo 继续收集)
↓
verify (post-test-report: 生成完整覆盖率报告)
↓
install
↓
deploy


# 运行单元测试
mvn clean test

# 查看报告
open target/site/jacoco/index.html

# 运行完整测试套件
mvn clean verify

# 查看最终报告
open target/site/jacoco/index.htm
报告可视化
总体指标:
✅ 绿色: 覆盖率 > 90%
⚠️ 黄色: 覆盖率 50%-90%
❌ 红色: 覆盖率 < 50%

代码级别:
✅ 121, 125, 128 (绿色 - 已覆盖)
❌ 130, 132 (红色 - 未覆盖)
⚠️ 135 (黄色 - 部分分支覆盖)
最佳实践建议
1. 合理的覆盖率目标
   测试类型	推荐覆盖率
   核心业务逻辑	≥ 90%
   工具类	≥ 95%
   配置类	≥ 70%
   实体类	≥ 60%