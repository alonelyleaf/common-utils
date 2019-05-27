package com.alonelyleaf.mq.core.consumer;

import com.alonelyleaf.mq.core.MQModule;
import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.UUIDUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public abstract class OrderlyMessageConsumer extends BaseConsumer implements MessageListenerOrderly {

    public OrderlyMessageConsumer(RocketConfig config) {
        super(config);
    }

    @Override
    public void registerListener() {
        registerMessageListener(this);
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {

        JStopWatch watch = new JStopWatch();

        String batchId = UUIDUtil.randomUUID();

        logger.info("{} bid {} , size {} ", MQModule.LOGGER_PREFIX_CONSUMER, batchId, msgs.size());

        for (MessageExt ext : msgs) {

            logger.info("{} bid {} id {} LATER {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), System.currentTimeMillis() - ext.getStoreTimestamp());

            // FIXME 此处不处理retry吗
            consume(ext);

            logger.info("{} SUCCESS bid {} id {} costs {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, ext.getMsgId(), watch.elapsed());
        }

        logger.info("{} SUCCESS bid {} costs {} ms", MQModule.LOGGER_PREFIX_CONSUMER, batchId, watch.stop());
        return ConsumeOrderlyStatus.SUCCESS;
    }
}
