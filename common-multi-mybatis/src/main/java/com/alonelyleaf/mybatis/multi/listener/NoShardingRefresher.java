package com.alonelyleaf.mybatis.multi.listener;

import com.alonelyleaf.mybatis.multi.properties.MultiProperties;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * NoShardingRefresher
 *
 * @author bijianlei
 * @date 2021/11/24
 */
public class NoShardingRefresher extends ContextRefresher {

    private static final Log log = LogFactory.getLog(NoShardingRefresher.class);

    private static final String SPRING_DATASOURCE_DYNAMIC_DATASOURCE = "spring.datasource.dynamic.datasource";
    public static final String SHARDING = "sharding";

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    private DataSourceCreator dataSourceCreator;

    @Autowired
    private MultiProperties multiProperties;

    public NoShardingRefresher(ConfigurableApplicationContext context, RefreshScope scope) {
        super(context, scope);
    }

    public synchronized Set<String> refresh() {
        Set<String> keys = super.refresh();

        if (this.multiProperties.isRefresh()) {
            Iterator<String> it = keys.iterator();

            // 检查是否包含动态数据源配置
            String key;
            while (true) {
                if (!it.hasNext()) {
                    return keys;
                }

                key = it.next();

                // 重新加载动态数据源
                if (key.contains(SPRING_DATASOURCE_DYNAMIC_DATASOURCE)) {

                    // 移除旧数据源
                    Map<String, DataSource> sources = this.dynamicRoutingDataSource.getCurrentDataSources();
                    for (String oldDs : sources.keySet()) {
                        if (!oldDs.equalsIgnoreCase(SHARDING)) {
                            try {
                                this.dynamicRoutingDataSource.removeDataSource(oldDs);
                            } catch (Exception e) {
                                log.error("dynamic datasource refresh old " + oldDs + " close error:", e);
                            }
                        }
                    }

                    // 添加新数据源
                    this.dynamicRoutingDataSource.setPrimary(this.dynamicDataSourceProperties.getPrimary());
                    Map<String, DataSourceProperty> datasourceMap = this.dynamicDataSourceProperties.getDatasource();
                    if (CollectionUtils.isEmpty(datasourceMap)) {
                        return keys;
                    }

                    datasourceMap.forEach((k, v) -> {
                        try {
                            DataSource dataSource = this.dataSourceCreator.createDataSource(v);
                            this.dynamicRoutingDataSource.addDataSource(StringUtils.isEmpty(v.getPoolName()) ? k : v.getPoolName(), dataSource);
                        } catch (Exception e) {
                            log.error("dynamic datasource refresh addDataSource " + v.getPoolName() + " error:", e);
                        }
                    });

                    return keys;
                }
            }
        }

        return keys;
    }
}
