cyclonedx-maven-plugin 是用于生成 SBOM (Software Bill of Materials) 的插件。

什么是 CycloneDX？
CycloneDX 是一个开源的标准，用于创建软件物料清单（SBOM），详细描述软件组件的依赖关系。

插件作用：
生成 SBOM 文件：扫描项目依赖，生成包含所有组件信息的清单
安全合规：帮助识别漏洞、许可证合规性
供应链安全：了解软件供应链中的所有组件
生成的文件：
通常在 target/ 目录下生成：

bom.xml (XML 格式)
bom.json (JSON 格式)
包含的信息：
依赖库名称和版本
许可证信息
漏洞数据
组件哈希值
供应商信息
为什么重要：
安全审计：快速识别有漏洞的组件
合规要求：满足企业/政府的安全合规标准
漏洞响应：当发现新漏洞时，快速定位受影响的应用
使用方式：
Bash
mvn cyclonedx:makeAggregateBom
这是现代云原生应用开发中的最佳实践，特别适用于企业级应用的安全管理。

