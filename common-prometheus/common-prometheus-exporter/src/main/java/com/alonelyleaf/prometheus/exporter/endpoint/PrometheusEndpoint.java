package com.alonelyleaf.prometheus.exporter.endpoint;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;

/**
 * Prometheus 监控端点
 * <p>
 * 暴露接口给外部获取监控信息
 *
 * @author bijl
 * @date 2021/5/27 上午10:16
 */
@Endpoint(id = "prometheus")
public class PrometheusEndpoint {

    private final CollectorRegistry collectorRegistry;

    PrometheusEndpoint(CollectorRegistry collectorRegistry) {
        this.collectorRegistry = collectorRegistry;
    }

    @ReadOperation
    public String invoke() {

        return this.writeRegistry(Collections.emptySet(), TextFormat.CONTENT_TYPE_004);
    }

    public String writeRegistry(Set<String> metricsToInclude, String contentType) {
        try {
            Writer writer = new StringWriter();
            TextFormat.writeFormat(contentType, writer, this.collectorRegistry.filteredMetricFamilySamples(metricsToInclude));
            return writer.toString();
        } catch (IOException var4) {
            throw new RuntimeException("Writing metrics failed", var4);
        }
    }
}

