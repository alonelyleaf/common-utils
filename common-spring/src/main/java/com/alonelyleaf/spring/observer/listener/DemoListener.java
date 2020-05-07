package com.alonelyleaf.spring.observer.listener;

import com.alonelyleaf.spring.observer.event.DemoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bijl
 * @date 2020/5/6
 */
public interface DemoListener {

    void onAdd(DemoEvent.AddEdit addEdit);

    void onEdit(DemoEvent.AddEdit addEdit);

    void onDelete(DemoEvent.Delete delete);

    class Adapter implements DemoListener{

        protected Logger logger = LoggerFactory.getLogger(getClass());

        public void onAdd(DemoEvent.AddEdit addEdit){

        }

        public void onEdit(DemoEvent.AddEdit addEdit){

        }

        public void onDelete(DemoEvent.Delete delete){

        }
    }
}
