package com.alonelyleaf.util.exception.business;

import com.alonelyleaf.util.result.StatusCode;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        this(StatusCode.FORBIDDEN.getCode(), message);
    }

    public ForbiddenException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public ForbiddenException(Integer code, String message) {
        super(code, message);
    }
}
