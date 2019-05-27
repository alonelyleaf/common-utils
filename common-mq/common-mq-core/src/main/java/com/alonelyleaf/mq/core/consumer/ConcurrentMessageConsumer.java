package com.alonelyleaf.mq.core.consumer;

import com.alonelyleaf.mq.core.MQModule;
import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.mq.core.exception.NonRetryException;
import com.alonelyleaf.mq.core.exception.RetryException;
import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.UUIDUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public abstract class ConcurrentMessageConsumer extends BaseConsumer implements MessageListenerConcurrently {

    /**
     * 消息已处理标志位
     */
    public static final String PROCESS_SUCCESS_FLAG = "processSuccessFlag";
    public static final String PROCESS_SUCCESS_FLAG_VALUE = "1";

    /**
     * 消息过期时间，单位ms
     */
    public static final int MESSAGE_EXPIRE_LIMIT = 32000;

    public ConcurrentMessageConsumer(RocketConfig config) {
        super(config);
    }

    @Override
    public void registerListener() {
        registerMessageListener(this);
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        JStopWatch watch = new JStopWatch();

        String batchId = UUIDUtil.randomUUID();

        logger.info("{} bid {} , size {} ", MQModule.LOGGER_PREFIX_CONSUMER, batchId, msgs.size());

        for (MessageExt ext : msgs) {

            logger.info("{} bid {} id {} LATER {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), durationOfMsg(ext));

            try {

                /* 存在这样的情况，消费一批数据时，中间的某条消息消费出现异常会导致一整批数据重新投递 */
                if (processedSuccess(ext)) {

                    if (logger.isDebugEnabled()) {
                        logger.debug("{} [SKIP] bid {} id {} message {} has be processed, skip it", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), ext.toString());
                    }

                    if (logger.isInfoEnabled()) {
                        logger.info("{} [SKIP] bid {} id {} has be processed, skip it", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId());
                    }

                    continue;
                }

                // 过期消息跳过
                if (isExpired(ext)) {
                    logger.warn("{} [EXPIRED] bid {} id {} message {} has been generated {} ms, skip it", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), ext.toString(), durationOfMsg(ext));
                    continue;
                }

                // 消费数据
                consume(ext);

                // 已正常消费的设置处理标识位
                setProcessSuccessStatus(ext);

            } catch (RetryException e) {
                // 重新投递
                logger.error("{} [RETRY-EXCEPTION] bid {} id {} costs {} ms message {}", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), watch.elapsed(), ext.toString(), e);

                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } catch (NonRetryException e) {
                // 不再重新投递，当成消费成功处理
                logger.error("{} [NON-RETRY-EXCEPTION] bid {} id {}  costs {} ms message {}", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), watch.elapsed(), ext.toString(), e);
            }

            logger.info("{} SUCCESS bid {} id {} costs {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), watch.elapsed());
        }

        logger.info("{} SUCCESS bid {} costs {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, watch.stop());

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    protected boolean processedSuccess(MessageExt ext) {

        String flag = ext.getUserProperty(PROCESS_SUCCESS_FLAG);
        return PROCESS_SUCCESS_FLAG_VALUE.equals(flag);
    }

    protected void setProcessSuccessStatus(MessageExt ext) {

        ext.putUserProperty(PROCESS_SUCCESS_FLAG, PROCESS_SUCCESS_FLAG_VALUE);
    }

    protected boolean isExpired(MessageExt ext) {
        return durationOfMsg(ext) > MESSAGE_EXPIRE_LIMIT;
    }

    /**
     * 消息从产生到目前的持续时长
     *
     * @param ext
     * @return
     */
    protected long durationOfMsg(MessageExt ext) {
        return System.currentTimeMillis() - ext.getBornTimestamp();
    }
}
