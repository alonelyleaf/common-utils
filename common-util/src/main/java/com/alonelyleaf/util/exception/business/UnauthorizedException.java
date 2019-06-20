package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;

/**
 * 用户没有登录
 *
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        this(StatusCode.UNAUTHORIZED.getCode(), message);
    }

    public UnauthorizedException(String message, Object data) {
        this(message);
        this.setData(data);
    }

    public UnauthorizedException(Integer code, String message) {
        super(code, message);
    }
}
