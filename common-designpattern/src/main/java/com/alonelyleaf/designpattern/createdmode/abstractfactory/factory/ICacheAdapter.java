package com.alonelyleaf.designpattern.createdmode.abstractfactory.factory;

import java.util.concurrent.TimeUnit;

/**
 * @author bijl
 * @date 2020/12/21 下午7:25
 */
public interface ICacheAdapter {

    String get(String key);
    void set(String key, String value);
    void set(String key, String value, long timeout, TimeUnit timeUnit);
    void del(String key);
}
