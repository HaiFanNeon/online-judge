package com.haifan.common.redis.config;

import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 自定义Redis JSON序列化器，用于替代默认的JDK序列化
 * 优势：存储数据为可读的JSON格式，支持跨语言、易调试
 * 泛型<T>：支持任意类型对象的序列化
 */
public class JsonRedisSerializer<T> implements RedisSerializer<T> {

    // 统一使用 UTF-8 字符集，避免编码不一致问题
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    // 目标类型，反序列化时用于确定具体类型
    private final Class<T> clazz;

    /**
     * 构造函数：必须传入目标类型的Class对象
     * @param clazz 要处理的Java类型，如User.class
     */
    public JsonRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 序列化方法：对象 -> JSON字节数组
     * @param t 要序列化的对象
     * @return JSON格式的字节数组，null返回空数组
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        // 使用Fastjson2将对象转为JSON字符串，再转为UTF-8字节数组
        return JSON.toJSONString(t).getBytes(DEFAULT_CHARSET);
    }

    /**
     * 反序列化方法：字节数组 -> Java对象
     * @param bytes Redis中读取的字节数据
     * @return 重构的Java对象，空数据返回null
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        // 字节数组转字符串后，使用Fastjson2解析为目标类型
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz);
    }
}