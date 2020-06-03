package com.alonelyleaf.spring.filter.impl.authenticate;

import com.alonelyleaf.spring.common.i18n.Message;
import com.alonelyleaf.spring.filter.ExcludeWildcardFilter;

import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.result.Result;
import com.alonelyleaf.util.result.StatusCode;
import com.alonelyleaf.util.result.error.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * 登录认证过滤器
 *
 * @author christ
 * @date 2016/6/8
 */
public class AuthenticateFilter extends ExcludeWildcardFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final String AUTHENTICATED_SIGN = AuthenticateFilter.class.getName() + "_AUTHENTICATED_SIGN";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
    }

    @Override
    protected String excludeInitParamField() {
        return "anonymous";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //如果当前请求是匿名请求则放过
        if (shouldSkip(request)) {
            authenticateIt(request);
            executeChain(request, response, chain);
            return;
        }

        //如果请求有已验证标识则放过
        if (hasAuthenticatedSign(request)) {
            executeChain(request, response, chain);
            return;
        }

        if (isAuthenticateFail(request)) {
            refuse(request, response);
            return;
        }

        executeChain(request, response, chain);
    }

    protected boolean isAuthenticateFail(ServletRequest request) {
        return !tokenValidate(request);
    }

    /**
     * @param request
     * @return
     */
    protected boolean tokenValidate(ServletRequest request) {
        return ValidateUtil.isNotEmpty(TokenUtil.getToken(ServletUtil.getRequest(request)));
    }

    /**
     * 判断当前请求是否已认证
     *
     * @param request
     * @return
     */
    protected boolean hasAuthenticatedSign(ServletRequest request) {
        if (ValidateUtil.isNotEmpty(request.getAttribute(AUTHENTICATED_SIGN))) {
            return true;
        }
        return false;
    }

    /**
     * 认证通过
     *
     * @param request
     */
    protected void authenticateIt(ServletRequest request) {
        request.setAttribute(AUTHENTICATED_SIGN, AuthenticateFilter.class);
    }

    /**
     * 拒绝
     *
     * @param response
     * @throws IOException
     */
    protected void refuse(ServletRequest request, ServletResponse response) throws IOException {
        logger.info("AUTHENTICATE REFUSE >> " + ServletUtil.getRequestURI(request));

        ServletUtil.jsonOut(response, new Result().error(new Error().setMsg(Message.ServiceCommon.unauthorized)
                .setErrorCode(StatusCode.UNAUTHORIZED)));
    }
}
