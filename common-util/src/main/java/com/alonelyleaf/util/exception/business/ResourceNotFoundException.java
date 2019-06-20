package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 资源未找到
 *
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        this(StatusCode.NOT_FOUND.getCode(), message);
    }

    public ResourceNotFoundException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public ResourceNotFoundException(Integer code, String message) {
        super(code, message);
    }
}
