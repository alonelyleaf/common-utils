package com.alonelyleaf.mybatis.multi.config;

import com.alonelyleaf.mybatis.multi.listener.NoShardingRefresher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 不分表配置
 *
 * @author bijianlei
 * @date 2021/11/24
 */
@Configuration
@ConditionalOnMissingClass({"org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource"})
public class NoShardingConfig {

    public NoShardingConfig() {
    }

    @Bean
    @Primary
    public ContextRefresher customNoShardingRefresher(@Autowired ConfigurableApplicationContext context, @Autowired RefreshScope scope) {
        return new NoShardingRefresher(context, scope);
    }
}
