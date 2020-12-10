package com.alonelyleaf.oauth.exception;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;


/**
 * 资源服务器异常自定义捕获
 */
@Component
public class UserOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        BusinessException bu = null;
        if (e instanceof InvalidTokenException) {
            bu = new BusinessException(-1, "refresh_token失效");
        } else {

            bu = new BusinessException(-1, e.getMessage());
        }

        return this.handleOAuth2Exception(new ServerErrorException(bu.getMessage(), bu));
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        HttpHeaders headers = new HttpHeaders();
        UserOAuth2Exception exception = new UserOAuth2Exception(e.getMessage(), e);
        ResponseEntity<OAuth2Exception> response = new ResponseEntity(exception, headers, HttpStatus.valueOf(200));
        return response;
    }


    private static class ServerErrorException extends OAuth2Exception {

        private BusinessException businessException;

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
            this.businessException = (BusinessException) t;
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return businessException.getCode();
        }
    }
}
