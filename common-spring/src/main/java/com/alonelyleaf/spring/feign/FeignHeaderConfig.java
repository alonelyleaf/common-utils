package com.alonelyleaf.spring.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * feign请求调用时传递请求头
 *
 * @author bijl
 * @date 2019/6/10
 */
@Configuration
public class FeignHeaderConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = servletRequestAttributes.getRequest();
        Map<String, Collection<String>> headerMap = new HashMap<>();
        //获取你需要传递的头信息
        headerMap.put("Cookie", Arrays.asList(req.getHeader("Cookie")));
        headerMap.put("platform", Arrays.asList(req.getHeader("platform")));
        //feign请求时，便可携带上该信息
        requestTemplate.headers(headerMap);
    }
}
