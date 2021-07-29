package com.alonelyleaf.prometheus.exporter.annotation;

import java.lang.annotation.*;

/**
 * 自定义监控注解
 *
 * @author bijl
 * @date 2021/6/7 下午6:51
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {

    /**
     * 监控指标名称，默认 类名#方法名
     */
    String name() default "";
}
