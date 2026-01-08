package com.example.geospatiallocationwithredis.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(LettuceConnectionFactory.class)
public class RedisConfig {

    // 仅当上下文中没有名为 "redisTemplate" 的 RedisTemplate 时创建（不会覆盖 Spring Boot 的自动配置）
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSer = new StringRedisSerializer();
        template.setKeySerializer(stringSer);
        template.setValueSerializer(stringSer);
        template.setHashKeySerializer(stringSer);
        template.setHashValueSerializer(stringSer);

        template.afterPropertiesSet();
        return template;
    }

    // 直接暴露 opsForGeo，使用 Spring Boot 自动配置的 StringRedisTemplate（如果存在）
    @Bean
    @ConditionalOnMissingBean(GeoOperations.class)
    
    public GeoOperations<String, String> geoOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForGeo();
    }
}



1. @Configuration(proxyBeanMethods = false)
Java
@Configuration(proxyBeanMethods = false)
作用: 关闭配置类的代理方法优化。

工作原理对比:

模式	特点	性能	说明
proxyBeanMethods = true (默认)	支持 @Bean 方法相互调用	稍慢	每次调用都返回同一个 Bean 实例
proxyBeanMethods = false	不支持 @Bean 方法相互调用	更快	直接调用，不经过代理
示例:

Java
@Configuration(proxyBeanMethods = true)  // 默认
public class Config1 {
    @Bean
    public BeanA beanA() {
        return new BeanA();
    }
    
    @Bean
    public BeanB beanB() {
        // 调用 beanA() 会返回同一个实例（单例）
        beanA().doSomething();
        return new BeanB();
    }
}
Java
@Configuration(proxyBeanMethods = false)
public class Config2 {
    @Bean
    public BeanA beanA() {
        return new BeanA();
    }
    
    @Bean
    public BeanB beanB() {
        // 调用 beanA() 会创建新实例（不是单例）
        beanA().doSomething();  // ⚠️ 注意：不是单例
        return new BeanB();
    }
}
何时使用 proxyBeanMethods = false ?

✅ 配置类中的 @Bean 方法不相互调用
✅ 希望提升启动性能
✅ 遵循 Spring Boot 2.x+ 的最佳实践