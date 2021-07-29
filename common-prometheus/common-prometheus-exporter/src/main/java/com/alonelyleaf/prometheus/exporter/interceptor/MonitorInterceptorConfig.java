package com.alonelyleaf.prometheus.exporter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 *  监控拦截器配置
 *
 * @author bijl
 * @date 2021/6/7 下午6:47
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.alonelyleaf.prometheus.exporter.interceptor"})
public class MonitorInterceptorConfig {

    @Resource
    private MybatisMonitorInterceptor mybatisMonitorInterceptor;

    /**
     * 注入controller的监控拦截器
     */
    @Bean
    public ControllerMonitorInterceptor controllerInterceptor() {
        return new ControllerMonitorInterceptor();
    }

    /**
     * 注入注解（@Monitor）的监控拦截器
     */
    @Bean
    public AnnotationMonitorInterceptor monitorInterceptor() {
        return new AnnotationMonitorInterceptor();
    }

    /**
     * 注入mq的监控拦截器
     */
    @Bean
    public MqMonitorInterceptor mqInterceptor() {
        return new MqMonitorInterceptor();
    }

    /**
     * 注入jvm的监控拦截器
     */
    @Bean
    public JvmMonitorInterceptor jvmMonitorInterceptor() {
        return new JvmMonitorInterceptor();
    }

    /**
     * 注入redis的监控拦截器
     */
    @Bean
    public RedisMonitorInterceptor redisMonitorInterceptor() {
        return new RedisMonitorInterceptor();
    }

    /**
     * 注入Mybatis的监控拦截器
     */
    @Bean
    public Interceptor[] mybatisInterceptor(List<ApplicationContext> applicationContexts) {

        //存在一个项目有多个数据源的情况，需要获取所有的SqlSessionFactory，并进行注入
        for (ApplicationContext context : applicationContexts) {
            try {
                //获取mybatis SqlSessionFactory，检查是否使用mybatis
                String[] beanNames = context.getBeanNamesForType(SqlSessionFactory.class);
                if (beanNames == null || beanNames.length == 0) {
                    continue;
                }
                for (String beanName : beanNames) {
                    SqlSessionFactory bean = context.getBean(beanName, SqlSessionFactory.class);
                    if (bean != null) {
                        bean.getConfiguration().addInterceptor(mybatisMonitorInterceptor);
                    }
                }
            } catch (Exception e) {
                log.error("注入mybatis拦截器失败，请检查。 如未使用mybatis，请忽略", e);
            }
        }

        return new Interceptor[]{mybatisMonitorInterceptor};
    }
}
