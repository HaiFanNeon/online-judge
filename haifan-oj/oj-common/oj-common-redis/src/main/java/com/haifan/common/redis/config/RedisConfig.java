package com.haifan.common.redis.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig implements CachingConfigurer {
    /**
     * 配置RedisTemplate Bean
     * @param connectionFactory 自动注入的Redis连接工厂
     * @return 自定义配置的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 设置连接工厂（必须）
        redisTemplate.setConnectionFactory(connectionFactory);

        /* 序列化配置策略 */

        // Key序列化：使用字符串序列化器（确保可读性）
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // Value序列化：使用自定义JSON序列化器（存储为JSON字符串）
        // 注意：Object.class可能导致类型擦除，实际生产建议根据业务类型细化
        redisTemplate.setValueSerializer(new JsonRedisSerializer<>(Object.class));

        // Hash Key序列化：同普通Key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Hash Value序列化：同普通Value
        redisTemplate.setHashValueSerializer(new JsonRedisSerializer<>(Object.class));

        // 初始化属性（Spring规范要求）
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}