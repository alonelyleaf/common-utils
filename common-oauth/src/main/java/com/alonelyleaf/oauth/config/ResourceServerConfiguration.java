package com.alonelyleaf.oauth.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 资源服务器配置
 *
 */
@Configuration
@AllArgsConstructor
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    /**
     * 自定义登录成功处理器
     */
    private AuthenticationSuccessHandler appLoginInSuccessHandler;

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http.headers().frameOptions().disable();
        http.formLogin()
                .successHandler(appLoginInSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/actuator/**",
                        "/token/**",
                        "/oauth/captcha",
                        "/oauth/logout",
                        "/mobile/**",
                        "/v2/api-docs",
                        "/v2/api-docs-ext").permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable();
    }

}
