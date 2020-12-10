package com.alonelyleaf.oauth.service;

import lombok.SneakyThrows;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户信息
 *
 * @author bijl
 * @date 2020/11/25 下午5:34
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Override
    @SneakyThrows
    public User loadUserByUsername(String username) {

        // 通过username信息查询用户信息，进行返回

        // 返回用户信息，可自定义 继承 User返回额外的信息
        List<String> roles = new ArrayList<>();
        roles.add("");

        return new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", roles)));
    }

}
