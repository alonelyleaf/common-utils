package com.alonelyleaf.util.result.error;

public class FieldError {

    private String field;
    private String msg;

    public String getField() {
        return field;
    }

    public FieldError setField(String field) {
        this.field = field;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public FieldError setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}