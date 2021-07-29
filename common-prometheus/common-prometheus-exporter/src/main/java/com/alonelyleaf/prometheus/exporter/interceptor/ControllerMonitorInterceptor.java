package com.alonelyleaf.prometheus.exporter.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;


/**
 * controller 监控拦截器
 *
 * @author bijl
 * @date 2021/6/8 上午10:43
 */
@Aspect
public class ControllerMonitorInterceptor extends BaseAopMonitorInterceptor {

    private static final String CONTROLLER_TOTAL = "controller_total";
    private static final String[] CONTROLLER_COUNTER_LABELS = new String[]{"path", "code"};
    private static final String CONTROLLER_COUNTER_HELP = "Total requests.";
    private static final String[] CONTROLLER_HISTOGRAM_LABELS = new String[]{"path"};
    private static final String CONTROLLER_HISTOGRAM_HELP = "Request latency in seconds.";
    private static final String CONTROLLER_DEFAULT_CODE = "0";
    private static final String CONTROLLER_ERROR_CODE = "500";

    /**
     * 定义慢请求的耗时，默认为1000ms，超过1000ms则认为是慢请求
     */
    @Value("${slow.log.controller.time:1000}")
    private long executeTimeInMs;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return execute(joinPoint, null, null, CONTROLLER_TOTAL);
    }

    @Override
    protected long getSlowTimeInMs() {
        return executeTimeInMs;
    }

    @Override
    protected String[] getCounterLabels() {
        return CONTROLLER_COUNTER_LABELS;
    }

    @Override
    protected String getCounterHelp() {
        return CONTROLLER_COUNTER_HELP;
    }

    @Override
    protected String[] getCounterLabelValues(ProceedingJoinPoint joinPoint, Object result, Throwable throwable, String monitorName) {

        String code = CONTROLLER_DEFAULT_CODE;
        if (result instanceof R) {
            code = String.valueOf(((R)result).getCode());
        } else if (throwable != null){
            code = CONTROLLER_ERROR_CODE;
        }

        return new String[]{monitorName, code};
    }

    @Override
    protected String[] getHistogramLabels() {
        return CONTROLLER_HISTOGRAM_LABELS;
    }

    @Override
    protected String getHistogramHelp() {
        return CONTROLLER_HISTOGRAM_HELP;
    }
}
