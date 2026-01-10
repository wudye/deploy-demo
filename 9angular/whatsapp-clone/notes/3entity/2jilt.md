Jilt（org.jilt）是一个\“编译期生成 Builder\”的注解处理器库，用来替代手写 Builder 或 Lombok 的 @Builder。你在类上加 @org.jilt.Builder 之后，编译时会自动生成类似 XxxBuilder 的类（例如 AuthorityBuilder、AuthorityEntityBuilder），让你可以用链式方式构建对象，并且类型安全。
在你这个项目里它的用途主要是两点：
生成 Builder 类供映射/转换使用：例如把 JPA 实体转换成领域对象时，用生成的 AuthorityBuilder 来创建 Authority，避免写很多 new + setter。
将构建逻辑放到编译期：Builder 是编译期生成的，不是运行时反射；只要注解处理器正常跑，这些 *Builder 类就存在。

在 `Authority` 上标了 `@Builder` 之后，Jilt 会在编译期生成一个 Builder 类型，通常就是 `AuthorityBuilder`（以及类似 `AuthorityBuilder.authority()` 这种入口方法），因此你在实体映射里写 `AuthorityBuilder.authority()` 才能编译通过。
如果 IDE/Maven 没有跑注解处理器，就会出现你现在这种 `Cannot resolve symbol 'AuthorityBuilder'`（以及 `AuthorityEntityBuilder`）的报错。
在 Maven 下常见的做法是配置 `maven-compiler-plugin` 开启注解处理并把 Jilt processor 加到 `annotationProcessorPaths`（具体坐标以你项目使用的 Jilt 版本为准）：
<!-- pom.xml -->
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <configuration>
    <annotationProcessorPaths>
      <path>
        <groupId>org.jilt</groupId>
        <artifactId>jilt-processor</artifactId>
        <version>${jilt.version}</version>
      </path>
    </annotationProcessorPaths>
  </configuration>
</plugin>
另外在 IntelliJ IDEA 里也需要启用 Annotation Processing（否则 IDE 也可能解析不到生成的 `AuthorityBuilder`）。