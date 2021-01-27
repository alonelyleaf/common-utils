package com.alonelyleaf.designpattern.createdmode.abstractfactory.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 *
 * @author bijl
 * @date 2020/12/21 下午7:26
 */
public class JDKInvocationHandler implements InvocationHandler {

    private ICacheAdapter cacheAdapter;

    public JDKInvocationHandler(ICacheAdapter cacheAdapter) {
        this.cacheAdapter = cacheAdapter;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?>[] classes = (Class<?>[]) Arrays.stream(args).map(Object::getClass).toArray();

        return ICacheAdapter.class.getMethod(method.getName(), classes).invoke(cacheAdapter, args);
    }
}
