package com.alonelyleaf.spring.gateway;


import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 录播服务accessToken缓存
 */
@Component
public class AccessTokenCache{

    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";

    private static final ConcurrentHashMap<String, String> accessKeyMap = new ConcurrentHashMap<>();

    public String get() {

        return accessKeyMap.get(ACCESS_TOKEN_KEY);
    }

    public void put(RegisterInfo registerInfo) {

        if (registerInfo == null) {
            return;
        }

        accessKeyMap.putIfAbsent(ACCESS_TOKEN_KEY, registerInfo.getAccessToken());
    }
}
