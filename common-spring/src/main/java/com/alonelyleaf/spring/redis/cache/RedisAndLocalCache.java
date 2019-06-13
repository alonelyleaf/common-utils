package com.alonelyleaf.spring.redis.cache;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 构建一二级缓存
 * https://www.jianshu.com/p/3ee734377931
 *
 * 如果直接使用缓存可以使用redisTemplate来创建和操作
 * https://www.cnblogs.com/songanwei/p/9274348.html
 *
 * @author bijl
 * @date 2019/6/10
 */
public class RedisAndLocalCache implements Cache {
    // 本地缓存提供
    ConcurrentHashMap<Object, Object> local = new ConcurrentHashMap<Object, Object>();
    RedisCache redisCache;
    TowLevelCacheManager cacheManager;

    public RedisAndLocalCache(TowLevelCacheManager cacheManager, RedisCache redisCache) {
        this.redisCache = redisCache;
        this.cacheManager = cacheManager;
    }

    @Override
    public String getName() {
        return redisCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return redisCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {

        // 一级缓存先取
        ValueWrapper wrapper = (ValueWrapper) local.get(key);
        if (wrapper != null) {
            return wrapper;
        } else {
            // 二级缓存取
            wrapper = redisCache.get(key);
            if (wrapper != null) {
                local.put(key, wrapper);
            }
            return wrapper;
        }
    }

    @Override
    public <T> T get(Object key, @Nullable Class<T> type){

        Object value = get(key);
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException(
                    "Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader){

        ValueWrapper result = get(key);

        if (result != null) {
            return (T) result.get();
        }

        T value = valueFromLoader(key, valueLoader);
        put(key, value);
        return value;

    }

    @Override
    public void put(Object key, Object value) {
        System.out.println(value.getClass().getClassLoader());
        redisCache.put(key, value);
        //通知其他节点缓存更新
        clearOtherJVM();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {

        return redisCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        redisCache.evict(key);
        //通知其他节点缓存更新
        clearOtherJVM();
    }

    @Override
    public void clear() {
        this.local.clear();
        redisCache.clear();
        //通知其他节点缓存更新
        clearOtherJVM();
    }

    protected void clearOtherJVM() {
        cacheManager.publishMessage("redis_cache_change", redisCache.getName());
    }

    // 提供给CacheManager清空一节缓存
    public void clearLocal() {
        this.local.clear();
    }

    private static <T> T valueFromLoader(Object key, Callable<T> valueLoader) {

        try {
            return valueLoader.call();
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }
}
