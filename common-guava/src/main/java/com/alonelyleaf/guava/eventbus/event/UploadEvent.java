package com.alonelyleaf.guava.eventbus.event;

import com.alonelyleaf.guava.base.Event;

/**
 * @author bijl
 * @date 2019/5/15
 */
public class UploadEvent implements Event {

    private String notifyId;

    private String type;

    private Object data;

    public UploadEvent(String notifyId, String type, Object data){
        this.notifyId = notifyId;
        this.type = type;
        this.data = data;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public UploadEvent setNotifyId(String notifyId) {
        this.notifyId = notifyId;
        return this;
    }

    public String getType() {
        return type;
    }

    public UploadEvent setType(String type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public UploadEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
