package com.alonelyleaf.spring.schedule.quartzschedule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 *  定时任务框架Quartz-(一)Quartz入门与Demo搭建
 *
 * https://blog.csdn.net/noaman_wgs/article/details/80984873
 *
 * @author bijl
 * @date 2019/7/8
 */
public class CustomizedScheduler {

    private static Logger logger = LoggerFactory.getLogger(CustomizedScheduler.class);

    public void createScheduler() throws InterruptedException, SchedulerException {

        // 1、创建调度器Scheduler
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();

        // 2、创建JobDetail实例，并与PrintWordsJob类绑定(Job执行内容)
        JobDetail jobDetail = JobBuilder.newJob(CustomizedJob.class)
                .withIdentity("job1", "group1") //配置job的id，包括名称和所属的组
                .usingJobData("JobDetailCustomizedKey", "I am JobDetail customizedValue!") //配置自定义信息，以便在job中使用
                .build();

        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + 5000);

        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 5000);


        // 3、构建Trigger实例,每隔1s执行一次
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "triggerGroup1")//配置trigger的id，包括名称和所属的组
                .usingJobData("TriggerCustomizedKey", "I am trigger customizedValue!")
                .startNow()//配置生效时间，此为立即生效，也可通过startAt(Date triggerStartTime)配置为某时间开始生效
                .startAt(startDate)
                .endAt(endDate) //配置结束时间
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1)//每隔1s执行一次
                        .repeatForever())//设置执行次数，一直执行
               //.withSchedule(CronScheduleBuilder.cronSchedule(conStr)) //还可以通过cron表达式，配置定时任务，例如 conStr = "0 0 0 * * ?"
                .build();

        //4、配置定时任务并执行
        scheduler.scheduleJob(jobDetail, trigger);
       logger.info("--------scheduler start ! ------------");
        scheduler.start();

        //睡眠以等待执行
        TimeUnit.MINUTES.sleep(1);
        scheduler.shutdown();
       logger.info("--------scheduler shutdown ! ------------");

    }
}
