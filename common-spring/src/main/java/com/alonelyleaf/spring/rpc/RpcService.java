package com.alonelyleaf.spring.rpc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RpcService
 * <p>
 * 一个提供了RPC服务的实现类。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 注解所属接口类型
     */
    Class<?> value();
}
