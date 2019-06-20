package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 前置条件不满足
 *
 */
public class PreconditionFailedException extends BusinessException {

    public PreconditionFailedException(String message) {
        this(StatusCode.PRECONDITION_FAILED.getCode(), message);
    }

    public PreconditionFailedException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public PreconditionFailedException(Integer code, String message) {
        super(code, message);
    }
}
