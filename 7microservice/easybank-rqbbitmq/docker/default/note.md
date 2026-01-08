# 微服务可观测性：Prometheus, Grafana, Loki, Jaeger 的角色分工

在现代微服务架构中，为了全面监控系统的状态，我们通常依赖于“可观测性三大支柱”：指标（Metrics）、日志（Logs）和追踪（Traces）。不同的工具在其中扮演着不同的专业角色。

---

### 可观测性的三大支柱 (The Three Pillars of Observability)

一个完整的监控系统通常由这三部分组成，它们回答了关于系统状态的不同问题：

1.  **指标 (Metrics) - “发生了什么？”**
    *   **是什么：** 一系列随时间变化的 **数字值**。比如：CPU使用率、内存占用、每秒请求数 (RPS)、API调用的平均延迟。
    *   **作用：** 告诉你系统的宏观状态和趋势。让你能快速发现“CPU使用率突然飙升了！”或者“错误率正在上升！”
    *   **对应的工具：** **Prometheus**。Prometheus 就是业界领先的指标系统。

2.  **日志 (Logs) - “为什么会发生？”**
    *   **是什么：** 在特定时间点发生的、不可变的 **文本事件记录**。比如：`User 'test2' logged in successfully` 或者 `Database connection failed: timeout expired`。
    *   **作用：** 提供详细的、带有上下文的错误信息和事件背景，帮助你理解一个问题的根本原因。
    *   **对应的工具：** **Loki**、ELK Stack (Elasticsearch, Logstash, Kibana)、Splunk。

3.  **追踪 (Traces) - “它发生在哪里？”**
    *   **是什么：** 记录一次请求穿越多个微服务的 **完整旅程**。它由多个 **Span**（跨度）组成，每个 Span 代表一个独立的操作单元（如一次API调用）。
    *   **作用：** 帮助你理解分布式系统中的调用链，定位性能瓶颈和故障点。让你能回答“为什么这次请求花了5秒？原来是卡在了数据库查询上。”
    *   **对应的工具：** **Jaeger**、Zipkin、Grafana Tempo。

---

### 工具的角色分工与协同工作

*   **Prometheus (普罗米修斯):**
    *   **职责：** 专注的 **指标** 存储和查询系统。它像一个时间序列数据库，只关心数字。
    *   **它不能做什么：** 它不存储文本日志，也不存储调用链的结构化数据。所以你 **不能** 用它来“检查日志”或追踪。

*   **Loki:**
    *   **职责：** Grafana Labs 开发的 **日志** 聚合系统。它的设计理念是“像 Prometheus 一样做日志”，非常轻量和高效。

*   **Jaeger:**
    *   **职责：** 我们配置的 **追踪** 系统。它接收、存储并可视化 OpenTelemetry Agent 发来的追踪数据。

*   **Grafana (格拉法纳):**
    *   **职责：** **统一的可视化平台**。它就像一个电视屏幕，可以插上不同的信号源。
    *   它可以同时连接到 **Prometheus**、**Loki** 和 **Jaeger** 这三个不同的“数据源 (Data Source)”。

---

### 如何实现统一监控：Grafana + PLG (Prometheus, Loki, Grafana)

要实现统一查看所有信息的想法，你需要搭建一个如下图所示的架构：

![Grafana Observability Stack](https://grafana.com/static/img/blog/loki-prometheus-tempo.png)

**实现流程：**

1.  **运行所有后端服务：**
    *   **Prometheus** 负责收集指标。
    *   **Jaeger** (或 Grafana Tempo) 负责接收追踪数据。
    *   **Loki** 负责接收日志。

2.  **配置 OpenTelemetry Agent：**
    *   `opentelemetry-javaagent` 可以同时导出 Traces, Metrics 和 Logs。你需要配置它将不同类型的数据发送到对应的后端。

3.  **配置 Grafana：**
    *   在 Grafana 中，分别添加 Prometheus, Loki, Jaeger (或 Tempo) 作为数据源。

4.  **创建统一的仪表盘 (Dashboard)：**
    *   在同一个 Grafana 仪表盘上，你可以创建一个显示 **Prometheus 指标曲线** 的面板，旁边放一个显示 **Loki 错误日志** 的面板，再放一个显示 **Jaeger 最新追踪列表** 的面板。

**最强大的功能：数据关联**

Grafana 允许你在这些数据源之间建立关联。你可以实现这样的效果：
1.  在 Prometheus 的图表上看到一个延迟尖峰。
2.  点击这个尖峰，Grafana 会自动跳转到 Loki，并筛选出 **同一时间段内** 的所有相关日志。
3.  在日志中找到一个 `traceId`，点击它，Grafana 会自动跳转到 Jaeger，并展示出 **这次慢请求的完整调用链瀑布图**。

**结论：** 你 **不能** 直接用 Prometheus 来“检查日志”，但你 **可以** 使用 **Grafana** 作为统一的入口，它通过连接 **Prometheus (用于指标)**、**Loki (用于日志)** 和 **Jaeger (用于追踪)**，让你在一个地方看到所有你需要的信息，并实现它们之间的无缝跳转和关联分析。这正是现代云原生可观测性体系的强大之处。
