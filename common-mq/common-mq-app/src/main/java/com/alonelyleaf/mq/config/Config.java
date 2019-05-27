package com.alonelyleaf.mq.config;

import com.alonelyleaf.mq.consumer.Consumer;
import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.mq.core.producer.MessageProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static final String MQ_GROUP = "BE_CONFERENCE_MANAGER";

    @Value("${rocketmq.servers}")
    private String servers;

    @Bean(name = "rocketConfig")
    public RocketConfig rocketConfig() {

        RocketConfig config = new RocketConfig();
        config.setServers(servers);
        config.setConsumer(new RocketConfig.Consumer().setGroup(MQ_GROUP).setTopic(Topic.MESSAGE.getTopic()).setConsumeThreadMin(5));
        config.setProducer(new RocketConfig.Producer().setGroup(MQ_GROUP).setRetryTimesWhenSendFailed(2));
        return config;
    }

    @Bean("messageProducer")
    public MessageProducer vasProducer(@Qualifier("rocketConfig") RocketConfig vasRocketConfig) {

        return new MessageProducer(vasRocketConfig);
    }

    @Bean("messageConsumer")
    public Consumer consumer(@Qualifier("rocketConfig") RocketConfig vasRocketConfig) {

        Consumer consumer = new Consumer(vasRocketConfig);

        try {
            LOGGER.info("[VAS CONSUMER] start consumer");
            consumer.start();
            LOGGER.info("[VAS CONSUMER] start consumer success !");
            return consumer;
        } catch (MQClientException e) {
            LOGGER.error("[START VAS CONSUMER ERROR]", e);
            throw new RuntimeException(e);
        }
    }
}
