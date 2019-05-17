package com.alonelyleaf.netty.api.service;


import com.alonelyleaf.netty.api.listener.Listener;

/**
 * 可提供服务的公用接口
 *
 */
public interface Service {

    void start(Listener listener);

    void stop(Listener listener);

    void init();
}
