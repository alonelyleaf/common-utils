package com.alonelyleaf.spring.filter;

import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.StringPool;
import com.alonelyleaf.util.ValidateUtil;
import jodd.util.Wildcard;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class IncludeWildcardFilter extends AbstractFilter {

    protected String include;

    /**
     * 需要拦截的请求
     */
    protected String[] includeWildcards;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        initParams(filterConfig, this, includeInitParamField());
        if (ValidateUtil.isNotEmpty(include)) {
            includeWildcards = include.split(StringPool.SEMICOLON);
        }
    }

    protected void initParams(FilterConfig filterConfig, Object target, String... parameters) {

        for (String parameter : parameters) {
            String value = filterConfig.getInitParameter(parameter);
            if (value != null) {
                if (includeInitParamField().equals(parameter)) {
                    BeanUtil.declared.setProperty(target, "include", value);
                }
            }
        }
    }

    protected String includeInitParamField() {
        return "include";
    }

    protected boolean shouldFilter(HttpServletRequest request) {
        if (ValidateUtil.isNotEmpty(includeWildcards)) {
            for (String each : includeWildcards) {
                if (Wildcard.match(request.getRequestURI(), each)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean shouldSkip(HttpServletRequest request) {
        return !shouldFilter(request);
    }
}
