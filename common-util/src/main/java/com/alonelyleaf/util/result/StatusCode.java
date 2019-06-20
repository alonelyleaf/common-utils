package com.alonelyleaf.util.result;

public enum StatusCode {

    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    CONFLICT(409),
    PRECONDITION_FAILED(412),
    UNSUPPORTED_MEDIA_TYPE(415),
    PROTOCOL_NOT_MATCH(444),
    INTERNAL_ERROR(500),
    GATEWAY_ERROR(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public StatusCode setCode(Integer code) {
        this.code = code;
        return this;
    }
}
