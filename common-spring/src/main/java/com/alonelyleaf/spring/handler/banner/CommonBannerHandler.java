package com.alonelyleaf.spring.handler.banner;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 通用广告图 handler
 *
 * @author bijl
 * @date 2021/2/24 上午11:06
 */
@Component
public class CommonBannerHandler implements IBannerHandler {

    @Override
    public List<Integer> supportTypeList() {

        return Arrays.asList(1, 2, 3);
    }

    @Override
    public void myMethod(String param) {

        // 逻辑处理
    }
}
