package com.alonelyleaf.prometheus.exporter.interceptor;

import com.alonelyleaf.prometheus.exporter.rocketmq.annotation.MessageListener;
import com.alonelyleaf.prometheus.exporter.annotation.Monitor;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 用于判断抛出异常的接口是否需要增加异常监控记录
 *
 * <p>
 *     由集成该组件的各服务实现接口{@link #shouldMonitor(Throwable)} 来判断，
 *     该接口在{@link BaseAopMonitorInterceptor#execute(ProceedingJoinPoint, Monitor, MessageListener, String)}
 *     catch 异常时进行判断。
 * </p>
 *
 * @author bijl
 * @date 2021/6/8 上午11:22
 */
public interface CheckException {

    /**
     * 是否应该增加异常监控记录
     *
     * @param throwable 抛出的异常
     * @return T： 上报error 日志
     */
    boolean shouldMonitor(Throwable throwable);
}
