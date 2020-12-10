package com.alonelyleaf.oauth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

public class UserOAuth2Exception extends OAuth2Exception {
    private Integer code = 0;

    public UserOAuth2Exception(String message, Throwable t) {
        super(message, t);
        code = ((OAuth2Exception) t).getHttpErrorCode();
    }

    @Override
    public int getHttpErrorCode() {
        return code;
    }

}
