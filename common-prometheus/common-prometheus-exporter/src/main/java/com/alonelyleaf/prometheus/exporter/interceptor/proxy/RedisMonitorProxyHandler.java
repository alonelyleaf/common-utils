package com.alonelyleaf.prometheus.exporter.interceptor.proxy;

import com.alonelyleaf.prometheus.exporter.util.MeterUtil;
import io.prometheus.client.Histogram;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * redis监控代理 Handler
 *
 * @author bijl
 * @date 2021/6/15 下午2:28
 */
public class RedisMonitorProxyHandler<T> implements InvocationHandler {

    public static final String UNKNOWN = "unknown";
    public static final List<String> KEY_PARAM_NAMES = Arrays.asList("key", "lockName");

    private final T target;
    private final String type;
    private final String totalName;
    private final Long slowTimeInMs;
    private final String[] counterLabels;
    private final String counterHelp;
    private final String[] histogramLabels;
    private final String histogramHelp;

    public RedisMonitorProxyHandler(String type, String totalName, T target, Long slowTimeInMs, String[] counterLabels,
                                    String counterHelp, String[] histogramLabels, String histogramHelp) {
        this.type = type;
        this.target = target;
        this.totalName = totalName;
        this.slowTimeInMs = slowTimeInMs;
        this.counterLabels = counterLabels;
        this.counterHelp = counterHelp;
        this.histogramLabels = histogramLabels;
        this.histogramHelp = histogramHelp;
    }

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        String[] labels = buildLabelValues(target, method, args);

        Histogram.Timer histogramRequestTimer = Objects.requireNonNull(MeterUtil.getHistogram(totalName + "_histogram", histogramLabels, histogramHelp))
            .labels(labels)
            .startTimer();

        try {
            return method.invoke(target, args);
        } catch (Throwable throwable) {
            Objects.requireNonNull(MeterUtil.getCounter(totalName + "_error", counterLabels, counterHelp))
                .labels(labels)
                .inc();
            throw throwable;
        } finally {

            Objects.requireNonNull(MeterUtil.getCounter(totalName, counterLabels, counterHelp))
                .labels(labels)
                .inc();

            double duration = histogramRequestTimer.observeDuration();
            if (duration * 1000L > slowTimeInMs) {
                Objects.requireNonNull(MeterUtil.getCounter(totalName + "_slow", counterLabels, counterHelp))
                    .labels(labels)
                    .inc();
            }
        }
    }

    private String[] buildLabelValues(Object instance, Method method, Object[] args) {

        String methodName = type + "#" + instance.getClass().getSimpleName() + "#" + method.getName();

        String key = RedisMonitorProxyHandler.UNKNOWN;
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {

            String[] parameterNames = Arrays.stream(parameters).map(Parameter::getName).toArray(String[]::new);

            for (int i = 0; i < parameterNames.length; i++) {

                // fixme jdk8 以前，编译时会将参数名改为 arg0，导致拿不到key
                if (RedisMonitorProxyHandler.KEY_PARAM_NAMES.contains(parameterNames[i]) && args[i] instanceof String) {
                    key = String.valueOf(args[i]);
                    break;
                }
            }
        }

        return new String[]{methodName, key};
    }
}
