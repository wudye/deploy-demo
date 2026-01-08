package com.mwu.geodistance.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/*
整体工作流程
请求到达 → LocaleResolver 从 Accept-Language 请求头解析用户语言
消息查询 → MessageSource 根据语言从对应的资源文件加载消息
验证错误 → LocalValidatorFactoryBean 使用国际化消息显示验证错误
 */
@Configuration
public class I18nConfig {
    /*
    设计选择：使用 AcceptHeaderLocaleResolver

根据 HTTP 请求头的 Accept-Language 字段自动选择语言
例如：请求头 Accept-Language: zh-CN,zh;q=0.9 会自动选择中文
默认语言：英文，当请求头没有指定语言时使用
     */

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return acceptHeaderLocaleResolver;
    }

    /*
    关键配置：

    setBasename("classpath:messages") ：指定资源文件基础名

    会加载 messages.properties （默认）
    会加载 messages_en.properties （英文）
    会加载 messages_zh.properties （中文）等
    setDefaultEncoding("UTF-8") ：确保支持中文等非 ASCII 字符

    setFallbackToSystemLocale(false) ：重要设计决策

    当找不到匹配语言的资源时，不回退到系统默认语言
    而是使用默认的英文资源文件
    避免因服务器操作系统语言不同导致的不一致行为
         */

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    // 将 Bean Validation（如 @NotNull 、 @Size ）的错误消息也国际化
    /*
    作用：将 Bean Validation（如 @NotNull 、 @Size ）的错误消息也国际化

设计亮点：

将自定义的 MessageSource 注入到验证器中
使得验证错误消息也能从资源文件中读取
统一了业务消息和验证消息的国际化机制
     */
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
