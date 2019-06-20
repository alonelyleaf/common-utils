package com.alonelyleaf.spring.filter.impl.access;

import com.alonelyleaf.spring.common.constant.Constant;
import com.alonelyleaf.util.QueryStringUtil;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.StringPool;
import com.alonelyleaf.util.ValidateUtil;

import jodd.json.meta.JSON;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static jodd.util.StringPool.*;


public class AccessLog {

    /**
     * log 名称
     */
    private String name;

    /**
     * http method
     */
    private String method;

    /**
     * 调用的url
     */
    private String url;

    /**
     * 调用者ip
     */
    private String ip;

    /**
     * 调用参数
     */
    private String params;

    /**
     * body部分
     */
    private String body;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 访问时间点
     */
    private long accessDate;

    /**
     * 当前登录用户
     */
    private Object userId;

    /**
     * 其他属性
     */
    private Map attrs = new HashMap();

    public String getName() {
        return name;
    }

    public AccessLog setName(String name) {
        this.name = name;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public AccessLog setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AccessLog setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public AccessLog setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getParams() {
        return params;
    }

    public AccessLog setParams(String params) {
        this.params = params;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public AccessLog setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public long getAccessDate() {
        return accessDate;
    }

    public AccessLog setAccessDate(long accessDate) {
        this.accessDate = accessDate;
        return this;
    }

    public Map getAttrs() {
        return attrs;
    }

    public AccessLog setAttrs(Map attrs) {
        this.attrs = attrs;
        return this;
    }

    public Object getUserId() {
        return userId;
    }

    public AccessLog setUserId(Object userId) {
        this.userId = userId;
        return this;
    }

    public AccessLog addAttr(String key, Object value) {
        this.attrs.put(key, value);
        return this;
    }

    public String getBody() {
        return body;
    }

    public AccessLog setBody(String body) {
        this.body = body;
        return this;
    }

    @JSON(include = false)
    public String toLogStr() {

        StringBuilder sb = new StringBuilder();
        sb.append(NEWLINE).append("--------------[").append(name).append("]--------------").append(NEWLINE);
        sb.append("url: ").append(method).append(StringPool.SPACE).append(url).append(NEWLINE);
        if (ValidateUtil.isNotEmpty(params)) sb.append("params : ").append(params).append(NEWLINE);
        sb.append("time: ").append(accessDate).append(NEWLINE);
        sb.append("ip: ").append(ip).append(NEWLINE);
        if (userAgent != null) sb.append("ua: ").append(userAgent).append(NEWLINE);
        if (userId != null) sb.append("userId: ").append(userId).append(NEWLINE);

        if (ValidateUtil.isNotEmpty(attrs)) {
            Set<Map.Entry> entry = attrs.entrySet();
            for (Map.Entry each : entry) {
                if (each.getValue() != null) {
                    sb.append(each.getKey()).append(COLON).append(SPACE).append(each.getValue()).append(NEWLINE);
                }
            }
        }

        if (ValidateUtil.isNotEmpty(body)) sb.append("body: ").append(body).append(NEWLINE);

        sb.append("--------------[/").append(name).append("]--------------").append(NEWLINE);
        return sb.toString();
    }

    //---------------------------static-------------------------

    public static AccessLog ofDefaults(String name, ServletRequest request) {
        return new AccessLog().setName(name)
                .setUrl(ServletUtil.getRequestURI(request))
                .setParams(QueryStringUtil.getQueryString(request.getParameterMap()))
                .setMethod(ServletUtil.getMethod(request))
                .setUserAgent(ServletUtil.getRequest(request).getHeader("User-Agent"))
                .setAccessDate(System.currentTimeMillis())
                .addAttr(StringPool.ACCEPT_ENCODING, ServletUtil.getHeader(StringPool.ACCEPT_ENCODING))
                .addAttr(Constant.Header.TOKEN_FIELD, ServletUtil.getHeader(Constant.Header.TOKEN_FIELD))
                .addAttr(Constant.Header.X_FROM, ServletUtil.getHeader(Constant.Header.X_FROM))
                .setIp(ServletUtil.getClientIP());
    }
}
