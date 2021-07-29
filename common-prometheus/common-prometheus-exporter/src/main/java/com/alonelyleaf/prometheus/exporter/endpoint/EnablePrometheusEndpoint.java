package com.alonelyleaf.prometheus.exporter.endpoint;

import com.alonelyleaf.prometheus.exporter.interceptor.MonitorInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 Prometheus 监控端点
 *
 * @author bijl
 * @date 2021/5/27 上午10:13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({PrometheusEndpointConfiguration.class, MonitorInterceptorConfig.class})
public @interface EnablePrometheusEndpoint {
}
