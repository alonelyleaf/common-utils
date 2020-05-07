package com.alonelyleaf.spring.filter.impl.frequency;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2020/4/24
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "ratelimit.config")
public class RateLimitConfig {

    private String sync;

    private String batchSync;

    public String getSync() {
        return sync;
    }

    public RateLimitConfig setSync(String sync) {
        this.sync = sync;
        return this;
    }

    public String getBatchSync() {
        return batchSync;
    }

    public RateLimitConfig setBatchSync(String batchSync) {
        this.batchSync = batchSync;
        return this;
    }
}
