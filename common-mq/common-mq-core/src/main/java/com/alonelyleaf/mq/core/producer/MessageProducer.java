package com.alonelyleaf.mq.core.producer;


import com.alonelyleaf.mq.core.MQModule;
import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.mq.core.exception.MQConfigException;
import com.alonelyleaf.mq.core.exception.MQProduceException;
import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.ValidateUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQ生产者
 *
 */
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private DefaultMQProducer producer;

    private RocketConfig rocketConfig;

    public MessageProducer(RocketConfig config) {
        this.rocketConfig = config;
        init();
    }

    /**
     * 发送
     *
     * @param message
     * @return
     */
    public SendResult send(Message message) {

        try {
            SendResult sendResult = producer.send(message);

            if (logger.isDebugEnabled()) {
                logger.debug("{} [SYNC] message: {} send status : {}", MQModule.LOGGER_PREFIX_PRODUCER, message.toString(), sendResult.getMsgId());
            }

            return sendResult;
        } catch (Exception e) {
            logger.error("{} [SYNC] message: {} send failed", MQModule.LOGGER_PREFIX_PRODUCER, message.toString());
            throw new MQProduceException(e);
        }
    }

    /**
     * 发送迟延消息
     *
     * @param message
     * @param delayLevel
     * @return
     */
    public SendResult sendDelay(Message message, int delayLevel) {

        message.setDelayTimeLevel(delayLevel);
        return send(message);
    }

    /**
     * 顺序发送，根据hashSource计算hash值，相同hash值的发到同一队列
     *
     * @param message
     * @param hashSource
     * @return
     */
    public SendResult orderlySend(Message message, Object hashSource) {

        return orderlySend(message, new SelectMessageQueueByHash(), hashSource);
    }

    /**
     * 自定义规则的顺序发送
     *
     * @param message
     * @param queueSelector
     * @param arg
     * @return
     */
    public SendResult orderlySend(Message message, MessageQueueSelector queueSelector, Object arg) {

        try {
            SendResult sendResult = producer.send(message, queueSelector, arg);

            if (logger.isDebugEnabled()) {
                logger.debug("{} [ORDERLY] message : {} send status : ", MQModule.LOGGER_PREFIX_PRODUCER, sendResult.getMsgId(), sendResult.getSendStatus());
            }

            return sendResult;
        } catch (Exception e) {
            logger.error("{} [ORDERLY] message : {} send failed", MQModule.LOGGER_PREFIX_PRODUCER, message.toString());
            throw new MQProduceException(e);
        }
    }

    /**
     * 异步发送
     *
     * @param message
     */
    public void asyncSend(Message message) {

        asyncSend(message, new SendCallback() {

            @Override
            public void onSuccess(SendResult sendResult) {

                if (logger.isDebugEnabled()) {
                    logger.debug("{} [ASYNC] message: {} send success", MQModule.LOGGER_PREFIX_PRODUCER, sendResult.getMsgId());
                }
            }

            @Override
            public void onException(Throwable e) {

                logger.error("{} [ASYNC] message: {} send failed", MQModule.LOGGER_PREFIX_PRODUCER, message.toString(), e);
            }
        });
    }

    /**
     * 异步发送延迟消息
     *
     * @param message
     * @param delayLevel
     */
    public void asyncSendDelay(Message message, int delayLevel) {

        message.setDelayTimeLevel(delayLevel);
        asyncSend(message);
    }

    /**
     * 带自定义回调的异步发送延迟消息
     *
     * @param message
     * @param callback
     * @param delayLevel
     */
    public void asyncSendDelay(Message message, SendCallback callback, int delayLevel) {

        message.setDelayTimeLevel(delayLevel);
        asyncSend(message, callback);
    }

    /**
     * 自定义回调的异步发送
     *
     * @param message
     * @param callback
     */
    public void asyncSend(Message message, SendCallback callback) {

        try {
            producer.send(message, callback);
        } catch (Exception e) {
            logger.error("{} [ASYNC] message: {} send failed", MQModule.LOGGER_PREFIX_PRODUCER, message.toString());
            throw new MQProduceException(e);
        }
    }

    // ------------------------------------ private ------------------------------------
    private void init() {

        String server = server();
        if (ValidateUtil.isEmpty(server)) {
            logger.error("{} empty server, please check the configuration ", MQModule.LOGGER_PREFIX_PRODUCER);
            throw new MQConfigException("empty server, please check the configuration");
        }

        JStopWatch watch = new JStopWatch();

        this.producer = new DefaultMQProducer(rocketConfig.getProducer().getGroup());
        producer.setNamesrvAddr(server);
        producer.setCompressMsgBodyOverHowmuch(rocketConfig.getProducer().getCompressMsgBodyOverHowmuch());
        producer.setSendMsgTimeout(rocketConfig.getProducer().getSendMsgTimeout());
        producer.setRetryTimesWhenSendAsyncFailed(rocketConfig.getProducer().getRetryTimesWhenSendAsyncFailed());
        producer.setRetryTimesWhenSendFailed(rocketConfig.getProducer().getRetryTimesWhenSendFailed());
        producer.setMaxMessageSize(rocketConfig.getProducer().getMaxMessageSize());
        producer.setRetryAnotherBrokerWhenNotStoreOK(rocketConfig.getProducer().getRetryAnotherBrokerWhenNotStoreOk());

        try {
            logger.info("{} [INIT] start....................................", MQModule.LOGGER_PREFIX_PRODUCER);
            logger.info(JSONUtil.serialize(rocketConfig.getProducer()));

            producer.start();

            logger.info("{} [INIT] end costs {} ms....................................", MQModule.LOGGER_PREFIX_PRODUCER, watch.stop());
        } catch (MQClientException e) {
            logger.error("{} [INIT] FAIL", e);
            throw new MQConfigException(e.getMessage(), e);
        }
    }

    private String server() {

        return rocketConfig.getServers();
    }
}
