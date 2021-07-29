package com.alonelyleaf.prometheus.exporter.interceptor;

import com.alonelyleaf.prometheus.exporter.annotation.Monitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;

/**
 * 自定义注解 监控拦截器
 *
 * @author bijl
 * @date 2021/6/7 下午6:56
 */
@Aspect
public class AnnotationMonitorInterceptor extends BaseAopMonitorInterceptor {

    private static final String MONITOR_TOTAL = "monitor_total";
    private static final String[] MONITOR_COUNTER_LABELS = new String[]{"method"};
    private static final String MONITOR_COUNTER_HELP = "Total monitor.";
    private static final String[] MONITOR_HISTOGRAM_LABELS = new String[]{"method"};
    private static final String MONITOR_HISTOGRAM_HELP = "Monitor latency in seconds.";

    /**
     * 定义慢请求的耗时，默认为1000ms，超过1000ms则认为是慢请求
     */
    @Value("${slow.log.monitor.time:1000}")
    private long executeTimeInMs;

    @Pointcut("@annotation(com.alonelyleaf.prometheus.exporter.annotation.Monitor)")
    public void monitorPointcut() {
    }

    @Around("monitorPointcut() && @annotation(monitor)")
    public Object monitorAround(ProceedingJoinPoint joinPoint, Monitor monitor) throws Throwable {
        return execute(joinPoint, monitor, null, MONITOR_TOTAL);
    }

    @Override
    protected long getSlowTimeInMs() {
        return executeTimeInMs;
    }

    @Override
    protected String[] getCounterLabels() {
        return MONITOR_COUNTER_LABELS;
    }

    @Override
    protected String getCounterHelp() {
        return MONITOR_COUNTER_HELP;
    }

    @Override
    protected String[] getHistogramLabels() {
        return MONITOR_HISTOGRAM_LABELS;
    }

    @Override
    protected String getHistogramHelp() {
        return MONITOR_HISTOGRAM_HELP;
    }
}
