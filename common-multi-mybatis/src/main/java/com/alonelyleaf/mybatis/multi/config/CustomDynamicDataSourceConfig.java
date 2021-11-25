package com.alonelyleaf.mybatis.multi.config;

import com.alonelyleaf.mybatis.multi.listener.CustomContextRefresher;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.apache.shardingsphere.shardingjdbc.spring.boot.common.SpringBootPropertiesConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

/**
 * CustomDynamicDataSourceConfig
 *
 * @author bijianlei
 * @date 2021/11/24
 */
@Configuration
@ConditionalOnClass({ShardingDataSource.class})
@AutoConfigureBefore({DynamicDataSourceAutoConfiguration.class})
@AutoConfigureAfter({SpringBootConfiguration.class})
public class CustomDynamicDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(CustomDynamicDataSourceConfig.class);
    public static final String SHARDING = "sharding";

    @Resource
    private DynamicDataSourceProperties properties;

    @Autowired
    @Lazy
    private ShardingDataSource shardingDataSource;

    @Autowired
    private SpringBootPropertiesConfigurationProperties props;

    public CustomDynamicDataSourceConfig() {
    }

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {

        final Map<String, DataSourceProperty> datasourceMap = this.properties.getDatasource();

        return new AbstractDataSourceProvider() {

            public Map<String, DataSource> loadDataSources() {

                Map<String, DataSource> dataSourceMap = this.createDataSourceMap(datasourceMap);

                if (dataSourceMap.containsKey(SHARDING)) {
                    throw new RuntimeException("sharding datasource repeat");
                } else {
                    CustomContextRefresher.processShardingDataSource(CustomDynamicDataSourceConfig.this.shardingDataSource, CustomDynamicDataSourceConfig.this.props);

                    dataSourceMap.put(SHARDING, CustomDynamicDataSourceConfig.this.shardingDataSource);
                    return dataSourceMap;
                }
            }
        };
    }

    @Primary
    @Bean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(this.properties.getDatasource().containsKey(this.properties.getPrimary()) ? this.properties.getPrimary() : SHARDING);
        dataSource.setStrict(this.properties.getStrict());
        dataSource.setStrategy(this.properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(this.properties.getP6spy());
        dataSource.setSeata(this.properties.getSeata());
        return dataSource;
    }

    @RefreshScope
    @ConfigurationProperties(prefix = "spring.shardingsphere.sharding")
    @Primary
    @Bean
    public SpringBootShardingRuleConfigurationProperties shardingRule() {
        return new SpringBootShardingRuleConfigurationProperties();
    }

    @RefreshScope
    @ConfigurationProperties(prefix = "spring.shardingsphere")
    @Primary
    @Bean
    public SpringBootPropertiesConfigurationProperties props() {
        return new SpringBootPropertiesConfigurationProperties();
    }

    @Bean
    @Primary
    public ContextRefresher customContextRefresher(@Autowired ConfigurableApplicationContext context,
                                                   @Autowired org.springframework.cloud.context.scope.refresh.RefreshScope scope) {
        String primary = this.properties.getDatasource().containsKey(this.properties.getPrimary()) ? this.properties.getPrimary() : SHARDING;
        return new CustomContextRefresher(context, scope, primary);
    }
}

