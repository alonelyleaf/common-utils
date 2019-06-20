package com.alonelyleaf.util.exception.business;

import com.alonelyleaf.util.result.StatusCode;

/**
 * 接口调用格式异常,一般属于接口调用者的问题
 */
public class ProtocolNotMatchException extends BadRequestException {

    public ProtocolNotMatchException() {
        super();
        setCode(StatusCode.PROTOCOL_NOT_MATCH);
    }

    public ProtocolNotMatchException(String field, String message) {
        super(field, message);
        setCode(StatusCode.PROTOCOL_NOT_MATCH);
    }

    public ProtocolNotMatchException(String field, String message, Object data) {
        this(field, message);
        this.setData(data);
    }

    public ProtocolNotMatchException(Integer code, String field, String message) {
        super(code, field, message);
        setCode(StatusCode.PROTOCOL_NOT_MATCH);
    }
}
