package com.alonelyleaf.util.result.error;

import com.alonelyleaf.util.result.StatusCode;

import java.util.ArrayList;
import java.util.List;

public class Error {

    private String msg;
    private int errorCode;
    private List<FieldError> fieldErrors = new ArrayList<>();

    public String getMsg() {
        return msg;
    }

    public Error setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Error setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public Error setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }

    //----------------------helper-------------------

    public Error setErrorCode(StatusCode code) {
        this.errorCode = code.getCode();
        return this;
    }
}