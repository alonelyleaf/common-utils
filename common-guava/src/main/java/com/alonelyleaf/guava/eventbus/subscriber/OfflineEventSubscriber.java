package com.alonelyleaf.guava.eventbus.subscriber;

import com.alonelyleaf.guava.base.ApplicationContextProvider;
import com.alonelyleaf.guava.eventbus.event.OfflineEvent;
import com.alonelyleaf.guava.eventbus.eventbus.EventBusCenter;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/5/15
 */
public class OfflineEventSubscriber {

    private EventBusCenter busCenter;

    public static final OfflineEventSubscriber getInstance() {
        return OfflineEventSubscriber.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final OfflineEventSubscriber INSTANCE = new OfflineEventSubscriber();
    }

    private OfflineEventSubscriber() {
        busCenter = ApplicationContextProvider.getBean(EventBusCenter.class);
        busCenter.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(OfflineEvent event) {

        dealWith(event);
    }

    private void dealWith(OfflineEvent event){

    }
}
