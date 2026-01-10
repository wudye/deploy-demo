package com.mwu.whatsappclone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// in spring boot applications, this is not strictly necessary as it is enabled by default
@EnableTransactionManagement
// 在 Spring Boot 应用中， @EnableJpaRepositories 是自动配置的，无需手动添加。
@EnableJpaRepositories(basePackages = "com.mwu.whatsappclone")
@EnableJpaAuditing
public class DatabaseConfiguration {
}
