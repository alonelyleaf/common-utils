package com.alonelyleaf.spring.filter.config;

import com.alonelyleaf.spring.filter.SimpleFilterDemo;
import com.alonelyleaf.spring.filter.impl.access.AccessFilter;
import com.alonelyleaf.spring.filter.impl.authenticate.AuthenticateFilter;
import com.alonelyleaf.spring.filter.impl.authenticate.TokenTouchFilter;
import com.alonelyleaf.spring.filter.impl.frequency.FrequencyFilter;
import com.alonelyleaf.spring.filter.impl.frequency.RateLimitConfig;
import com.alonelyleaf.util.MapUtil;
import com.alonelyleaf.util.StringPool;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Map;

/**
 * @author bijl
 * @date 2019/6/19
 */
@org.springframework.context.annotation.Configuration
public class FilterConfiguration {

    @Value("${access.log.exclude:}")
    private String exclude;

    @Autowired
    private RateLimitConfig rateLimitConfig;

    /**
     * 频率控制
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean freqencyFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(); //新建bean
        bean.setOrder(0); //设置执行顺序
        bean.setFilter(new FrequencyFilter()); //设置过滤器
        bean.setName(AccessFilter.class.getName()); //设置名称
        bean.addUrlPatterns("/api/v*"); //要进行过滤的url
        bean.addInitParameter("url", StringUtil.join(frequencyLimit().keySet(), StringPool.SEMICOLON)); //初始化参数
        bean.addInitParameter("limit", StringUtil.join(frequencyLimit().values(), StringPool.SEMICOLON)); //初始化参数
        return bean;
    }

    /**
     * 访问日志
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean accessLogFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(); //新建bean
        bean.setOrder(1); //设置执行顺序
        bean.setFilter(new AccessFilter()); //设置过滤器
        bean.setName(AccessFilter.class.getName()); //设置名称
        bean.addUrlPatterns("/*"); //要进行过滤的url
        bean.addInitParameter("exclude", exclude.toString()); //初始化参数
        return bean;
    }

    /**
     * 需要登录接口过滤器
     *
     * @return
     */
    @Bean
    FilterRegistrationBean authenticateFilter() {

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setOrder(2);
        bean.setFilter(new AuthenticateFilter());
        bean.setName(AuthenticateFilter.class.getName());
        bean.addUrlPatterns("/conference/api/*"); //要进行过滤的url
        bean.addInitParameter("anonymous", StringUtil.join(anonymousUrls(), ";"));//初始化参数
        return bean;
    }

    /**
     * 简单实例，演示在filter中使用@Autowired转配bean时，如何注册filter到过滤器链
     *
     * 1.先将该过滤器类装配成 bean
     * 2.使用DelegatingFilterProxy来代理此过滤器
     * 3.配置过滤器生命周期
     *
     * @return
     */
    @Bean
    FilterRegistrationBean simpleFilterDemoFilter() {

        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setOrder(3);
        //DelegatingFilterProxy其作用就是一个filter的代理，使用它通过spring容器来管理filter的生命周期
        bean.setFilter(new DelegatingFilterProxy("simpleFilterDemo"));
        //servlet容器将通过该代理相应的调用来控制目标Filter的生命周期，默认为false
        bean.addInitParameter("targetFilterLifecycle", "true");
        bean.setName(AuthenticateFilter.class.getName());
        bean.addUrlPatterns("/conference/api/*"); //要进行过滤的url
        bean.addInitParameter("anonymous", StringUtil.join(anonymousUrls(), ";"));//初始化参数
        return bean;
    }

    @Bean
    public SimpleFilterDemo simpleFilterDemo(){

        return new SimpleFilterDemo();
    }

    /**
     * Token刷新
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean tokenTouchFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setOrder(Integer.MAX_VALUE);
        bean.setFilter(new TokenTouchFilter());
        bean.setName(TokenTouchFilter.class.getName());
        bean.addUrlPatterns("/*"); //要进行过滤的url
        return bean;
    }

    private String[] anonymousUrls() {

        return new String[]{
                "/conference/api/v?/external/*",
                "/conference/api/v?/fragile/*",
                "/conference/api/v?/internal/*"
        };
    }

    /**
     * 配置请求在指定的时间内最大请求次数
     *
     * @return
     */
    private Map<String, String> frequencyLimit() {

        return MapUtil.asMap(
                "/api/v?/external/phonebook/sync", rateLimitConfig.getSync(),
                "/api/v?/external/phonebook/batchSync", rateLimitConfig.getBatchSync()
        );
    }
}
