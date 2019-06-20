package com.alonelyleaf.util.exception.business;


import com.alonelyleaf.util.result.StatusCode;
import com.alonelyleaf.util.result.error.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数不合法
 */
public class BadRequestException extends BusinessException {

    private List<FieldError> fieldErrors;

    public BadRequestException() {
        super(StatusCode.BAD_REQUEST.getCode());
    }

    public BadRequestException(String field, String message) {
        this(StatusCode.BAD_REQUEST.getCode(), field, message);
    }

    public BadRequestException(String field, String message, Object data) {
        this(field, message);
        this.setData(data);
    }

    public BadRequestException(Integer code, String field, String message) {
        super(code, message);
        ensureNotNull();
        fieldErrors.add(new FieldError().setField(field).setMsg(message));
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public BadRequestException setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }

    public BadRequestException addFieldError(FieldError fieldError) {
        ensureNotNull();
        this.fieldErrors.add(fieldError);
        return this;
    }

    private void ensureNotNull() {
        if (this.fieldErrors == null) {
            this.fieldErrors = new ArrayList<FieldError>();
        }
    }
}
