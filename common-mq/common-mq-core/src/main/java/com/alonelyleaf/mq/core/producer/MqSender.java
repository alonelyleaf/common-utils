package com.alonelyleaf.mq.core.producer;

import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.StringPool;
import org.apache.commons.codec.Charsets;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;

public abstract class MqSender {

    /**
     * 同步发送
     *
     * @param topic
     * @param message
     */
    public void send(String topic, Object message) {

        send(topic, StringPool.EMPTY, message);
    }

    /**
     * 带tag的同步发送
     *
     * @param topic
     * @param tags
     * @param message
     */
    public void send(String topic, String tags, Object message) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().send(mqMsg);
    }

    /**
     * 发送延迟消息，开源版本只支持18个level的延迟
     * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     *
     * @param topic
     * @param message
     * @param delayLevel
     */
    public void sendDelay(String topic, Object message, int delayLevel) {

        sendDelay(topic, StringPool.EMPTY, message, delayLevel);
    }

    public void sendDelay(String topic, String tags, Object message, int delayLevel) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().sendDelay(mqMsg, delayLevel);
    }

    /**
     * 异步发送
     *
     * @param topic
     * @param message
     */
    public void asyncSend(String topic, Object message) {

        asyncSend(topic, StringPool.EMPTY, message);
    }

    public void asyncSend(String topic, String tags, Object message) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().asyncSend(mqMsg);
    }

    /**
     * 异步发送延迟消息
     *
     * @param topic
     * @param message
     * @param delayLevel
     */
    public void asyncSendDelay(String topic, Object message, int delayLevel) {

        asyncSendDelay(topic, StringPool.EMPTY, message, delayLevel);
    }

    public void asyncSendDelay(String topic, String tags, Object message, int delayLevel) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().asyncSendDelay(mqMsg, delayLevel);
    }

    /**
     * 异步发送，自定义回调
     *
     * @param topic
     * @param message
     * @param callback
     */
    public void asyncSend(String topic, Object message, SendCallback callback) {

        asyncSend(topic, StringPool.EMPTY, message, callback);
    }

    public void asyncSend(String topic, String tags, Object message, SendCallback callback) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().asyncSend(mqMsg, callback);
    }

    /**
     * 异步发送延迟消息，自定义回调
     *
     * @param topic
     * @param message
     * @param delayLevel
     * @param callback
     */
    public void asyncSendDelay(String topic, Object message, int delayLevel, SendCallback callback) {

        asyncSendDelay(topic, StringPool.EMPTY, message, delayLevel, callback);
    }

    public void asyncSendDelay(String topic, String tags, Object message, int delayLevel, SendCallback callback) {

        if (message == null) {
            return;
        }

        Message mqMsg = new Message(topic, tags, serialize(message));
        producer().asyncSendDelay(mqMsg, callback, delayLevel);
    }


    // ------------------------------------ abstract ------------------------------------

    // 子类提供底层发送者
    protected abstract MessageProducer producer();

    // ------------------------------------ private ------------------------------------

    private byte[] serialize(Object message) {

        return JSONUtil.serialize(message).getBytes(Charsets.UTF_8);
    }
}
