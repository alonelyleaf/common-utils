package com.alonelyleaf.spring.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/6/13
 */
@Component
public class ScheduleTaskDemo {

    //FIXME 集群环境下，定时任务需要考虑是否应该加锁

    @Scheduled(cron = "0 * * * * ?")
    private void execCron(){

    }

    //两次任务间隔时间，单位ms
    @Scheduled(fixedRate = 1000)
    private void execRate(){

    }
}
