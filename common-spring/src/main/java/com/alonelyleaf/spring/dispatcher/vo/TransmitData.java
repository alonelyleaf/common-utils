package com.alonelyleaf.spring.dispatcher.vo;

import java.io.Serializable;

public class TransmitData implements Serializable {

    /**
     * 接口
     */
    private Integer type;

    /**
     * 数据
     */
    private byte[] data;

    /**
     * data反序列化后得到的对象
     */
    private Object obj;

    //-------------------------------------- getter setter ---------------------------------------------

    public Integer getType() {
        return type;
    }

    public TransmitData setType(Integer type) {
        this.type = type;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public TransmitData setData(byte[] data) {
        this.data = data;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public TransmitData setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
