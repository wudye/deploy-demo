SOLID 设计原则是面向对象设计的五条指导原则，目的是提高代码的可维护性、可扩展性与可重用性。简要说明：
单一职责原则（Single Responsibility Principle，SRP）
每个类应只有一个引起它变化的原因，即只负责一项职责。
开闭原则（Open/Closed Principle，OCP）
软件实体应对扩展开放，对修改关闭。通过抽象和扩展来增加功能，避免修改已有代码。
里氏替换原则（Liskov Substitution Principle，LSP）
子类必须可以替换父类，并且不破坏程序的正确性。子类行为应与父类契合。
接口隔离原则（Interface Segregation Principle，ISP）
使用多个专门的接口，而不是一个臃肿的接口。客户只依赖它感兴趣的接口方法。
依赖倒置原则（Dependency Inversion Principle，DIP）
高层模块不应依赖低层模块，两者都应依赖抽象；抽象不应依赖细节，细节应依赖抽象。