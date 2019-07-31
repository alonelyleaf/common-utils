package com.alonelyleaf.http.httpclient;

import java.io.Serializable;

/**
 * @author bijl
 * @date 2019/7/5
 */
public class HttpClientResult implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private String content;

    public HttpClientResult(int code){

        this.code = code;
    }

    public HttpClientResult(int code, String content){

        this.code = code;
        this.content = content;
    }
}
