package com.haifan.common.redis.service;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis操作工具类，封装常用Redis命令
 * 注意：需确保RedisTemplate配置正确的序列化器（如JsonRedisSerializer）
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate; // 注意：未指定泛型类型，可能存在类型安全风险

    // *********************** Key操作 ***********************

    /**
     * 检查key是否存在
     * @param key 键名
     * @return 存在返回true，否则false
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间（秒）
     * @param key 键名
     * @param timeout 过期时间（秒）
     * @return 设置成功返回true
     */
    public Boolean expire(final String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间（自定义单位）
     * @param key 键名
     * @param timeout 时间量
     * @param unit 时间单位
     * @return 设置成功返回true
     */
    public Boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取剩余有效时间
     * @param key 键名
     * @param unit 时间单位
     * @return 返回剩余时间
     */
    public Long getExpire(final String key, final TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 删除单个key
     * @param key 键名
     * @return 删除成功返回true
     */
    public Boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除keys（注意：参数应为Collection类型）
     * @param keys 键集合（实际参数应为Collection<String>）
     * @return 删除成功数量 >0 时返回true
     * @警告 参数类型Collections可能存在拼写错误，应改为Collection
     */
    public Boolean deleteObject(Collections keys) { // 错误：应为Collection类型
        return redisTemplate.delete(keys);
    }

    // *********************** String操作 ***********************

    /**
     * 设置缓存对象（无过期时间）
     * @param key 键名
     * @param value 存储对象（自动序列化）
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存对象（含过期时间）
     * @param key 键名
     * @param value 存储对象
     * @param timeout 时间量
     * @param timeUnit 时间单位
     */
    public <T> void setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获取缓存对象（自动反序列化）
     * @param key 键名
     * @param clazz 目标类型Class对象
     * @return 反序列化后的对象，不存在返回null
     * @注意 若RedisTemplate已配置正确序列化器，可能无需额外JSON解析
     */
    public <T> T getCacheObject(final String key, Class<T> clazz) {
        ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
        T t = valueOperations.get(key);
        // 如果序列化器配置不一致可能导致类型检查
        if (t instanceof String) {
            return t; // 直接返回字符串（可能不符合预期）
        }
        // 二次解析可能引发性能问题
        return JSON.parseObject(String.valueOf(t), clazz);
    }

    // *********************** List操作 ***********************

    /**
     * 获取列表长度
     * @param key 键名
     * @return 列表长度，key不存在返回0
     */
    public Long getListSize(final String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表范围元素
     * @param key 键名
     * @param start 起始索引（包含）
     * @param end 结束索引（包含）
     * @param clazz 元素类型Class
     * @return 反序列化后的对象列表，无数据返回null
     */
    public <T> List<T> getCacheListByRange(final String key, long start, long end, Class<T> clazz) {
        List range = redisTemplate.opsForList().range(key, start, end);
        return CollectionUtils.isEmpty(range) ? null :
                JSON.parseArray(JSON.toJSONString(range), clazz); // 存在序列化性能损耗
    }

    /**
     * 右推多个元素（尾部插入）
     * @param key 键名
     * @param list 元素集合
     * @return 操作后列表长度
     */
    public <T> Long rightPushAll(final String key, Collection<T> list) {
        return redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * 左推多个元素（头部插入）
     * @param key 键名
     * @param list 元素集合
     * @return 操作后列表长度
     */
    public <T> Long leftPushAll(final String key, Collection<T> list) {
        return redisTemplate.opsForList().leftPushAll(key, list);
    }

    /**
     * 移除列表中指定值的元素
     * @param key 键名
     * @param value 要移除的值
     * @return 移除的元素数量
     */
    public <T> Long removeForList(final String key, T value) {
        return redisTemplate.opsForList().remove(key, 1L, value); // 最多删除1次
    }

    // *********************** Hash操作 ***********************

    /**
     * 批量获取Hash字段值
     * @param key 键名
     * @param hkeys 字段名集合
     * @param clazz 值类型Class
     * @return 反序列化后的值列表（顺序与hkeys一致）
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<String> hkeys, Class<T> clazz) {
        List list = redisTemplate.opsForHash().multiGet(key, hkeys);
        List<T> result = new ArrayList<>();
        for (Object item : list) {
            // 每个元素单独解析可能影响性能
            result.add(JSON.parseObject(JSON.toJSONString(item), clazz));
        }
        return result;
    }

    /**
     * 设置Hash单个字段
     * @param key 键名
     * @param hKey 字段名
     * @param value 字段值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 批量设置Hash字段
     * @param key 键名
     * @param dataMap 字段键值对
     */
    public <K, T> void setCacheMap(final String key, final Map<K, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 删除Hash字段
     * @param key 键名
     * @param hKey 字段名
     * @return 删除的字段数量
     */
    public Long deleteCacheMapValue(final String key, final String hKey) {
        return redisTemplate.opsForHash().delete(key, hKey);
    }
}