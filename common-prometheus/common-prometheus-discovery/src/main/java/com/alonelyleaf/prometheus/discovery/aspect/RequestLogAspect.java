package com.alonelyleaf.prometheus.discovery.aspect;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Spring boot 控制器 请求日志，方便代码调试
 *
 * @author bijl
 */
@Slf4j
@Aspect
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class RequestLogAspect {

    /**
     * AOP 环切 控制器
     *
     * @param point JoinPoint
     * @return Object
     * @throws Throwable 异常
     */
    @Around(
        "@within(org.springframework.stereotype.Controller) || " +
            "@within(org.springframework.web.bind.annotation.RestController)) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)"
    )
    public Object aroundApi(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();

        // 构建成一条长 日志，避免并发下日志错乱
        StringBuilder beforeReqLog = new StringBuilder(300);
        // 日志参数
        List<Object> beforeReqArgs = new ArrayList<>();
        beforeReqLog.append("\n\n================  Request Start  ================\n");
        // 打印路由
        beforeReqLog.append("===> {}: {}?{}");
        beforeReqArgs.add(requestMethod);
        beforeReqArgs.add(requestUrl);
        beforeReqArgs.add(request.getQueryString());
        log.info(beforeReqLog.toString(), beforeReqArgs.toArray());

        // 打印请求参数
        // 打印请求 headers
        beforeReqLog.append("================   Request End   ================\n");

        // 打印执行时间
        long startNs = System.nanoTime();
        // aop 执行后的日志
        StringBuilder afterReqLog = new StringBuilder(200);
        // 日志参数
        List<Object> afterReqArgs = new ArrayList<>();
        afterReqLog.append("\n\n================  Response Start  ================\n");
        try {
            Object result = point.proceed();

            // 打印返回结构体
            afterReqLog.append("===Result===  {}\n");
            afterReqArgs.add(JSON.toJSONString(result));

            return result;
        } finally {
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            afterReqLog.append("<=== {}: {} ({} ms)\n");
            afterReqArgs.add(requestMethod);
            afterReqArgs.add(requestUrl);
            afterReqArgs.add(tookMs);
            afterReqLog.append("================   Response End   ================\n");
            log.info(afterReqLog.toString(), afterReqArgs.toArray());
        }
    }

}
