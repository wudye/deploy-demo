hibernate:
naming:
implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
physical-strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
作用： 定义如何将 Java 类名和字段名转换为数据库表名和列名。

两种命名策略的区别
1. Implicit Naming Strategy（隐式命名策略）
   作用： 处理 JPA 注解中未显式指定名称的情况，提供逻辑名称。

处理内容：

实体类名（没有 @Table 注解时）
属性名（没有 @Column 注解时）
关联关系的默认名称
工作流程：

Java 类名 → Implicit Strategy → 逻辑名称 → Physical Strategy → 物理名称（数据库表名）
. Physical Naming Strategy（物理命名策略）
作用： 将逻辑名称转换为实际的数据库标识符（表名、列名）。

处理内容：

转换大小写（如驼峰转下划线）
转换特殊字符
添加前缀或后缀
处理数据库特定的命名规则
工作流程：

逻辑名称 → Physical Strategy → 物理名称（数据库表名）