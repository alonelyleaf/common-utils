package com.alonelyleaf.spring.handler.banner;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 个人中心页广告图 handler
 *
 * @author bijl
 * @date 2021/2/24 上午11:24
 */
@Component
public class PersonalBannerHandler implements IBannerHandler {

    @Override
    public List<Integer> supportTypeList() {

        return Arrays.asList(4);
    }

    @Override
    public void myMethod(String param) {

        // 逻辑处理
    }
}
