package com.alonelyleaf.designpattern.structuremode.decorator.interceptor;

import com.alonelyleaf.designpattern.structuremode.decorator.interceptor.HandlerInterceptor;

public class SsoInterceptor implements HandlerInterceptor {

    public boolean preHandle(String request, String response, Object handler) {
        // 模拟获取cookie
        String ticket = request.substring(1, 8);
        // 模拟校验
        return ticket.equals("success");
    }

}
