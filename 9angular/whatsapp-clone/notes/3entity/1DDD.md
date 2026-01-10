┌─────────────────────────────────────────┐
│         User Interface (用户界面)         │
│    - Controller                          │
│    - REST API                            │
│    - View                                │
└─────────────────────────────────────────┘
▲
│ 调用
│
┌─────────────────────────────────────────┐
│      Application (应用层)                 │
│    - Application Service                 │
│    - DTO / Command / Query               │
│    - Use Case                            │
└─────────────────────────────────────────┘
▲
│ 调用
│
┌─────────────────────────────────────────┐
│         Domain (领域层) ★ 核心            │
│    - Entity (实体)                       │
│    - Value Object (值对象)                │
│    - Aggregate (聚合)                    │
│    - Repository Interface (仓储接口)     │
│    - Domain Service (领域服务)           │
│    - Domain Event (领域事件)             │
└─────────────────────────────────────────┘
▲
│ 调用
│
┌─────────────────────────────────────────┐
│      Infrastructure (基础设施层)           │
│    - JPA Entity                          │
│    - Repository Implementation           │
│    - Database Configuration             │
│    - External API Client                 │
│    - Configuration                       │
└─────────────────────────────────────────┘

    ↑           ↑           ↑
用户界面层     应用层       领域层
│           │           │
└───────────┴───────────┘
↓
基础设施层

核心原则:

✅ 依赖方向从外向内
✅ 领域层不依赖任何其他层
✅ 基础设施层依赖领域层接口（依赖倒置）

总结
层次	职责	关键组件	依赖方向
用户界面层	处理用户交互	Controller, REST API	→ 应用层
应用层	编排业务用例	Application Service, DTO	→ 领域层
领域层	封装核心业务逻辑	Entity, Value Object, Aggregate, Repository Interface	无依赖（核心）
基础设施层	提供技术实现	JPA Entity, Repository Implementation, Configuration	→ 领域层接口
DDD 核心思想:

🎯 以领域为中心：代码组织围绕业务领域
🔒 分层架构：清晰的职责分离
🔄 依赖倒置：领域层不依赖基础设施层
📦 聚合设计：保证数据一致性
💎 值对象：封装不变的业务概念