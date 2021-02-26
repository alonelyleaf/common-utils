package com.alonelyleaf.spring.event.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 订单取消事件
 *
 * @author bijl
 * @date 2021/2/26 下午2:35
 */
@Getter
public class OrderCancelEvent extends ApplicationEvent {

    private final String orderNo;

    public OrderCancelEvent(String orderNo) {
        super(orderNo);
        this.orderNo = orderNo;
    }
}
