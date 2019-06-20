package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 远程服务不可用
 *
 */
public class RemoteServiceUnavailableException extends BusinessException {

    public RemoteServiceUnavailableException(String message) {
        this(StatusCode.GATEWAY_ERROR.getCode(), message);
    }

    public RemoteServiceUnavailableException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public RemoteServiceUnavailableException(Integer code, String message) {
        super(code, message);
    }
}
