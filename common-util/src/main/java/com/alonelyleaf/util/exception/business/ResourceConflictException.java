package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 资源冲突
 *
 */
public class ResourceConflictException extends BusinessException {

    public ResourceConflictException(String message) {
        this(StatusCode.CONFLICT.getCode(), message);
    }

    public ResourceConflictException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public ResourceConflictException(Integer code, String message) {
        super(code, message);
    }
}
