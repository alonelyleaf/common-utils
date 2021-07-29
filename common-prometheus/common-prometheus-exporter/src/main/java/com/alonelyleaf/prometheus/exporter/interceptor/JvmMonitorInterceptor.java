package com.alonelyleaf.prometheus.exporter.interceptor;

import io.prometheus.client.hotspot.DefaultExports;
import org.springframework.boot.CommandLineRunner;

/**
 * jvm 监控拦截器
 *
 * @author bijl
 * @date 2021/6/8 上午11:17
 */
public class JvmMonitorInterceptor implements CommandLineRunner {

    @Override
    public void run(String... strings) {
        // 添加 Java hotspot 默认监控指标
        DefaultExports.initialize();
    }
}
