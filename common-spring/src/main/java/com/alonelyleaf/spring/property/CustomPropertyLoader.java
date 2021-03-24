package com.alonelyleaf.spring.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 手动加载自定义配置文件
 *
 * @author bijl
 * @date 2021/3/4 下午4:10
 */
@PropertySource(value = {"classpath:mytest.yml"}, encoding = "utf-8")
@Service
public class CustomPropertyLoader {

    @Value("${alonelyleaf.name}")
    private String xxx;
}