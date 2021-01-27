package com.alonelyleaf.designpattern.createdmode.abstractfactory.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 代理理类，同时对于使⽤用哪个集群有外部通过⼊入参进⾏行行传递
 *
 * @author bijl
 * @date 2020/12/21 下午7:28
 */
public class JDKProxy {

    public static <T> T getProxy(Class<T> interfaceClass, ICacheAdapter cacheAdapter) throws Exception {

        InvocationHandler handler = new JDKInvocationHandler(cacheAdapter);
        ClassLoader classLoader =
            Thread.currentThread().getContextClassLoader();
        Class<?>[] classes = interfaceClass.getInterfaces();

        return (T) Proxy.newProxyInstance(classLoader, new Class[]{classes[0]}, handler);
    }
}
