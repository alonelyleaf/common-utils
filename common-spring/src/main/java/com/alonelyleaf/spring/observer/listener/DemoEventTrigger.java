package com.alonelyleaf.spring.observer.listener;

import com.alonelyleaf.spring.observer.event.DemoEvent;
import com.alonelyleaf.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author bijl
 * @date 2020/5/6
 */
@Component
public class DemoEventTrigger {

    private static final Logger logger = LoggerFactory.getLogger(DemoEventTrigger.class);

    @Autowired(required = false)
    private List<DemoListener> listeners;

    public void triggerOnAddEvent(DemoEvent.AddEdit event) {

        if (ValidateUtil.isEmpty(listeners)) {
            return;
        }

        eachExec(each -> {

            try {
                each.onAdd(event);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void triggerOnEditEvent(DemoEvent.AddEdit event) {

        if (ValidateUtil.isEmpty(listeners)) {
            return;
        }

        eachExec(each -> {

            try {
                each.onEdit(event);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public void triggerOnDeleteEvent(DemoEvent.Delete event) {

        if (ValidateUtil.isEmpty(listeners)) {
            return;
        }

        eachExec(each -> {

            try {
                each.onDelete(event);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    /**
     * 排序后遍历执行
     *
     * @param consumer
     */
    private void eachExec(Consumer<DemoListener> consumer) {

        listeners.forEach(consumer);
    }
}
