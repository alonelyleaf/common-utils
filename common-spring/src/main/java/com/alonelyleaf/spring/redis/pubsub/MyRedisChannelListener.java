package com.alonelyleaf.spring.redis.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author bijl
 * @date 2019/6/6
 */
@Component
public class MyRedisChannelListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] channel = message.getChannel();
        byte[] body = message.getBody();
        try {
            String content = new String(body,"UTF-8");
            String address = new String(channel,"UTF-8");
            System.out.println("get " + content + " from " + address);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new MyRedisChannelListener());
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("news"));
        return container;

    }

}
