package com.alonelyleaf.spring.filter.impl.authenticate;

import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bijl
 * @date 2019/6/19
 */
public class TokenUtil {

    /**
     * 获得请对对应的token，可能为空
     *
     * @param request
     * @return
     */
    public static Token getToken(HttpServletRequest request) {

        //
        return null;
    }


    /**
     * 刷新当前请求对应的token
     */
    public static void touchToken() {
        touchToken(ServletUtil.getCurrentRequest());
    }

    /**
     * 刷新请求对应的token
     *
     * @param request
     */
    public static void touchToken(HttpServletRequest request) {

        //根据请求获取tokenId，进而获取token信息，并刷新过期时间;
    }

    public class Token{

        private String id;
        private Long timeout;

        public String getId() {
            return id;
        }

        public Token setId(String id) {
            this.id = id;
            return this;
        }

        public Long getTimeout() {
            return timeout;
        }

        public Token setTimeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }
    }
}
