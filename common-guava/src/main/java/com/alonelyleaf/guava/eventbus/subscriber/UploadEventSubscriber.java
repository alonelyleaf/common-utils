package com.alonelyleaf.guava.eventbus.subscriber;

import com.alonelyleaf.guava.base.ApplicationContextProvider;
import com.alonelyleaf.guava.eventbus.event.OfflineEvent;
import com.alonelyleaf.guava.eventbus.eventbus.EventBusCenter;

import com.alonelyleaf.util.JSONUtils;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/5/15
 */
@Component
public class UploadEventSubscriber {

    private static Logger logger = LoggerFactory.getLogger(UploadEventSubscriber.class);

    private EventBusCenter busCenter;

    public static final UploadEventSubscriber getInstance() {
        return UploadEventSubscriber.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UploadEventSubscriber INSTANCE = new UploadEventSubscriber();
    }

    private UploadEventSubscriber() {
        busCenter = ApplicationContextProvider.getBean(EventBusCenter.class);
        busCenter.register(this);
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(OfflineEvent event) {

        dealWith(event);
    }

    private void dealWith(OfflineEvent event){

        logger.info("UploadEventSubscriber receive event {}", JSONUtils.serialize(event));
    }
}
