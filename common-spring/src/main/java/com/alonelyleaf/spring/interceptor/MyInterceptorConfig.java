package com.alonelyleaf.spring.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author bijl
 * @date 2019/7/8
 */
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Bean
    public MyInterceptor myInterceptor(){

        return new MyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(myInterceptor());
    }
}
