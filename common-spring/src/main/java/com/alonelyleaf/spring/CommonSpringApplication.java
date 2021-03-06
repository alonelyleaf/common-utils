package com.alonelyleaf.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@ComponentScan("com.alonelyleaf.*")
@EnableCaching
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class CommonSpringApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(CommonSpringApplication.class, args);

        /**
         * 通过调用ApplicationContext.registerShutdownHook()来注册钩子函数，实现bean的destroy，Spring容器的关闭，通过实现这些方法来实现平滑关闭。
         *
         * 注意：当前讨论的都是Spring非Web程序，如果是Web程序的话，不需要我们来注册钩子函数，Spring的Web程序已经有了相关的代码实现优雅关闭了。
         */
        ((AbstractApplicationContext) context).registerShutdownHook();
    }


    /**
     * 异步线程池，通过 @Async("asyncExecutor") 使用
     *
     * @return
     */
    @Bean
    // WARN: DO NOT CHANGE THE METHOD NAME, USED FOR @Async POOL CONFIG
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(40);
        threadPoolTaskExecutor.setMaxPoolSize(1000);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setThreadNamePrefix("async-task");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * Scheduler调度线程池配置
     *
     * @return
     */
    @Bean
    // WARN: DO NOT CHANGE THE METHOD NAME, USED FOR @Schedule POOL CONFIG
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setThreadNamePrefix("Schedule-");
        taskScheduler.setPoolSize(20);
        return taskScheduler;
    }
}
