package com.alonelyleaf.guava.eventbus.eventbus;

import com.alonelyleaf.guava.base.Event;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/5/15
 */
@Component
public class EventBusCenter {

    @Autowired
    private EventBus eventBus;

    public void post(Event event) {
        eventBus.post(event);
    }

    public void register(Object bean) {
        eventBus.register(bean);
    }

    public void unregister(Object bean) {
        eventBus.unregister(bean);
    }
}
