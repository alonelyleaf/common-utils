package com.alonelyleaf.mybatis.multi.config;

import com.alonelyleaf.mybatis.multi.logback.LogProperties;
import com.alonelyleaf.mybatis.multi.logback.LogbackContainer;
import com.alonelyleaf.mybatis.multi.properties.MultiProperties;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Mybatis 自动配置
 *
 * @author bijianlei
 * @date 2021/11/24
 */
@Configuration
@EnableConfigurationProperties({LogProperties.class, MultiProperties.class})
public class MybatisAutoConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    public MybatisAutoConfig() {
    }

    @Bean
    @ConditionalOnMissingBean
    public LogbackContainer getLogbackContainer(LogProperties logProperties) {
        LogbackContainer logbackContainer = new LogbackContainer();
        logbackContainer.setApplicationName(this.applicationName);
        logbackContainer.start(logProperties);
        return logbackContainer;
    }

    @RefreshScope
    @ConfigurationProperties("spring.datasource.dynamic")
    @Primary
    @Bean
    public DynamicDataSourceProperties dynamicDataSourceProperties() {
        return new DynamicDataSourceProperties();
    }

    @RefreshScope
    @ConfigurationProperties(prefix = "alonelyleaf.slow.sql.log")
    @Primary
    @Bean
    public LogProperties logProperties() {
        return new LogProperties();
    }

    @RefreshScope
    @ConfigurationProperties(prefix = "alonelyleaf.datasource")
    @Primary
    @Bean
    public MultiProperties multiProperties() {
        return new MultiProperties();
    }
}

