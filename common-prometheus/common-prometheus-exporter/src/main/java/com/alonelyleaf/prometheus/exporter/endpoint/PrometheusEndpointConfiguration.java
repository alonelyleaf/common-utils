package com.alonelyleaf.prometheus.exporter.endpoint;

import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;

/**
 * Prometheus 监控端点配置
 *
 * @author bijl
 * @date 2021/5/27 上午10:15
 */
public class PrometheusEndpointConfiguration {

    @Bean
    public PrometheusEndpoint prometheusEndpoint() {
        return new PrometheusEndpoint(CollectorRegistry.defaultRegistry);
    }
}
