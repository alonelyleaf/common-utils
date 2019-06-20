package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 系统内部错误
 *
 */
public class InternalServerException extends BusinessException {

    public InternalServerException(String message) {
        this(StatusCode.INTERNAL_ERROR.getCode(), message);
    }

    public InternalServerException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public InternalServerException(Integer code, String message) {
        super(code, message);
    }
}
