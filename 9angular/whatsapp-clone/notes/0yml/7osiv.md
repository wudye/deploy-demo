Open Session In View (OSIV) 模式详解
spring:
jpa:
open-in-view: false
作用： 禁用 Open Session In View 模式，明确事务边界。

什么是 Open Session In View (OSIV)？
定义
Open Session In View (OSIV) 是一种设计模式，它将 Hibernate Session（JPA EntityManager）的生命周期扩展到整个 HTTP 请求期间。

工作原理
OSIV 开启时的生命周期
HTTP 请求到达
↓
Spring DispatcherServlet
↓
OpenEntityManagerInViewFilter 拦截器
↓
打开 Hibernate Session
↓
调用 Controller 方法
↓
调用 Service 方法
↓
调用 Repository 方法（可能触发事务）
↓
事务提交，Session 关闭
↓
返回到 Controller
↓
返回响应（Session 仍然打开）⬅️ 关键！
↓
在视图中访问延迟加载的属性
↓
关闭 Session
↓
HTTP 响应发送
OSIV 关闭时的生命周期
HTTP 请求到达
↓
Spring DispatcherServlet
↓
调用 Controller 方法
↓
调用 Service 方法
↓
事务管理器打开 Session
↓
执行数据库操作
↓
事务提交，Session 关闭
↓
返回到 Controller
↓
返回响应（Session 已关闭）⬅️ 关键！
↓
HTTP 响应发送

配置	查询次数	耗时	可扩展性
OSIV 开启	101 次	500ms	❌ 差
OSIV 关闭	1 次	20ms	✅ 优秀

配置	事务边界	N+1 查询	性能	推荐
open-in-view: true	不清晰	❌ 隐藏问题	差	❌
open-in-view: false	✅ 清晰	✅ 立即发现	优秀	✅
关键优势
✅ open-in-view: false
- 明确的事务边界
- 立即发现 N+1 查询
- 强制最佳实践
- 性能更好
- 更容易调试