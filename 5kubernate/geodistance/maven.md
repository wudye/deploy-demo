# 最佳实践总结
* 统一版本管理: 使用 <properties> 集中管理所有版本
* 测试分层: 单元测试 ( *Test.java ) + 集成测试 ( *IT.java )
* 代码质量: JaCoCo + SpotBugs + Checkstyle 三重保障
* 注解处理: Lombok + MapStruct + Binding 协同工作
* 环境隔离: 使用 Profile 管理不同环境配置
* 自动化发布: GPG 签名 + Nexus Staging 自动化流程

📊 配置检查清单
类别	    检查项	状态
基础配置	父 POM 配置	✅
        Java 版本	✅
        编码设置	✅
依赖管理	作用域正确	✅
        版本统一管理	✅
编译配置	注解处理器	✅
        预览特性	✅
测试配置	Surefire + Failsafe	✅
        Testcontainers	✅
代码质量	JaCoCo 覆盖率	✅
        SpotBugs 检查	✅
Checkstyle 规范	✅
        打包发布	Spring Boot 打包	✅
        源码 + Javadoc	✅
        GPG 签名	✅
        Nexus 发布	✅

#  编译器配置
步骤 3.1: Maven Compiler Plugin
<!-- Maven Compiler Plugin -->
    <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>${maven-compiler-plugin.version}</version>
    <configuration>
    <source>${java.version}</source>
    <target>${java.version}</target>
    <release>${java.version}</release>
    <encoding>${project.build.sourceEncoding}</encoding>

            <!-- 注解处理器配置 -->
            <annotationProcessorPaths>
                <path>
                    <groupId>org.mapstruct</groupId>
                    <artifactId>mapstruct-processor</artifactId>
                    <version>${mapstruct.version}</version>
                </path>
                <path>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok</artifactId>
                    <version>${lombok.version}</version>
                </path>
                <path>
                    <groupId>org.projectlombok</groupId>
                    <artifactId>lombok-mapstruct-binding</artifactId>
                    <version>${lombok-mapstruct-binding.version}</version>
                </path>
            </annotationProcessorPaths>
            
            <!-- 启用 Java 预览特性 -->
            <compilerArgs>
                <arg>-Xlint:all</arg>
                <arg>-Werror</arg>
                <arg>--enable-preview</arg>
            </compilerArgs>
            
            <!-- 显示编译警告 -->
            <showWarnings>true</showWarnings>
            <failOnWarning>true</failOnWarning>
        </configuration>
    </plugin>
代码质量配置
步骤 4.1: JaCoCo 代码覆盖率
<!-- JaCoCo 代码覆盖率插件 -->
        <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>${jacoco.version}</version>
        <executions>
        <!-- 准备 Agent -->
        <execution>
        <id>prepare-agent</id>
        <goals>
        <goal>prepare-agent</goal>
        </goals>
        </execution>

                    <!-- 单元测试覆盖率报告 -->
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    
                    <!-- 完整测试覆盖率报告 -->
                    <execution>
                        <id>post-test-report</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    
                    <!-- 代码覆盖率检查 -->
                    <execution>
                        <id>check</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>check</goal>
                        </goals>
                        <configuration>
                            <rules>
                                <rule>
                                    <element>PACKAGE</element>
                                    <limits>
                                        <limit>
                                            <counter>LINE</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${jacoco.line.coverage}</minimum>
                                        </limit>
                                        <limit>
                                            <counter>BRANCH</counter>
                                            <value>COVEREDRATIO</value>
                                            <minimum>${jacoco.branch.coverage}</minimum>
                                        </limit>
                                    </limits>
                                </rule>
                            </rules>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

步骤 4.2: SpotBugs 静态分析
<!-- SpotBugs 静态代码分析 -->
    <plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>${spotbugs.version}</version>
    <configuration>
    <effort>Max</effort>
    <threshold>Low</threshold>
    <xmlOutput>true</xmlOutput>
    <excludeFilterFile>spotbugs-exclude.xml</excludeFilterFile>
    </configuration>
    <executions>
    <execution>
    <id>spotbugs-check</id>
    <phase>verify</phase>
    <goals>
    <goal>check</goal>
    </goals>
    </execution>
    </executions>
    <dependencies>
    <dependency>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs</artifactId>
    <version>4.8.6</version>
    </dependency>
    </dependencies>
    </plugin>
步骤 4.3: Checkstyle 代码规范
<!-- Checkstyle 代码规范检查 -->
    <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>${checkstyle.version}</version>
    <configuration>
    <configLocation>checkstyle.xml</configLocation>
    <consoleOutput>true</consoleOutput>
    <failsOnError>true</failsOnError>
    <violationSeverity>warning</violationSeverity>
    </configuration>
    <executions>
    <execution>
    <id>checkstyle-check</id>
    <phase>verify</phase>
    <goals>
    <goal>check</goal>
    </goals>
    </execution>
    </executions>
    </plugin>

阶段 5: 测试配置
步骤 5.1: Surefire 单元测试
 <!-- Surefire 单元测试插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
                <configuration>
                    <!-- 测试文件匹配模式 -->
                    <includes>
                        <include>**/*Test.java</include>
                        <include>**/*Tests.java</include>
                    </includes>
                    <excludes>
                        <exclude>**/*IT.java</exclude>
                        <exclude>**/*IntegrationTest.java</exclude>
                    </excludes>
                    
                    <!-- JVM 参数 -->
                    <argLine>@{argLine} --enable-preview -Xmx512m</argLine>
                    
                    <!-- 并行执行配置 -->
                    <parallel>methods</parallel>
                    <threadCount>4</threadCount>
                    <perCoreThreadCount>false</perCoreThreadCount>
                    
                    <!-- 测试超时 -->
                    <forkedProcessTimeoutInSeconds>300</forkedProcessTimeoutInSeconds>
                    
                    <!-- 测试输出 -->
                    <useSystemClassLoader>false</useSystemClassLoader>
                </configuration>
            </plugin>
步骤 5.2: Failsafe 集成测试
Xml
<!-- Failsafe 集成测试插件 -->
<plugin>
<groupId>org.apache.maven.plugins</groupId>
<artifactId>maven-failsafe-plugin</artifactId>
<version>${maven-failsafe-plugin.version}</version>
<configuration>
<!-- 测试文件匹配模式 -->
<includes>
<include>**/*IT.java</include>
<include>**/*IntegrationTest.java</include>
<include>**/*E2ETest.java</include>
</includes>

                    <!-- JVM 参数 -->
                    <argLine>@{argLine} --enable-preview -Xmx1024m</argLine>
                    
                    <!-- 并行执行配置 -->
                    <parallel>classes</parallel>
                    <threadCount>2</threadCount>
                    
                    <!-- 测试超时 -->
                    <forkedProcessTimeoutInSeconds>600</forkedProcessTimeoutInSeconds>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>integration-test</goal>
                            <goal>verify</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
阶段 6: 打包和发布配置
步骤 6.1: Spring Boot Maven Plugin

    <!-- Spring Boot Maven Plugin -->
    <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    <!-- 排除编译时依赖 -->
    <excludes>
    <exclude>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    </exclude>
    </exclude>

                    <!-- 可执行 JAR 配置 -->
                    <executable>true</executable>
                    <classifier>exec</classifier>
                    
                    <!-- JVM 参数 -->
                    <jvmArguments>-Xms256m -Xmx1024m</jvmArguments>
                </configuration>
            </plugin>
步骤 6.2: JAR Plugin（普通 JAR）

        <!-- Maven JAR Plugin -->
        <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>${maven-jar-plugin.version}</version>
        <configuration>
        <archive>
        <manifest>
        <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
        <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
        </manifest>
        <manifestEntries>
        <Implementation-Build>${buildNumber}</Implementation-Build>
        <Build-Time>${maven.build.timestamp}</Build-Time>
        </manifestEntries>
        </archive>
        </configuration>
        </plugin>
步骤 6.3: 源码和 Javadoc

<!-- Maven Source Plugin -->
        <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>${maven-source-plugin.version}</version>
        <executions>
        <execution>
        <id>attach-sources</id>
        <goals>
        <goal>jar</goal>
        </goals>
        </execution>
        </executions>
        </plugin>

            <!-- Maven Javadoc Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>${maven-javadoc-plugin.version}</version>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <charset>UTF-8</charset>
                    <docencoding>UTF-8</docencoding>
                    <source>${java.version}</source>
                    <additionalOptions>
                        <additionalOption>--enable-preview</additionalOption>
                    </additionalOptions>
                    <doclint>none</doclint>
                    <quiet>true</quiet>
                </configuration>
                <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

阶段 7: 发布配置
步骤 7.1: GPG 签名
<!-- Maven GPG Plugin -->
    <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-gpg-plugin</artifactId>
    <version>${maven-gpg-plugin.version}</version>
    <executions>
    <execution>
    <id>sign-artifacts</id>
    <phase>verify</phase>
    <goals>
    <goal>sign</goal>
    </goals>
    </execution>
    </executions>
    <configuration>
    <gpgArguments>
    <arg>--pinentry-mode</arg>
    <arg>loopback</arg>
    </gpgArguments>
    </configuration>
    </plugin>

步骤 7.2: Nexus Staging
<!-- Nexus Staging Maven Plugin -->
    <plugin>
    <groupId>org.sonatype.plugins</groupId>
    <artifactId>nexus-staging-maven-plugin</artifactId>
    <version>${nexus-staging-maven-plugin.version}</version>
    <extensions>true</extensions>
    <configuration>
    <serverId>ossrh</serverId>
    <nexusUrl>https://s01.oss.sonatype.org/</nexusUrl>
    <autoReleaseAfterClose>true</autoReleaseAfterClose>
    </configuration>
    </plugin>
    </plugins>
    </build>
阶段 8: Profile 配置
步骤 8.1: 多环境 Profile
<!-- 多环境配置 -->
    <profiles>
    <!-- 开发环境 -->
    <profile>
    <id>dev</id>
    <activation>
    <activeByDefault>true</activeByDefault>
    </activation>
    <properties>
    <env>dev</env>
    <log.level>DEBUG</log.level>
    </properties>
    </profile>
    
            <!-- 测试环境 -->
            <profile>
                <id>test</id>
                <properties>
                    <env>test</env>
                    <log.level>INFO</log.level>
                </properties>
            </profile>
            
            <!-- 生产环境 -->
            <profile>
                <id>prod</id>
                <properties>
                    <env>prod</env>
                    <log.level>WARN</log.level>
                </properties>
            </profile>
            
            <!-- CI/CD 环境 -->
            <profile>
                <id>ci</id>
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-failsafe-plugin</artifactId>
                            <executions>
                                <execution>
                                    <goals>
                                        <goal>integration-test</goal>
                                        <goal>verify</goal>
                                    </goals>
                                </execution>
                            </executions>
                        </plugin>
                    </plugins>
                </build>
            </profile>
            
            <!-- 发布到 Maven Central -->
            <profile>
                <id>release</id>
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-gpg-plugin</artifactId>
                        </plugin>
                    </plugins>
                </build>
            </profile>
        </profiles>
        
        <!-- 发布配置 -->
        <distributionManagement>
            <snapshotRepository>
                <id>ossrh</id>
                <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
            </snapshotRepository>
            <repository>
                <id>ossrh</id>
                <url>https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/</url>
            </repository>
        </distributionManagement>
    </project>


🚀 常用 Maven 命令
# 编译项目
mvn clean compile

# 运行单元测试
mvn clean test

# 查看单元测试覆盖率
mvn test
open target/site/jacoco/index.htm

完整测试
# 运行完整测试（单元 + 集成）
mvn clean verify

# 查看完整覆盖率报告
mvn verify
open target/site/jacoco/index.html

代码质量检查
# 运行所有代码质量检查
mvn clean verify

# 生成 SpotBugs 报告
mvn spotbugs:check

# 运行 Checkstyle
mvn checkstyle:check

打包部署
# 打包可执行 JAR
mvn clean package

# 打包并跳过测试（不推荐用于生产）
mvn clean package -DskipTests

# 运行应用
java -jar target/geodistance-0.0.1-SNAPSHOT.jar


发布到 Maven Central
# 发布 SNAPSHOT 版本
mvn clean deploy

# 发布正式版本
mvn clean deploy -Prelease

