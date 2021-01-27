package com.alonelyleaf.designpattern.structuremode.decorator.interceptor;

public interface HandlerInterceptor {

    boolean preHandle(String request, String response, Object handler);

}
