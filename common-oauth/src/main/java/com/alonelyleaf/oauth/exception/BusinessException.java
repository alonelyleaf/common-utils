package com.alonelyleaf.oauth.exception;

import com.alonelyleaf.oauth.exception.BaseException;
import lombok.Data;

/**
 * 业务异常
 *
 */
@Data
public class BusinessException extends BaseException {

    private Integer code;

    private Object data;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
