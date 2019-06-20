package com.alonelyleaf.spring.gateway;

import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenFinder{

    @Autowired
    private RegisterClient registerClient;

    @Autowired
    private AccessTokenCache accessTokenCache;

    @Value("${spring.application.name}")
    private String application;

    private String channel = "RECORD_NOTICE_CHANNEL";

    public String getAccessToken() {

        String accessToken = accessTokenCache.get();

        if (accessToken != null && accessToken.length() > 0) {

            return accessToken;
        }

        Result<RegisterInfo> result = registerClient.register(application, channel);

        if (ValidateUtil.isNotEmpty(result) && ValidateUtil.isNotEmpty(result.getData())) {
            accessTokenCache.put(result.getData());
            return result.getData().getAccessToken();
        }
        return null;
    }
}
