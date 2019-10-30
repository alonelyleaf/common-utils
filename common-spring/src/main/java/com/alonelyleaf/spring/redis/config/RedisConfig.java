package com.alonelyleaf.spring.redis.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author bijl
 * @date 2019/6/10
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class) //让我们这个配置类在内置的配置类之后在配置，这样就保证我们的配置类生效，并且不会被覆盖配置
public class RedisConfig {

    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {

        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    /**
     * redisTemplate 序列化使用的fastJson2JsonRedisSerializer
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer fastJson2JsonRedisSerializer) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
