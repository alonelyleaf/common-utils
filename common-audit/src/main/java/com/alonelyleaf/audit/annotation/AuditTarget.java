package com.alonelyleaf.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bijl
 * @date 2019/11/28
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditTarget {

    /**
     * 用于接口参数
     * 标记操作对象
     *
     * @return
     */
    String field() default "";
}
