# Distributed log Tracing
1. 关联ID模式 (Correlation ID Pattern)
2. 现代分布式追踪的核心 (The Core of Modern Tracing)
    现代分布式追踪系统（如 OpenTelemetry, Jaeger, Zipkin）的核心是基于 Google Dapper 论文的一套更丰富的概念。它的核心是 Trace Context (追踪上下文)，主要包含两个ID：
   Trace ID (追踪ID):•这个就 等同于 你的 correlation-id。它在整个请求生命周期中是唯一的，用来标识这是同一次完整的端到端请求。
   Span ID (跨度ID):一个 Span 代表一次调用链中的 一个独立操作单元（比如一次API调用、一次数据库查询）。每次服务调用都会生成一个新的 Span ID。
   同时，它还会记录下 Parent Span ID，即调用它的那个操作的ID。
   1.请求进入网关，生成一个 Trace ID 和一个初始的 Span ID (我们称之为 Span A)。
    2.网关调用 Accounts 服务时，它会在HTTP Header中同时传递 Trace ID 和 Span A 的ID。
    3.Accounts 服务收到请求后：•它知道自己的 Trace ID 是什么。•它创建一个 新的 Span ID (我们称之为 Span B)。
    •它记录下自己的 Parent Span ID 是 Span A。•这样，Span A -> Span B 的父子调用关系就建立起来了。
    4.如果 Accounts 服务再去调用数据库，它会重复这个过程，创建 Span C，并记录 Span C 的父级是 Span B。
3. 自动化。通过一个 Agent (就像你pom.xml里的opentelemetry-javaagent) 自动拦截所有请求和调用，并注入/传递traceId和spanId。 |

# 可观测性的三大支柱 (The Three Pillars of Observability)
