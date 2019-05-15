package com.alonelyleaf.guava.eventbus.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author bijl
 * @date 2019/5/15
 */
@Configuration
public class EventBusConfiguration {

    @Bean
    public AsyncEventBus asyncEventBus() {
        return new AsyncEventBus(asyncExecutor());
    }

    /**
     * 异步线程池
     *
     * @return
     */
    @Bean
    // WARN: DO NOT CHANGE THE METHOD NAME, USED FOR @Async POOL CONFIG
    // SEE (CoreModule.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(20);
        threadPoolTaskExecutor.setMaxPoolSize(1000);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setThreadNamePrefix("async-task");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
