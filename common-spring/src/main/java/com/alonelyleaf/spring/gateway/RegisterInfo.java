package com.alonelyleaf.spring.gateway;

/**
 * 调用杭分录播服务注册接口返回的注册信息
 *
 */
public class RegisterInfo {

    private String appId;

    private String accessToken;

    private Integer lifetime;

    // ---------------------------- getter setter -------------------------------

    public String getAppId() {
        return appId;
    }

    public RegisterInfo setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public RegisterInfo setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Integer getLifetime() {
        return lifetime;
    }

    public RegisterInfo setLifetime(Integer lifetime) {
        this.lifetime = lifetime;
        return this;
    }
}
