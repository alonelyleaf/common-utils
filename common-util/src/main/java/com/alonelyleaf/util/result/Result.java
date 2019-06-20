package com.alonelyleaf.util.result;

import com.alonelyleaf.util.result.error.Error;
/**
 * result结构和dbc返回的结构保持一致
 *
 */
public class Result<T> {

    private long ret;
    private T data;
    private Error error;

    public long getRet() {
        return ret;
    }

    public Result setRet(long ret) {
        this.ret = ret;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public Error getError() {
        return error;
    }

    public Result setError(Error error) {
        this.error = error;
        return this;
    }

    //----------------------helper-------------------

    public Result error(Error error) {
        this.ret = -1;
        this.setError(error);
        return this;
    }

}
