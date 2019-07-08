package com.alonelyleaf.spring.schedule.quartzschedule;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author bijl
 * @date 2019/7/8
 */
public class CustomizedJob implements Job {

    @Override
    public void execute(JobExecutionContext var1) throws JobExecutionException{

        //获取自定义值
        String value1 = var1.getJobDetail().getJobDataMap().getString("JobDetailCustomizedKey");
        String value2 = var1.getTrigger().getJobDataMap().getString("TriggerCustomizedKey");
    }
}
