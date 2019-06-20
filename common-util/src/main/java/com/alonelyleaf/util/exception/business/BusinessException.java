package com.alonelyleaf.util.exception.business;

import com.alonelyleaf.util.result.StatusCode;

/**
 * 业务异常
 *
 */
public class BusinessException extends BaseException {

    private Integer code;

    private Object data;

    public BusinessException(Integer code) {
        this.code = code;
    }

    public BusinessException(StatusCode code) {
        this.code = code.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public BusinessException setCode(Integer code) {
        this.code = code;
        return this;
    }

    public BusinessException setCode(StatusCode code) {
        this.code = code.getCode();
        return this;
    }

    public Object getData() {
        return data;
    }

    public BusinessException setData(Object data) {
        this.data = data;
        return this;
    }
}
