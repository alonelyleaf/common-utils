package com.alonelyleaf.prometheus.exporter.interceptor;

import com.alonelyleaf.prometheus.exporter.annotation.MessageListener;
import com.alonelyleaf.prometheus.exporter.annotation.Monitor;
import com.alonelyleaf.prometheus.exporter.util.MeterUtil;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Objects;

/**
 * 基础AOP监控拦截器
 *
 * @author bijl
 * @date 2021/6/7 下午6:57
 */
@Slf4j
public abstract class BaseAopMonitorInterceptor implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    protected Object execute(final ProceedingJoinPoint joinPoint, final Monitor monitor, final MessageListener messageListener, final String allMonitor) throws Throwable {
        return doExecute(joinPoint, monitor, messageListener, allMonitor);
    }

    private Object doExecute(ProceedingJoinPoint joinPoint, Monitor monitor, MessageListener messageListener, String allMonitor) throws Throwable {

        String monitorName = buildMonitorName(joinPoint, monitor, messageListener);

        Histogram.Timer histogramRequestTimer = Objects.requireNonNull(MeterUtil.getHistogram(allMonitor + "_histogram", getHistogramLabels(), getHistogramHelp()))
            .labels(getHistogramLabelValues(joinPoint, monitorName))
            .startTimer();

        Object result;
        String[] labels = new String[]{monitorName};
        try {
            result = joinPoint.proceed();
            labels = getCounterLabelValues(joinPoint, result, null, monitorName);
        } catch (Throwable throwable) {

            labels = getCounterLabelValues(joinPoint, null, throwable, monitorName);

            Map<String, CheckException> checkExceptionMap = applicationContext.getBeansOfType(CheckException.class);
            if (checkExceptionMap.values().stream().allMatch(t -> t.shouldMonitor(throwable))) {

                Objects.requireNonNull(MeterUtil.getCounter(allMonitor + "_error", getCounterLabels(), getCounterHelp()))
                    .labels(labels)
                    .inc();
            }

            throw throwable;
        } finally {

            Objects.requireNonNull(MeterUtil.getCounter(allMonitor, getCounterLabels(), getCounterHelp()))
                .labels(labels)
                .inc();

            double duration = histogramRequestTimer.observeDuration();
            if (duration * 1000L > getSlowTimeInMs()) {
                Objects.requireNonNull(MeterUtil.getCounter(allMonitor + "_slow", getCounterLabels(), getCounterHelp()))
                    .labels(labels)
                    .inc();
            }
        }

        return result;
    }

    protected String buildMonitorName(ProceedingJoinPoint joinPoint, Monitor monitor, MessageListener messageListener) {
        if (monitor != null && StringUtils.isNotEmpty(monitor.name())) {
            return monitor.name();
        }

        if (messageListener != null && StringUtils.isNotEmpty(messageListener.tag())) {
            return messageListener.tag();
        }

        return joinPoint.getSignature().getDeclaringType().getSimpleName()
                + "#" + joinPoint.getSignature().getName();
    }

    protected abstract long getSlowTimeInMs();

    protected abstract String[] getCounterLabels();

    protected abstract String getCounterHelp();

    protected String[] getCounterLabelValues(ProceedingJoinPoint joinPoint, Object result, Throwable throwable, String monitorName) {
        return new String[]{monitorName};
    }

    protected abstract String[] getHistogramLabels();

    protected abstract String getHistogramHelp();

    protected String[] getHistogramLabelValues(ProceedingJoinPoint joinPoint, String monitorName) {
        return new String[]{monitorName};
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BaseAopMonitorInterceptor.applicationContext = applicationContext;
    }

}
