package com.alonelyleaf.spring.redis.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/6/10
 */
@Component
public class MyRedisPubCenter {

    //使用自定义的RedisTemplate，也可使用默认的实现（StringRedisTemplate）
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发布者
     * @param message
     */
    public void publish(String channel, Object message) {

        redisTemplate.convertAndSend(channel, message);
    }
}
