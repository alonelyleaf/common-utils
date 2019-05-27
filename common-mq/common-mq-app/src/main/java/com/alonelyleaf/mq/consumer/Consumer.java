package com.alonelyleaf.mq.consumer;

import com.alonelyleaf.mq.core.RocketConfig;
import com.alonelyleaf.mq.core.consumer.ConcurrentMessageConsumer;

import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.UUIDUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消费者
 */
public class Consumer extends ConcurrentMessageConsumer {

    public Consumer(RocketConfig config) {
        super(config);
    }

    @Override
    public void consume(MessageExt msg) {

        // consume message：decode and deal with
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        JStopWatch watch = new JStopWatch();
        String batchId = UUIDUtil.randomUUID();

        logger.info("[VAS CONSUMER] bid {} , size {} ", batchId, msgs.size());
        for (MessageExt msg : msgs) {
            logger.info("[VAS CONSUMER] bid {} id {} LATER {} ms", batchId, msg.getMsgId(), durationOfMsg(msg));
            try {

                if (processedSuccess(msg)) {
                    continue;
                }

                consume(msg);

                setProcessSuccessStatus(msg);

            } catch (Exception e) {
                logger.error("[VAS CONSUMER] bid {} id {} costs {}ms message {} will reconsume later", batchId, msg.getMsgId(), watch.elapsed(), msg.toString(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            logger.info("[VAS CONSUMER] SUCCESS bid {} id {} costs {} ms", batchId, msg.getMsgId(), watch.elapsed());
        }

        logger.info("[VAS CONSUMER] SUCCESS bid {} costs {} ms", batchId, watch.stop());
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
