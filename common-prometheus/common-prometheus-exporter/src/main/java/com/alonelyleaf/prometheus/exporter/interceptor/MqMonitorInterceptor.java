package com.alonelyleaf.prometheus.exporter.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;

/**
 * MQ 监控拦截器
 *
 * @author bijl
 * @date 2021/6/8 上午10:42
 */
@Aspect
public class MqMonitorInterceptor extends BaseAopMonitorInterceptor {

    private static final String MQ_TOTAL = "mq_consumer_total";
    private static final String[] MQ_COUNTER_LABELS = new String[]{"method"};
    private static final String MQ_COUNTER_HELP = "Total mq consumer.";
    private static final String[] MQ_HISTOGRAM_LABELS = new String[]{"method"};
    private static final String MQ_HISTOGRAM_HELP = "Mq consumer latency in seconds.";

    /**
     * 定义慢请求的耗时，默认为1000ms，超过1000ms则认为是慢请求
     */
    @Value("${slow.log.mq.time:1000}")
    private long executeTimeInMs;

    @Pointcut("@annotation(com.alonelyleaf.core.rocketmq.annotation.MessageListener)")
    public void mqPointcut() {
    }

    @Around("mqPointcut() && @annotation(messageListener)")
    public Object controllerAround(ProceedingJoinPoint joinPoint, MessageListener messageListener) throws Throwable {
        return execute(joinPoint, null, messageListener, MQ_TOTAL);
    }

    @Override
    protected long getSlowTimeInMs() {
        return executeTimeInMs;
    }

    @Override
    protected String[] getCounterLabels() {
        return MQ_COUNTER_LABELS;
    }

    @Override
    protected String getCounterHelp() {
        return MQ_COUNTER_HELP;
    }

    @Override
    protected String[] getHistogramLabels() {
        return MQ_HISTOGRAM_LABELS;
    }

    @Override
    protected String getHistogramHelp() {
        return MQ_HISTOGRAM_HELP;
    }
}
