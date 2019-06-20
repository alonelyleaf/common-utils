package com.alonelyleaf.spring.gateway;

import feign.Response;
import feign.Util;

import java.io.IOException;

public class ErrorDecoder implements feign.codec.ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return errorStatus(methodKey, response);
    }

    private static Exception errorStatus(String methodKey, Response response) {
        String body = null;
        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader());
            }
        } catch (IOException ignored) { // NOPMD
        }

        return new Exception(response.reason());
    }
}