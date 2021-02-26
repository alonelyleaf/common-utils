package com.alonelyleaf.spring.event.order;

import com.alonelyleaf.spring.event.order.event.OrderCancelEvent;
import com.alonelyleaf.spring.event.order.event.OrderCreateEvent;
import com.alonelyleaf.spring.event.order.event.OrderPaidEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 订单事件 Listener
 *
 */
@AllArgsConstructor
@Component
@Slf4j
public class OrdersEventListener implements SmartApplicationListener {

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return OrderCreateEvent.class.isAssignableFrom(eventType)
            || OrderCancelEvent.class.isAssignableFrom(eventType)
            || OrderPaidEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("receive event: {}", event);
        if (event instanceof OrderCreateEvent) {
            // 逻辑处理;
        }

        if (event instanceof OrderCancelEvent) {
            // 逻辑处理;
        }

        if (event instanceof OrderPaidEvent) {
            // 逻辑处理;
        }
    }
}
