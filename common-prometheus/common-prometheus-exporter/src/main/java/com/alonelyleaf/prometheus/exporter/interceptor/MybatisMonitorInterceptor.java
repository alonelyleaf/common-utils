package com.alonelyleaf.prometheus.exporter.interceptor;

import com.alonelyleaf.prometheus.exporter.util.MeterUtil;
import io.prometheus.client.Histogram;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;

/**
 * Mybatis 监控拦截器
 *
 * @author bijl
 * @date 2021/6/15 下午8:27
 */
@Intercepts(value = {
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class})
})
@Slf4j
@Component
public class MybatisMonitorInterceptor implements Interceptor {

    private static final String UNKNOWN = "unknown";
    private static  final String MYBATIS_TOTAL = "db_total";

    private static final String[]  MYBATIS_COUNTER_LABELS = new String[]{"method", "sqlType", "commandType"};
    private static final String  MYBATIS_COUNTER_HELP = "Total redis request.";
    private static final String[]  MYBATIS_HISTOGRAM_LABELS = new String[]{"method", "sqlType", "commandType"};
    private static final String  MYBATIS_HISTOGRAM_HELP = "Redis request latency in seconds.";

    private static final String READ_MONITOR = "read";
    private static final String WRITE_MONITOR = "write";

    private static final String SELECT_MONITOR = "select";
    private static final String INSERT_MONITOR = "insert";
    private static final String UPDATE_MONITOR = "update";
    private static final String DELETE_MONITOR = "delete";

    private static final String UPDATE = "update";

    /**
     * 定义慢请求的耗时，默认为1000ms，超过1000ms则认为是慢请求
     */
    @Value("${slow.log.db.time:1000}")
    private long executeTimeInMs;

    /**
     * 实现拦截的地方
     */
    @Override
    public Object intercept(final Invocation invocation) throws Throwable {
        return doExecute(invocation);
    }

    private Object doExecute(Invocation invocation) throws Throwable {
        String monitorName = parseMonitor(invocation);
        String sqlTypeMonitor = parseTypeMonitor(invocation);
        String updateTypeMonitor = updateTypeMonitor(invocation);;

        String[] labels = new String[]{monitorName, sqlTypeMonitor, updateTypeMonitor};

        Histogram.Timer histogramRequestTimer = Objects.requireNonNull(MeterUtil.getHistogram(MYBATIS_TOTAL + "_histogram", MYBATIS_HISTOGRAM_LABELS, MYBATIS_HISTOGRAM_HELP))
            .labels(labels)
            .startTimer();

        try {
            return invocation.proceed();
        } catch (Throwable throwable) {

            Objects.requireNonNull(MeterUtil.getCounter(MYBATIS_TOTAL + "_error", MYBATIS_COUNTER_LABELS, MYBATIS_COUNTER_HELP))
                .labels(labels)
                .inc();

            throw throwable;
        } finally {

            Objects.requireNonNull(MeterUtil.getCounter(MYBATIS_TOTAL, MYBATIS_COUNTER_LABELS, MYBATIS_COUNTER_HELP))
                .labels(labels)
                .inc();

            double duration = histogramRequestTimer.observeDuration();
            if (duration * 1000L > getSlowTimeInMs()) {
                Objects.requireNonNull(MeterUtil.getCounter(MYBATIS_TOTAL + "_slow", MYBATIS_COUNTER_LABELS, MYBATIS_COUNTER_HELP))
                    .labels(labels)
                    .inc();
            }
        }
    }

    private String parseTypeMonitor(Invocation invocation) {
        String name = invocation.getMethod().getName();
        return StringUtils.equals(name, UPDATE) ? WRITE_MONITOR : READ_MONITOR;
    }


    private String updateTypeMonitor(Invocation invocation) {
        try{
            String name = invocation.getMethod().getName();
            if (!StringUtils.equals(name, "update")) {
                return SELECT_MONITOR;
            }
            Object arg = invocation.getArgs()[0];
            if (!(arg instanceof MappedStatement)){
                return UNKNOWN;
            }
            MappedStatement statement = (MappedStatement)arg;
            SqlCommandType commandType = statement.getSqlCommandType();

            if (Objects.equals(commandType , SqlCommandType.DELETE)){
                return DELETE_MONITOR;
            }
            if (Objects.equals(commandType , SqlCommandType.UPDATE)){
                return UPDATE_MONITOR;
            }
            if (Objects.equals(commandType , SqlCommandType.INSERT)){
                return INSERT_MONITOR;
            }
            return UNKNOWN;
        }catch (Exception e){
            return UNKNOWN;
        }
    }

    private String parseMonitor(Invocation invocation) {
        try {
            Object target = invocation.getTarget();
            if (!(target instanceof Executor)) {
                return UNKNOWN;
            }

            Object[] args = invocation.getArgs();
            if (args == null || args.length == 0) {
                return UNKNOWN;
            }

            Object arg = invocation.getArgs()[0];
            if (!(arg instanceof MappedStatement)) {
                return UNKNOWN;
            }

            String fullClazz = ((MappedStatement) arg).getId();
            String[] methodSplitter = fullClazz.split("\\.");

            int length = methodSplitter.length;
            if (length < 2) {
                return UNKNOWN;
            }
            return methodSplitter[length - 2] + "#" + methodSplitter[length - 1];
        } catch (Exception e) {
            return UNKNOWN;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    protected long getSlowTimeInMs() {
        return executeTimeInMs;
    }
}
