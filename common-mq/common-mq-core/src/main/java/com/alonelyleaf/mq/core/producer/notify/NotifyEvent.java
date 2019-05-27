package com.alonelyleaf.mq.core.producer.notify;


import com.alonelyleaf.util.CommonUtil;
import com.alonelyleaf.util.UUIDUtil;

import java.io.Serializable;

/**
 * 通知事件
 *
 * @param <T>
 */
public class NotifyEvent<T> implements Serializable {

    /**
     * 事件唯一标识
     */
    private String uuid = UUIDUtil.randomUUID();

    /**
     * 事件创建时间
     */
    private Long bornTime = CommonUtil.now();

    /**
     * 事件类型
     */
    private String type;

    /**
     * 数据
     */
    private T data;

    public NotifyEvent(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public NotifyEvent setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Long getBornTime() {
        return bornTime;
    }

    public NotifyEvent setBornTime(Long bornTime) {
        this.bornTime = bornTime;
        return this;
    }

    public String getType() {
        return type;
    }

    public NotifyEvent setType(String type) {
        this.type = type;
        return this;
    }

    public T getData() {
        return data;
    }

    public NotifyEvent setData(T data) {
        this.data = data;
        return this;
    }
}
