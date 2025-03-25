package com.haifan.common.security.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.haifan.common.core.constants.HttpConstants;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 序列化与反序列化全局配置类
 * <p>
 * 功能说明：
 * 1. 统一处理HTTP请求中的日期时间字符串与LocalDateTime对象的转换
 * 2. 配置Jackson对LocalDateTime的序列化/反序列化格式
 * </p>
 */
@Configuration
public class LocalDateTimeSerializerConfig {

    /**
     * 定义字符串到LocalDateTime的转换器
     * <p>
     * 转换逻辑：
     * 1. 优先尝试ISO标准格式解析（如："2023-08-20T15:30:00"）
     * 2. 若失败则使用自定义格式（定义在HttpConstants.DATE_TIME_PATTER）
     * 3. 空字符串或空白内容返回null
     * </p>
     *
     * @return 字符串到LocalDateTime的转换器实例
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (source.trim().length() == 0) {
                    return null;
                }
                try {
                    // 优先尝试ISO格式解析
                    return LocalDateTime.parse(source);
                } catch (Exception e) {
                    // 使用项目统一配置的日期格式进行解析
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(HttpConstants.DATE_TIME_PATTER));
                }
            }
        };
    }

    /**
     * 自定义Jackson对象映射构建器
     * <p>
     * 配置说明：
     * 1. 设置统一的日期时间格式化模式
     * 2. 注册JavaTimeModule以支持LocalDateTime序列化
     * 3. 添加自定义的序列化器与反序列化器
     * </p>
     *
     * @return Jackson定制化构建器
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        JavaTimeModule module = new JavaTimeModule();
        // 创建使用统一格式的反序列化器
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern(HttpConstants.DATE_TIME_PATTER));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        return builder -> {
            // 设置全局日期格式
            builder.simpleDateFormat(HttpConstants.DATE_TIME_PATTER);
            // 注册LocalDateTime序列化器
            builder.serializers(new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern(HttpConstants.DATE_TIME_PATTER)));
            // 注册配置好的JavaTimeModule
            builder.modules(module);
        };
    }
}