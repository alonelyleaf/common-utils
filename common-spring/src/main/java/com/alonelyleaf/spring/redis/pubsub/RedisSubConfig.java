package com.alonelyleaf.spring.redis.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 订阅多个频道，并在RedisMessageReceiver定义消息处理方法
 *
 * @author bijl
 * @date 2019/6/10
 */
@Configuration
public class RedisSubConfig {

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     *
     * @param connectionFactory
     * @param listenerColumnDictAdapter
     * @param listenerSysCconfigAdapter
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerColumnDictAdapter,
                                            MessageListenerAdapter listenerSysCconfigAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅通道指标映射字典
        container.addMessageListener(listenerColumnDictAdapter, new PatternTopic(Constant.Channel.CHANNEL_1));
        // 订阅系统配置频道
        container.addMessageListener(listenerSysCconfigAdapter, new PatternTopic(Constant.Channel.CHANNEL_2));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param redisMessageReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerColumnDictAdapter(RedisMessageReceiver redisMessageReceiver) {
        return new MessageListenerAdapter(redisMessageReceiver, "receiveColumnDictMessage");
    }

    @Bean
    MessageListenerAdapter listenerSysCconfigAdapter(RedisMessageReceiver redisMessageReceiver) {
        return new MessageListenerAdapter(redisMessageReceiver, "receiveSysConfigMessage");
    }
}
