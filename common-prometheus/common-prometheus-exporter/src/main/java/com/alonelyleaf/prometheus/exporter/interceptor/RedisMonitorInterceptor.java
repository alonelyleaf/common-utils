package com.alonelyleaf.prometheus.exporter.interceptor;

import com.alonelyleaf.prometheus.exporter.interceptor.proxy.RedisMonitorProxyHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis 监控拦截器
 * <p>
 * 对 {@link RedisTemplate} 所有方法进行拦截，如果返回结果为 redis Operations，则需要对其方法进行代理。
 *
 * @author bijl
 * @date 2021/6/15 下午2:25
 */
@Aspect
public class RedisMonitorInterceptor extends BaseAopMonitorInterceptor {

    public static final String REDIS_TOTAL = "redis_total";

    private static final String[]  REDIS_COUNTER_LABELS = new String[]{"method", "key"};
    private static final String  REDIS_COUNTER_HELP = "Total redis request.";
    private static final String[]  REDIS_HISTOGRAM_LABELS = new String[]{"method", "key"};
    private static final String  REDIS_HISTOGRAM_HELP = "Redis request latency in seconds.";

    private final Map<Object, Object> proxyInstanceMaps = new ConcurrentHashMap<>();
    private static final Map<Class, OperationConfig> classTypeMaps = new ConcurrentHashMap<>();

    /**
     * 定义慢请求的耗时，默认为1000ms，超过1000ms则认为是慢请求
     */
    @Value("${slow.log.redis.time:1000}")
    private long executeTimeInMs;

    @Pointcut("execution(* org.springframework.data.redis.core.RedisTemplate..*(..))")
    public void redisMonitorPointcut() {
    }

    @Around("redisMonitorPointcut()")
    public Object redisMonitorAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isNeedProxy(joinPoint)) {
            Object proceed = joinPoint.proceed();
            return proxyObject(joinPoint, proceed);
        } else {
            return execute(joinPoint, null, null, REDIS_TOTAL);
        }
    }

    private Object proxyObject(ProceedingJoinPoint joinPoint, Object proceed) {
        Object proxy = proxyInstanceMaps.get(proceed);
        if (proxy != null) {
            return proxy;
        }

        proxy = createProxyObject(joinPoint, proceed);
        if (isCache(joinPoint)) {
            proxyInstanceMaps.put(proceed, proxy);
        }

        return proxy;
    }

    private Object createProxyObject(ProceedingJoinPoint joinPoint, Object proceed) {
        OperationConfig config = getConfig(joinPoint);

        InvocationHandler handler = new RedisMonitorProxyHandler<>(config.type, REDIS_TOTAL, proceed, getSlowTimeInMs(),
            getCounterLabels(), getCounterHelp(), getHistogramLabels(), getHistogramHelp());
        return Proxy.newProxyInstance(proceed.getClass().getClassLoader(), new Class<?>[]{config.clazz}, handler);
    }

    private boolean isCache(ProceedingJoinPoint joinPoint) {
        return getConfig(joinPoint).cacheInstance;
    }

    /**
     * 判断方法是否需要代理，如果返回结果为 redis Operations，则需要对其方法进行代理
     */
    private boolean isNeedProxy(ProceedingJoinPoint joinPoint) {
        return getConfig(joinPoint) != null;
    }

    private OperationConfig getConfig(ProceedingJoinPoint joinPoint) {
        return classTypeMaps.get(((MethodSignature) joinPoint.getSignature()).getReturnType());
    }

    @Override
    protected long getSlowTimeInMs() {
        return executeTimeInMs;
    }

    @Override
    protected String[] getCounterLabels() {
        return  REDIS_COUNTER_LABELS;
    }

    @Override
    protected String getCounterHelp() {
        return  REDIS_COUNTER_HELP;
    }

    @Override
    protected String[] getCounterLabelValues(ProceedingJoinPoint joinPoint, Object result, Throwable throwable, String monitorName) {

        String key = getRedisKey(joinPoint);

        return new String[]{monitorName, key};
    }

    @Override
    protected String[] getHistogramLabels() {
        return  REDIS_HISTOGRAM_LABELS;
    }

    @Override
    protected String getHistogramHelp() {
        return  REDIS_HISTOGRAM_HELP;
    }

    @Override
    protected String[] getHistogramLabelValues(ProceedingJoinPoint joinPoint, String monitorName) {

        String key = getRedisKey(joinPoint);

        return new String[]{monitorName, key};
    }

    private String getRedisKey(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String key = RedisMonitorProxyHandler.UNKNOWN;
        String[] parameterNames = pnd.getParameterNames(method);
        if (parameterNames != null && parameterNames.length > 0) {

            for (int i = 0; i < parameterNames.length; i++) {
                // fixme jdk8 以前，编译时会将参数名改为 arg0，导致拿不到key
                if (RedisMonitorProxyHandler.KEY_PARAM_NAMES.contains(parameterNames[i]) && args[i] instanceof String) {
                    key = String.valueOf(args[i]);
                    break;
                }
            }
        }

        return key;
    }

    static {
        classTypeMaps.put(ValueOperations.class, new OperationConfig("String", true, ValueOperations.class));
        classTypeMaps.put(ListOperations.class, new OperationConfig("List", true, ListOperations.class));
        classTypeMaps.put(SetOperations.class, new OperationConfig("Set", true, SetOperations.class));
        classTypeMaps.put(ZSetOperations.class, new OperationConfig("ZSet", true, ZSetOperations.class));
        classTypeMaps.put(GeoOperations.class, new OperationConfig("Geo", true, GeoOperations.class));
        classTypeMaps.put(HyperLogLogOperations.class, new OperationConfig("HyperLogLog", true, HyperLogLogOperations.class));
        classTypeMaps.put(HashOperations.class, new OperationConfig("Hash", false, HashOperations.class));

        classTypeMaps.put(BoundKeyOperations.class, new OperationConfig("String", false, BoundKeyOperations.class));
        classTypeMaps.put(BoundValueOperations.class, new OperationConfig("String", false, BoundValueOperations.class));
        classTypeMaps.put(BoundGeoOperations.class, new OperationConfig("Geo", false, BoundGeoOperations.class));
        classTypeMaps.put(BoundHashOperations.class, new OperationConfig("Hash", false, BoundHashOperations.class));
        classTypeMaps.put(BoundListOperations.class, new OperationConfig("List", false, BoundListOperations.class));
        classTypeMaps.put(BoundSetOperations.class, new OperationConfig("Set", false, BoundSetOperations.class));
        classTypeMaps.put(BoundZSetOperations.class, new OperationConfig("ZSet", false, BoundZSetOperations.class));
    }

    private static class OperationConfig {
        private final String type;
        private final boolean cacheInstance;
        private final Class clazz;

        OperationConfig(String type, boolean cacheInstance, Class clazz) {
            this.type = type;
            this.cacheInstance = cacheInstance;
            this.clazz = clazz;
        }
    }
}
