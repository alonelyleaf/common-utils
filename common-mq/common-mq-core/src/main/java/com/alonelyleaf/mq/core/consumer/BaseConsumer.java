package com.alonelyleaf.mq.core.consumer;

import com.alonelyleaf.mq.core.MQModule;
import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.mq.core.exception.MQConfigException;
import com.alonelyleaf.mq.core.exception.MQConsumeException;
import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.ValidateUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseConsumer extends DefaultMQPushConsumer {

    protected static final Logger logger = LoggerFactory.getLogger(BaseConsumer.class);

    protected RocketConfig config;

    protected String topic;
    protected String tags;


    public BaseConsumer(RocketConfig config) {
        super(config.getConsumer().getGroup());
        this.topic = config.getConsumer().getTopic();
        this.tags = config.getConsumer().getTags();
        this.config = config;
        init();
    }

    /**
     * 注册消费事件监听器
     */
    public abstract void registerListener();

    /**
     * 单条消息消费
     *
     * @param messageExt
     */
    public abstract void consume(MessageExt messageExt);

    // ---------------------------- private -------------------------------
    private void init() {

        String server = server();
        if (ValidateUtil.isEmpty(server)) {
            logger.error("{} empty server, please check the configuration ", MQModule.LOGGER_PREFIX_CONSUMER);
            throw new MQConfigException("empty server, please check the configuration");
        }

        JStopWatch watch = new JStopWatch();

        logger.info("{} [INIT] start....................................", MQModule.LOGGER_PREFIX_CONSUMER);
        logger.info(JSONUtil.serialize(config.getConsumer()));

        setNamesrvAddr(server);
        setConsumeThreadMax(config.getConsumer().getConsumeThreadMax());
        setConsumeThreadMin(config.getConsumer().getConsumeThreadMin());
        setMessageModel(MessageModel.CLUSTERING);

        subscribe();
        registerListener();

        logger.info("{} [INIT] end costs {} ms....................................", MQModule.LOGGER_PREFIX_CONSUMER, watch.stop());
    }

    private String server() {
        return config.getServers();
    }

    private void subscribe() {
        try {
            subscribe(topic, tags);
        } catch (MQClientException e) {
            logger.error("{} [INIT] FAIL", MQModule.LOGGER_PREFIX_CONSUMER, e);
            throw new MQConsumeException(e);
        }
    }
}
