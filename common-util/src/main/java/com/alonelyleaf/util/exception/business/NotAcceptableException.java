package com.alonelyleaf.util.exception.business;

import com.alonelyleaf.util.result.StatusCode;

public class NotAcceptableException extends BusinessException {

    public NotAcceptableException(String message) {
        this(StatusCode.NOT_ACCEPTABLE.getCode(), message);
    }

    public NotAcceptableException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public NotAcceptableException(Integer code, String message) {
        super(code, message);
    }
}
