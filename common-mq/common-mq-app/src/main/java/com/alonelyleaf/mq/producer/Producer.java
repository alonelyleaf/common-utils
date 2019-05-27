package com.alonelyleaf.mq.producer;

import com.alonelyleaf.mq.core.producer.MessageProducer;
import com.alonelyleaf.mq.core.producer.MqSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 生产者
 *
 */
@Component
public class Producer extends MqSender {

    @Autowired
    @Qualifier("messageProducer")
    private MessageProducer messageProducer;

    @Override
    protected MessageProducer producer() {
        return messageProducer;
    }
}
