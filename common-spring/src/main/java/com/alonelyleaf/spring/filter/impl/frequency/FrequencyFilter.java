package com.alonelyleaf.spring.filter.impl.frequency;

import com.alonelyleaf.spring.common.i18n.Message;
import com.alonelyleaf.spring.filter.AbstractFilter;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.result.Result;
import com.alonelyleaf.util.result.StatusCode;
import com.alonelyleaf.util.result.error.Error;
import com.google.common.util.concurrent.RateLimiter;
import jodd.util.StringPool;
import jodd.util.Wildcard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author bijl
 * @date 2020/4/24
 */
public class FrequencyFilter extends AbstractFilter {

    protected String url;

    protected String limit;

    private String[] urlWildcards;

    protected Map<String, RateLimiter> rateLimiterMap;

    private static final Logger logger = LoggerFactory.getLogger(FrequencyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        initParams(filterConfig, this, "url", "limit");

        if (ValidateUtil.isEmpty(url) && ValidateUtil.isEmpty(limit)) {
            return;
        }

        urlWildcards = url.split(StringPool.SEMICOLON);
        List<Integer> limits = convert2Int(limit);

        rateLimiterMap = new HashMap<>();
        for (int i = 0; i < Math.min(urlWildcards.length, limits.size()); i++) {
            rateLimiterMap.put(urlWildcards[i], buildLimiter(limits.get(i)));
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        RateLimiter limiter = rateLimiterMap.get(matchUrlWildcard(servletRequest));

        // 只对配置的请求进行限制
        if (ValidateUtil.isNotEmpty(limiter)) {

            boolean acquired = limiter.tryAcquire();

            if (!acquired) {
                refuse(servletRequest, servletResponse);
                return;
            }
        }

        executeChain(servletRequest, servletResponse, filterChain);
    }


    private String matchUrlWildcard(ServletRequest request) {

        if (ValidateUtil.isEmpty(url)) {
            return StringPool.EMPTY;
        }

        for (String each : urlWildcards) {
            if (Wildcard.match(ServletUtil.getRequestURI(request), each)) {
                return each;
            }
        }

        return StringPool.EMPTY;
    }

    private RateLimiter buildLimiter(Integer limit) {

        return RateLimiter.create(limit);
    }

    private List<Integer> convert2Int(String str) {

        if (ValidateUtil.isEmpty(str)) {
            return new ArrayList<>();
        }

        java.util.List<java.lang.Integer> integerList = new ArrayList<>();
        for (String string : str.split(StringPool.SEMICOLON)) {
            if (!ValidateUtil.isNum(string)) {
                integerList.add(100);
            }

            integerList.add(java.lang.Integer.valueOf(string));
        }

        return integerList;
    }

    /**
     * 拒绝
     *
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void refuse(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        logger.info("ACQUIRE PERMISSION REFUSE >> ip:{}, url:{}", ServletUtil.getClientIP(), ServletUtil.getRequestURI(request));

        Random random = new Random();
        int after = random.nextInt(3) + 1;
        if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).addHeader("Retry-After", String.valueOf(after));
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        ServletUtil.jsonOut(response, new Result().error(new Error()
                .setMsg(Message.ServiceCommon.waitTimeout)
                .setErrorCode(StatusCode.SERVICE_UNAVAILABLE))
        );
    }
}
