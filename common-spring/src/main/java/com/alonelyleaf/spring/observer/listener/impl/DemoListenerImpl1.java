package com.alonelyleaf.spring.observer.listener.impl;

import com.alonelyleaf.spring.observer.event.DemoEvent;
import com.alonelyleaf.spring.observer.listener.DemoListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2020/5/7
 */
@Component
@Order(2)
public class DemoListenerImpl1  extends DemoListener.Adapter {

    @Override
    public void onAdd(DemoEvent.AddEdit addEdit){
        // do something
    }

    @Override
    public void onEdit(DemoEvent.AddEdit addEdit){
        // do something
    }

    @Override
    public void onDelete(DemoEvent.Delete delete){
        // do something
    }
}
