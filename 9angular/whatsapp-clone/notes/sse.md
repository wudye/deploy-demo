📚 SSE（Server-Sent Events）详解
🎯 什么是 SSE？
SSE（Server-Sent Events） 是一种基于 HTTP 的单向服务器推送技术，允许服务器主动向浏览器客户端推送事件。

核心特点：

✅ 单向通信：服务器 → 客户端
✅ 基于 HTTP：使用标准 HTTP 协议
✅ 长连接：保持连接打开，持续推送数据
✅ 文本格式：使用 text/event-stream MIME 类型
✅ 自动重连：连接断开时浏览器自动重连
✅ 事件流：支持命名事件和事件 ID

🔍 SSE 工作原理图
┌─────────────┐                    ┌──────────────┐                    ┌─────────────┐
│   Browser   │                    │  Spring Boot │                    │  Keycloak   │
│ (Angular)   │                    │  Backend     │                    │             │
└──────┬──────┘                    └──────┬───────┘                    └──────┬──────┘
│                                    │                                   │
│ 1. GET /api/sse/subscribe          │                                   │
│    (with Bearer Token)            │                                   │
├─────────────────────────────────────>│                                   │
│                                    │ 2. Validate Token                │
│                                    ├───────────────────────────────────>│
│                                    │<───────────────────────────────────┤
│ 3. Create SSE Connection           │                                   │
│<────────────────────────────────────┤                                   │
│    (Content-Type: text/event-stream)│                                   │
│                                    │                                   │
│     ╔══════════════════════════╗  │                                   │
│     ║   Long-lived Connection  ║  │                                   │
│     ║      (Keep Alive)        ║  │                                   │
│     ╠══════════════════════════╣  │                                   │
│     ║  Event: heartbeat       ║  │  4. Every 5 seconds              │
│     ║  Data: Check heartbeat  ║  │<───────────────────────────────────┤
│     ║                          ║  │                                   │
│     ║  Event: MESSAGE         ║  │  5. New message arrived          │
│     ║  Data: {...message...}  ║  │<───────────────────────────────────┤
│     ║                          ║  │                                   │
│     ║  Event: TYPING          ║  │  6. User typing indicator         │
│     ║  Data: {...typing...}   ║  │<───────────────────────────────────┤
│     ╠══════════════════════════╣  │                                   │
│     ║  Connection stays open  ║  │                                   │
│     ╚══════════════════════════╝  │                                   │
│                                    │                                   │

🔬 SSE 数据格式
基本 SSE 消息格式
data: Hello World!

data: {"message": "Hello World!"}

event: message
data: {"text": "Hi there", "sender": "Alice"}

id: 12345
event: heartbeat
data: ping

retry: 5000
data: Keep alive

字段说明
字段	说明	示例
data	事件数据（必需）	data: Hello
event	事件名称（可选）	event: message
id	事件 ID（可选，用于重连）	id: 123
retry	重连间隔（毫秒，可选）	retry: 5000

方法	功能	调用频率
heartBeat()	发送心跳，保持连接活跃，更新用户在线状态	每 5 秒
addEmitter()	创建新的 SSE 连接，添加到 emitters map	用户登录时
sendMessage()	向特定用户推送消息	有新消息时

2. SSE 连接生命周期
   ┌─────────────────────────────────────────────────────────────────┐
   │                    SSE Connection Lifecycle                       │
   └─────────────────────────────────────────────────────────────────┘

1. 用户登录 Angular 应用
   ↓
2. OAuth2AuthService 调用 sseService.subscribe(token)
   ↓
3. 发送请求: GET /api/sse/subscribe
   Header: Authorization: Bearer <token>
   ↓
4. Spring Security 验证 JWT Token
   ↓
5. NotificationResource.subscribe() 被调用
   ↓
6. NotificationService.addEmitter() 创建 SseEmitter
    - 获取用户信息
    - 创建 SseEmitter(60000L) [60 秒超时]
    - 初始发送: "Starting connection..."
    - 将 emitter 添加到 Map
      ↓
7. 返回 SseEmitter，建立长连接
   Response:
   HTTP/1.1 200 OK
   Content-Type: text/event-stream
   Cache-Control: no-cache
   Connection: keep-alive
   ↓
8. 连接保持打开
   ↓
9. 每 5 秒发送心跳:
   Event: heartbeat
   Data: Check heartbeat...
   ↓
10. 有新消息时，调用 sendMessage():
    Event: MESSAGE
    Data: {"text": "...", "sender": "..."}
    ↓
11. 连接断开或超时时:
    - emitters map 中移除该用户
    - 浏览器自动重连
3. SSE 消息示例
4. 初始连接消息
   id: 2c64d3bb-8cde-4921-b16c-aa6f1536a7f5
   data: Starting connection...

心跳消息（每 5 秒）
event: heartbeat
id: 2c64d3bb-8cde-4921-b16c-aa6f1536a7f5
data: Check heartbeat...

新消息通知
event: MESSAGE
id: 2c64d3bb-8cde-4921-b16c-aa6f1536a7f5
data: {
"id": "msg-123",
"content": "Hello World",
"sender": {
"username": "Alice",
"publicId": "...",
"avatarUrl": "..."
},
"timestamp": "2026-01-10T12:00:00Z",
"conversationId": "conv-456"
}

用户正在输入
event: TYPING
id: 2c64d3bb-8cde-4921-b16c-aa6f1536a7f5
data: {
"conversationId": "conv-456",
"senderUsername": "Alice",
"isTyping": true
}

🔄 完整的消息推送流程
┌──────────────┐
│   User A     │
│  (Browser)   │
└──────┬───────┘
│
│ 发送消息
├─────────────────────────────────────────────────────────┐
│                                                         │
┌──────▼────────┐                                              │
│  Angular App  │                                              │
└──────┬────────┘                                              │
│                                                       │
│ POST /api/messages                                     │
│ { "content": "Hello", "conversationId": "..." }       │
├──────────────────────────────────────────────────────────┤
│                                                         │
┌──────▼──────────────────┐                                     │
│  Spring Boot Backend    │                                     │
│  MessageController      │                                     │
└──────┬──────────────────┘                                     │
│                                                       │
│ 保存消息到数据库                                        │
├──────────────────────────────────────────────────────────┤
│                                                         │
┌──────▼──────────────────┐                                     │
│  NotificationService     │                                     │
└──────┬──────────────────┘                                     │
│                                                       │
│ 查找会话参与者                                         │
│ - User A (发送者)                                      │
│ - User B (接收者)                                      │
│                                                       │
│ sendMessage(message, [UserB], MESSAGE)                   │
├──────────────────────────────────────────────────────────┤
│                                                         │
│ 1. 检查 User B 是否有活跃的 SSE 连接                    │
│ 2. 如果有，发送事件:                                    │
│    event: MESSAGE                                     │
│    data: {...message...}                                │
├──────────────────────────────────────────────────────────┤
│                                                         │
│ SSE 响应                                                │
│ HTTP/1.1 200 OK                                        │
│ Content-Type: text/event-stream                          │
│                                                         │
│ event: MESSAGE                                          │
│ id: <user-b-id>                                        │
│ data: { "content": "Hello", "sender": "UserA", ... }   │
├──────────────────────────────────────────────────────────┤
│                                                         │
┌──────▼────────┐                                              │
│   User B     │                                              │
│  (Browser)   │                                              │
└──────────────┘                                              │
│                                                       │
│ EventSource 接收到事件                                   │
│ update UI 显示新消息                                     │
│                                                       │
└─────────────────────────────────────────────────────────┘#


🔄 SSE 订阅和事件处理流程
┌─────────────────────────────────────────────────────────────────┐
│               Angular SSE Subscription Flow                       │
└─────────────────────────────────────────────────────────────────┘

1. 用户成功登录
   ↓
2. OAuth2AuthService.onSuccess
   ↓
3. 调用 sseService.subscribe(accessToken)
   ↓
4. 创建 EventSource 连接
   this.eventSource = new EventSourcePolyfill(
   `${API_URL}/sse/subscribe`,
   {
   headers: { "Authorization": `Bearer ${token}` },
   heartbeatTimeout: 60000
   }
   );
   ↓
5. 发送 HTTP 请求
   GET http://localhost:8080/api/sse/subscribe
   Headers:
   Authorization: Bearer eyJhbGci...
   ↓
6. Spring Boot 返回 SSE 连接
   Response:
   HTTP/1.1 200 OK
   Content-Type: text/event-stream
   Connection: keep-alive

   id: 2c64d3bb-8cde-4921-b16c-aa6f1536a7f5
   data: Starting connection...
   ↓
7. onopen 事件触发
   this.eventSource.onopen = (event) => {
   console.log("Connection SSE to server OK", event);
   }
   ↓
8. 开始接收服务器推送的事件
   ↓
9. onmessage 处理默认消息
   this.eventSource.onmessage = (event) => {
   if (event.data.indexOf("{") !== -1) {
   const message: Message = JSON.parse(event.data);
   this.receiveNewMessage$.next(message);
   }
   }
   ↓
10. addEventListener 处理命名事件
    this.eventSource.addEventListener("delete-conversation", event => {
    this.deleteConversation$.next(JSON.parse(event.data));
    });

    this.eventSource.addEventListener("view-messages", event => {
    this.viewMessages$.next(JSON.parse(event.data));
    });
    ↓
11. 组件订阅事件流
    this.sseService.receiveNewMessage.subscribe(message => {
    // 更新 UI 显示新消息
    });
    ↓
12. 错误处理和自动重连
    this.eventSource.onerror = (event) => {
    console.log("Connection SSE lost, let's retry to connect");
    this.retryConnectionToSSEServer();
    }

    private retryConnectionToSSEServer() {
    this.retryConnectionSubscription = interval(10000)
    .subscribe(() => this.subscribe(this.accessToken!));
    }
    📊 SSE 事件类型映射
    服务器事件	Angular 处理方式	说明
    心跳 heartbeat	自动处理	保持连接活跃，不触发 UI 更新
    新消息 (默认)	onmessage	新消息到达，更新聊天界面
    删除对话 delete-conversation	addEventListener	对话被删除，从列表移除
    查看消息 view-messages	addEventListener	用户查看了消息，更新未读状态
    🎯 完整的端到端 SSE 通信示例
    场景：User A 给 User B 发送消息
    时间线                           事件
    ─────────────────────────────────────────────────────────────
    T0                               User A 在 Angular 中发送消息

T0+100ms                         Angular 发送 POST 请求
POST http://localhost:8080/api/messages
Body: {
"content": "Hello!",
"conversationId": "conv-123",
"senderPublicId": "user-a-id"
}

T0+200ms                         Spring Boot 接收请求
MessageController.sendMessage()

T0+250ms                         保存消息到 PostgreSQL

T0+300ms                         调用 NotificationService.sendMessage()
向 User B 推送通知

T0+310ms                         NotificationService 查找 User B 的 SSE 连接
找到 emitters[user-b-id]

T0+320ms                         通过 SSE 发送事件给 User B
SSE Event:
event: MESSAGE
id: user-b-id
data: {
"id": "msg-456",
"content": "Hello!",
"sender": {
"username": "UserA",
"publicId": "user-a-id",
"avatarUrl": "..."
},
"sendDate": "2026-01-10T12:00:00Z",
"conversationId": "conv-123"
}

T0+350ms                         User B 的浏览器接收到事件
EventSource.onmessage 触发

T0+360ms                         Angular 组件接收到新消息
this.sseService.receiveNewMessage$
.subscribe(message => {
// 更新 UI
})

T0+370ms                         User B 的界面显示新消息
"Hello!" - UserA

T0+375ms                         更新未读消息计数

T0+5000ms                        后端发送心跳给所有用户
event: heartbeat
data: Check heartbeat...
(每 5 秒重复)
🔧 SSE 的优势和特性
相比 WebSocket 的优势
特性	SSE	WebSocket
方向	单向（服务器 → 客户端）	双向（双向通信）
协议	基于 HTTP	独立协议（ws://）
重连	自动重连（浏览器原生支持）	需要手动实现
防火墙	通过标准 HTTP 端口	可能被阻止
实现复杂度	简单	较复杂
适用场景	实时通知、进度更新	实时聊天、游戏
为什么 WhatsApp Clone 使用 SSE？

✅ 适用场景：实时接收消息、通知 ✅ 简化实现：不需要双向通信（发送消息用 HTTP POST） ✅ 自动重连：网络不稳定时自动恢复 ✅ 更容易调试：使用标准 HTTP 工具

🛡️ SSE 安全性