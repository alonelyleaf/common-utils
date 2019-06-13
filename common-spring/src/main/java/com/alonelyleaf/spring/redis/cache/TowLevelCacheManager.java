package com.alonelyleaf.spring.redis.cache;

import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author bijl
 * @date 2019/6/10
 */
public class TowLevelCacheManager extends RedisCacheManager {

    RedisTemplate redisTemplate;

    public TowLevelCacheManager(RedisTemplate redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter,defaultCacheConfiguration);
        this.redisTemplate = redisTemplate;
    }
    //使用RedisAndLocalCache代替Spring Boot自带的RedisCache
    @Override
    protected Cache decorateCache(Cache cache) {
        return new RedisAndLocalCache(this, (RedisCache) cache);
    }

    public void publishMessage(String topicName, String cacheName) {

        this.redisTemplate.convertAndSend(topicName, cacheName);
    }

    // 接受一个消息清空本地缓存
    public void receiver(String name) {
        RedisAndLocalCache cache = ((RedisAndLocalCache) this.getCache(name));
        if(cache!=null){
            cache.clearLocal();
        }
    }
}
