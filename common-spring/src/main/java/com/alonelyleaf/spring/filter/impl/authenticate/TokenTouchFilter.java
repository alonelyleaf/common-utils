package com.alonelyleaf.spring.filter.impl.authenticate;


import com.alonelyleaf.spring.filter.AbstractFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * token刷新过滤器
 *
 */
public class TokenTouchFilter extends AbstractFilter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        TokenUtil.touchToken();
        executeChain(request, response, chain);
    }
}
