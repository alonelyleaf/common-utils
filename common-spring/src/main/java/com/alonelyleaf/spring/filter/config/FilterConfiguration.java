package com.alonelyleaf.spring.filter.config;

import com.alonelyleaf.spring.filter.impl.access.AccessFilter;
import com.alonelyleaf.spring.filter.impl.authenticate.AuthenticateFilter;
import com.alonelyleaf.spring.filter.impl.authenticate.TokenTouchFilter;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bijl
 * @date 2019/6/19
 */
@Configuration
public class FilterConfiguration {

    @Value("${access.log.pretty:false}")
    private Boolean pretty;

    /**
     * 访问日志
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean accessLogFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setOrder(1);
        bean.setFilter(new AccessFilter());
        bean.setName(AccessFilter.class.getName());
        bean.addUrlPatterns("/*");
        bean.addInitParameter("pretty", pretty.toString());
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
        bean.addUrlPatterns("/conference/api/*");
        bean.addInitParameter("anonymous", StringUtil.join(anonymousUrls(), ";"));
        return bean;
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
        bean.addUrlPatterns("/*");
        return bean;
    }

    private String[] anonymousUrls() {

        return new String[]{
                "/conference/api/v?/external/*",
                "/conference/api/v?/fragile/*",
                "/conference/api/v?/internal/*"
        };
    }
}
