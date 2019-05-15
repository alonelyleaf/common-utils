package com.alonelyleaf.guava.eventbus.publisher;

import com.alonelyleaf.guava.eventbus.event.OfflineEvent;
import com.alonelyleaf.guava.eventbus.event.UploadEvent;
import com.alonelyleaf.guava.eventbus.eventbus.EventBusCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/5/15
 */
@Component
public class EventPublisher {

    @Autowired
    private EventBusCenter eventBus;


    public void publishOfflineEvent(OfflineEvent offlineEvent){

        eventBus.post(offlineEvent);
    }

    public void publishUploadEvent(UploadEvent uploadEvent){

        eventBus.post(uploadEvent);
    }
}
