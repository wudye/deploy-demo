jacoco-maven-plugin 是 JaCoCo (Java Code Coverage) 插件，用于测量 Java 代码的测试覆盖率。

JaCoCo 是什么？
JaCoCo 是一个免费的代码覆盖率工具，用于分析哪些代码被测试执行，哪些没有。

配置解析：
1. 版本 0.8.13
   JaCoCo 插件版本
2. <execution> 块配置了两个目标：
第一个执行 - prepare-agent
目标：准备 JaCoCo 代理
作用：在运行测试时启动代码覆盖率监控
时机：在测试阶段之前自动执行
第二个执行 - report
ID： report
阶段： test （测试阶段执行）
目标： report （生成覆盖率报告）
作用：测试完成后生成 HTML/XML/格式的覆盖率报告
生成的报告：
执行测试后，在 target/site/jacoco/ 目录下生成：

index.html ：主报告页面
com/.../...html ：各包和类的详细覆盖率
jacoco.xml ：XML 格式报告
jacoco.csv ：CSV 格式数据
报告内容：
行覆盖率：哪些代码行被执行
分支覆盖率：条件分支的覆盖情况
方法覆盖率：方法是否被调用
类覆盖率：类是否被实例化
使用方式：
Bash
mvn clean test
测试完成后自动生成覆盖率报告，可以查看哪些代码需要补充测试。

