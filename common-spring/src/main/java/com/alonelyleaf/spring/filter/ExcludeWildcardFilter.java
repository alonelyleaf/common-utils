package com.alonelyleaf.spring.filter;

import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.StringPool;
import com.alonelyleaf.util.ValidateUtil;
import jodd.util.Wildcard;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;

public class ExcludeWildcardFilter extends AbstractFilter {

    protected String exclude;

    /**
     * 不需要拦截的请求
     */
    protected String[] excludeWildcards;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initParams(filterConfig, this, excludeInitParamField());
        if (ValidateUtil.isNotEmpty(exclude)) {
            excludeWildcards = exclude.split(StringPool.SEMICOLON);
        }
    }

    protected void initParams(FilterConfig filterConfig, Object target, String... parameters) {

        for (String parameter : parameters) {
            String value = filterConfig.getInitParameter(parameter);
            if (value != null) {
                if (excludeInitParamField().equals(parameter)) {
                    BeanUtil.declared.setProperty(target, "exclude", value);
                }
            }
        }
    }

    protected String excludeInitParamField() {
        return "exclude";
    }

    protected boolean shouldSkip(ServletRequest request) {
        if (ValidateUtil.isNotEmpty(excludeWildcards)) {
            for (String each : excludeWildcards) {
                if (Wildcard.match(ServletUtil.getRequestURI(request), each)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean shouldFilter(ServletRequest request) {
        return !shouldSkip(request);
    }
}
