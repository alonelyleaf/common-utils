package com.alonelyleaf.spring.event.order.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 订单支付成功事件
 *
 * @author bijl
 * @date 2021/2/26 下午2:35
 */
@Getter
public class OrderPaidEvent extends ApplicationEvent {

    private final String orderNo;

    public OrderPaidEvent(String orderNo) {
        super(orderNo);
        this.orderNo = orderNo;
    }
}
