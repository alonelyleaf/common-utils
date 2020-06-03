package com.alonelyleaf.mq.listener;

import com.alonelyleaf.mq.consumer.Consumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * 在ready事件中启动消费者，防止消费者启动太快服务还未启动的情况
 *
 * @author bijl
 * @date 2020/5/19
 */
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Autowired(required = false)
    @Qualifier("messageConsumer")
    private Consumer consumer;

    public void onApplicationEvent(ApplicationReadyEvent event){

        if (consumer == null) {
            logger.info("[CONNECTOR] [CONSUMER] connector consumer is not init.");
            return;
        }

        try {
            logger.info("[VAS CONSUMER] start consumer");
            consumer.start();
            logger.info("[VAS CONSUMER] start consumer success !");
        } catch (MQClientException e) {
            logger.error("[START VAS CONSUMER ERROR]", e);
            throw new RuntimeException(e);
        }
    }

    public int getOrder(){

        return Ordered.LOWEST_PRECEDENCE;
    }
}
