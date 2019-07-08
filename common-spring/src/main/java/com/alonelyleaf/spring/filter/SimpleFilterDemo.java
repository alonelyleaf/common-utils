package com.alonelyleaf.spring.filter;

import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;
import jodd.util.Wildcard;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author bijl
 * @date 2019/7/8
 */
public class SimpleFilterDemo implements Filter {

    protected String anonymous;

    /**
     * 不需要验证的请求
     */
    private String[] anonymousWildcards;

    @Autowired
    private FilterDealWithService filterDealWithService;

    public void init(FilterConfig filterConfig) throws ServletException {

        initParams(filterConfig, this, "anonymous", "authenticate");
        if (ValidateUtil.isNotEmpty(anonymous)) {
            anonymousWildcards = anonymous.split(";");
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException{

        //如果当前请求是匿名请求则放过
        if (isAnonymous(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        //进行处理
        filterDealWithService.dealWith();

        filterChain.doFilter(request, response);
    }

    /**
     * 读取配置参数
     *
     * @param filterConfig
     * @param target
     * @param parameters
     */
    public static void initParams(FilterConfig filterConfig, Object target, String... parameters) {

        for (String parameter : parameters) {
            String value = filterConfig.getInitParameter(parameter);

            if (value != null) {
                BeanUtil.declared.setProperty(target, parameter, value);
            }
        }
    }

    protected boolean isAnonymous(ServletRequest request) {
        if (ValidateUtil.isNotEmpty(anonymousWildcards)) {
            for (String each : anonymousWildcards) {
                if (Wildcard.match(ServletUtil.getRequestURI(request), each)) {
                    return true;
                }
            }
        }
        return false;
    }

}
