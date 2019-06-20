package com.alonelyleaf.spring.filter.impl.access;

import com.alonelyleaf.spring.filter.ExcludeWildcardFilter;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.StringPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * this is the very first filter to record the client request
 */
public class AccessFilter extends ExcludeWildcardFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class.getSimpleName());

    private static final String ACCESS_LOG_NAME = "API_CALL";

    protected boolean recordBodyPart = true;

    protected int slowThreshold = 300;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);

        initParams(filterConfig, this, "recordBodyPart", "slowThreshold");
    }

    @Override
    public void filter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // if we do not need record access log
        if (shouldSkip(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        long start = System.currentTimeMillis();
        AccessLog log = AccessLog.ofDefaults(ACCESS_LOG_NAME, request);

        try {
            if (isPlanContentType(request)) {

                if (shouldRecordBodyPart(request)) {
                    try {
                        // wrap the request for repeat read , if exception occurs use the original
                        request = new RepeatReadHttpServletRequest(request);
                        log.setBody(ServletUtil.readRequestBody(request));
                    } catch (Exception e) {
                        logger.error("record body part error", e);
                    }
                }

                // record the request at first
                handleAccessLog(log);
            }
        } finally {
            filterChain.doFilter(request, response);

            // at last record the access time cross the whole chain
            handleAccessTime(log, System.currentTimeMillis() - start);
        }
    }

    protected boolean isPlanContentType(HttpServletRequest request) {
        return request.getContentType() == null
                || request.getContentType().contains(StringPool.JSON)
                || request.getContentType().contains(StringPool.XML);
    }

    @SuppressWarnings("unused")
    protected boolean shouldRecordBodyPart(HttpServletRequest request) {
        return recordBodyPart;
    }

    protected void handleAccessLog(AccessLog log) {
        logger.info(log.toLogStr());
    }

    protected void handleAccessTime(AccessLog log, long elapsed) {
        if (elapsed > slowThreshold) {
            logger.warn("[ACCESS-SLOW] {} {} costs {} ms", log.getIp(), log.getUrl(), elapsed);
        } else {
            logger.info("[ACCESS] {} {} costs {} ms", log.getIp(), log.getUrl(), elapsed);
        }
    }

    public static class RepeatReadHttpServletRequest extends HttpServletRequestWrapper {

        private byte[] requestBody = null;

        public RepeatReadHttpServletRequest(HttpServletRequest request) {

            super(request);

            try {
                requestBody = StreamUtils.copyToByteArray(request.getInputStream());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        public ServletInputStream getInputStream() throws IOException {
            if (requestBody == null) {
                requestBody = new byte[0];
            }
            final ByteArrayInputStream input = new ByteArrayInputStream(requestBody);
            return new ServletInputStream() {

                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener listener) {
                    // empty
                }

                @Override
                public int read() throws IOException {
                    return input.read();
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
