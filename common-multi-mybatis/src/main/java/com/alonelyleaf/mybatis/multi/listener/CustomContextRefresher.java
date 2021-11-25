package com.alonelyleaf.mybatis.multi.listener;

import com.alibaba.fastjson.JSON;
import com.alonelyleaf.mybatis.multi.properties.MultiProperties;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shardingsphere.core.rule.ShardingRule;
import org.apache.shardingsphere.core.rule.TableRule;
import org.apache.shardingsphere.core.yaml.swapper.ShardingRuleConfigurationYamlSwapper;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.shardingjdbc.jdbc.adapter.AbstractDataSourceAdapter;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.context.MultipleDataSourcesRuntimeContext;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.context.ShardingRuntimeContext;
import org.apache.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;
import org.apache.shardingsphere.shardingjdbc.spring.boot.common.SpringBootPropertiesConfigurationProperties;
import org.apache.shardingsphere.shardingjdbc.spring.boot.sharding.SpringBootShardingRuleConfigurationProperties;
import org.apache.shardingsphere.spring.boot.datasource.DataSourcePropertiesSetterHolder;
import org.apache.shardingsphere.spring.boot.util.DataSourceUtil;
import org.apache.shardingsphere.spring.boot.util.PropertyUtil;
import org.apache.shardingsphere.underlying.common.config.inline.InlineExpressionParser;
import org.apache.shardingsphere.underlying.common.rule.DataNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CustomContextRefresher
 *
 * @author bijianlei
 * @date 2021/11/24
 */
public class CustomContextRefresher extends ContextRefresher implements EnvironmentAware {

    private static final Log log = LogFactory.getLog(CustomContextRefresher.class);

    private static final String SPRING_DATASOURCE_DYNAMIC_DATASOURCE = "spring.datasource.dynamic.datasource";
    private static final String SPRING_DATASOURCE_SHARDINGSPHERE_DATASOURCE = "spring.shardingsphere.datasource";
    private static final String jndiName = "jndi-name";
    public static final String SHARDING = "sharding";

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    private DataSourceCreator dataSourceCreator;

    @Autowired
    private SpringBootShardingRuleConfigurationProperties shardingRule;

    @Autowired
    private SpringBootPropertiesConfigurationProperties props;

    @Autowired
    private Environment environment;

    @Autowired
    private MultiProperties multProperties;

    private List<String> shardingDsName = new ArrayList<>();
    private Map<String, DataSource> shardingDs = new LinkedHashMap<>();
    private String primary;

    public CustomContextRefresher(ConfigurableApplicationContext context, RefreshScope scope, String primary) {
        super(context, scope);
        this.primary = primary;
    }

    public synchronized Set<String> refresh() {

        Set<String> keys = super.refresh();

        // 如果不需要刷新数据源，直接返回
        if (!this.multProperties.isRefresh()) {
            return keys;
        }

        Iterator<String> it = keys.iterator();
        boolean dynamicLoaded = false;
        boolean shardingFlag = true;
        while (true) {
            String key;
            do {
                if (!it.hasNext()) {
                    return keys;
                }

                key = it.next();

                // 重新加载动态数据源
                if (key.contains(SPRING_DATASOURCE_DYNAMIC_DATASOURCE) && !dynamicLoaded) {
                    dynamicLoaded = true;
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

                    this.dynamicRoutingDataSource.setPrimary(this.primary);
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
                }

            } while (!key.contains(SPRING_DATASOURCE_SHARDINGSPHERE_DATASOURCE));

            // 加载分表数据源，由于默认值为 true，首次会默认执行，后续则会跳过
            if (!shardingFlag) {
                continue;
            }
            shardingFlag = false;

            // 清除 sharding 数据源
            Map<String, DataSource> sources = this.dynamicRoutingDataSource.getCurrentDataSources();
            DataSource dataSrc = sources.get(SHARDING);
            if (dataSrc != null) {
                AbstractDataSourceAdapter ds = (AbstractDataSourceAdapter) dataSrc;
                if (!CollectionUtils.isEmpty(this.shardingDsName)) {
                    try {
                        ds.close(this.shardingDsName);
                        ds.getDataSourceMap().clear();
                        this.shardingDs.clear();
                    } catch (Exception var30) {
                        log.error("sharding datasource " + JSON.toJSONString(this.shardingDsName) + " close error:", var30);
                    }
                }
            }

            // 关闭 shardingDs 中的数据源
            if (this.shardingDs.size() > 0) {
                this.shardingDs.values().forEach(this::close);
            }

            this.shardingDsName.clear();
            sources.remove(SHARDING);
            this.setEnvironment(this.environment);
            this.shardingDs.clear();

            try {
                this.genDataSource(this.environment);
                if (this.shardingDs.size() > 0) {
                    DataSource sds = ShardingDataSourceFactory.createDataSource(this.shardingDs,
                        (new ShardingRuleConfigurationYamlSwapper()).swap(this.shardingRule), this.props.getProps());

                    // 处理分表数据源
                    processShardingDataSource((ShardingDataSource) sds, this.props);

                    // 添加到动态数据源中
                    this.dynamicRoutingDataSource.addDataSource(SHARDING, sds);
                }
            } catch (SQLException var31) {
                log.error("sharding datasource " + JSON.toJSONString(this.shardingDsName) + " init error:", var31);
            }
        }
    }

    public static void processShardingDataSource(ShardingDataSource sds, SpringBootPropertiesConfigurationProperties props) {

        boolean flag = false;
        ShardingRule shardingRule = sds.getRuntimeContext().getRule();
        Collection<TableRule> tableRules = shardingRule.getTableRules();
        if (!CollectionUtils.isEmpty(tableRules)) {

            for (TableRule tableRule : tableRules) {
                List<DataNode> oriDataNodes = tableRule.getActualDataNodes();
                if (!CollectionUtils.isEmpty(oriDataNodes) && oriDataNodes.size() == 1
                    && oriDataNodes.get(0).getTableName() != null && oriDataNodes.get(0).getTableName().endsWith("_d")) {

                    try {
                        List<String> dates = genDates(oriDataNodes);
                        if (CollectionUtils.isEmpty(dates)) {
                            throw new RuntimeException(tableRule.getLogicTable() + " actualDataNodes 配置不正确");
                        }

                        List<DataNode> nodes = new ArrayList<>(dates.size());
                        dates.forEach((str) -> {
                            nodes.add(new DataNode(oriDataNodes.get(0).getDataSourceName()
                                .concat(".").concat(tableRule.getLogicTable()).concat("_").concat(str)));
                        });

                        Set<String> actualTables = Sets.newHashSet();
                        Map<DataNode, Integer> dataNodeIndexMap = Maps.newHashMap();
                        AtomicInteger index = new AtomicInteger(0);
                        nodes.forEach((dataNode) -> {
                            actualTables.add(dataNode.getTableName());
                            if (index.intValue() == 0) {
                                dataNodeIndexMap.put(dataNode, 0);
                            } else {
                                dataNodeIndexMap.put(dataNode, index.intValue());
                            }

                            index.incrementAndGet();
                        });
                        Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
                        Field modifiersField = Field.class.getDeclaredField("modifiers");
                        modifiersField.setAccessible(true);
                        modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & -17);
                        actualDataNodesField.setAccessible(true);
                        actualDataNodesField.set(tableRule, nodes);
                        Field actualTablesField = TableRule.class.getDeclaredField("actualTables");
                        actualTablesField.setAccessible(true);
                        actualTablesField.set(tableRule, actualTables);
                        Field dataNodeIndexMapField = TableRule.class.getDeclaredField("dataNodeIndexMap");
                        dataNodeIndexMapField.setAccessible(true);
                        dataNodeIndexMapField.set(tableRule, dataNodeIndexMap);
                        Map<String, Collection<String>> datasourceToTablesMap = Maps.newHashMap();
                        datasourceToTablesMap.put(((DataNode) oriDataNodes.get(0)).getDataSourceName(), actualTables);
                        Field datasourceToTablesMapField = TableRule.class.getDeclaredField("datasourceToTablesMap");
                        datasourceToTablesMapField.setAccessible(true);
                        datasourceToTablesMapField.set(tableRule, datasourceToTablesMap);
                        flag = true;
                    } catch (Exception var29) {
                        throw new RuntimeException(tableRule.getLogicTable() + " actualDataNodes 配置异常", var29);
                    }
                }
            }
        }

        if (flag) {
            try {
                Field ctx = MultipleDataSourcesRuntimeContext.class.getDeclaredField("metaData");
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(ctx, ctx.getModifiers() & -17);
                ctx.setAccessible(true);
                ShardingRuntimeContext cx = new ShardingRuntimeContext(sds.getDataSourceMap(), shardingRule, props.getProps(), sds.getDatabaseType());
                ctx.set(sds.getRuntimeContext(), cx.getMetaData());
                cx.close();
            } catch (Exception var28) {
                log.error("runtimeContext error:", var28);
                throw new RuntimeException("runtimeContext error", var28);
            }
        }
    }


    public static List<String> genDates(List<DataNode> oriDataNodes) throws ParseException {

        String[] rules = oriDataNodes.get(0).getTableName().split("_");
        int year = Integer.parseInt(rules[0]);
        if (year <= 0) {
            year = 2021;
        }

        int mon = Integer.parseInt(rules[1]);
        if (mon <= 0) {
            mon = 1;
        }

        int endYear = Integer.parseInt(rules[2]);
        if (endYear <= 0) {
            endYear = 2040;
        }

        int day = Integer.parseInt(rules[3]);
        if (day <= 7) {
            day = 1;
        }

        if (day >= 7) {
            day = 0;
        }

        return getDayOfWeekWithinDateInterval(String.valueOf(year) + (mon < 10 ? "0" + mon : mon) + "01",
            String.valueOf(endYear) + (mon < 10 ? "0" + mon : mon) + "01", day);
    }

    public static List<String> getDayOfWeekWithinDateInterval(String dataBegin, String dataEnd, int weekDays) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> dateResult = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        String[] dateInterval = new String[]{dataBegin, dataEnd};
        Date[] dates = new Date[dateInterval.length];

        for (int i = 0; i < dateInterval.length; ++i) {
            cal.setTime(sdf.parse(dateInterval[i]));
            dates[i] = cal.getTime();
        }

        for (Date date = dates[0]; date.compareTo(dates[1]) <= 0; date = cal.getTime()) {
            cal.setTime(date);
            if (cal.get(7) - 1 == weekDays) {
                String format = sdf.format(date);
                dateResult.add(format);
            }

            cal.add(5, 1);
        }

        return dateResult;
    }

    public void setEnvironment(Environment environment) {
        String prefix = SPRING_DATASOURCE_SHARDINGSPHERE_DATASOURCE + ".";
        this.shardingDsName = this.getDataSourceNames(environment, prefix);
    }

    private List<String> getDataSourceNames(Environment environment, String prefix) {
        StandardEnvironment standardEnv = (StandardEnvironment) environment;
        standardEnv.setIgnoreUnresolvableNestedPlaceholders(true);
        return standardEnv.getProperty(prefix + "name") == null ? (new InlineExpressionParser(standardEnv.getProperty(prefix + "names"))).splitAndEvaluate() : Collections.singletonList(standardEnv.getProperty(prefix + "name"));
    }

    public final void genDataSource(Environment environment) {
        String prefix = SPRING_DATASOURCE_SHARDINGSPHERE_DATASOURCE + ".";

        for (String dsName : this.getDataSourceNames(environment, prefix)) {
            try {
                this.shardingDs.put(dsName, this.getDataSource(environment, prefix, dsName));
            } catch (ReflectiveOperationException var6) {
                log.error("Can't find datasource type!", var6);
            } catch (NamingException var7) {
                log.error("Can't find JNDI datasource!", var7);
            }
        }
    }

    private DataSource getDataSource(Environment environment, String prefix, String dataSourceName) throws ReflectiveOperationException, NamingException {
        Map dataSourceProps = PropertyUtil.handle(environment, prefix + dataSourceName.trim(), Map.class);
        Preconditions.checkState(!dataSourceProps.isEmpty(), "Wrong datasource properties!");
        if (dataSourceProps.containsKey(jndiName)) {
            return this.getJndiDataSource(dataSourceProps.get(jndiName).toString());
        } else {
            DataSource result = DataSourceUtil.getDataSource(dataSourceProps.get("type").toString(), dataSourceProps);
            DataSourcePropertiesSetterHolder.getDataSourcePropertiesSetterByType(dataSourceProps.get("type").toString()).ifPresent((dataSourcePropertiesSetter) -> {
                dataSourcePropertiesSetter.propertiesSet(environment, prefix, dataSourceName, result);
            });
            return result;
        }
    }

    private DataSource getJndiDataSource(String jndiName) throws NamingException {
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
        bean.setResourceRef(true);
        bean.setJndiName(jndiName);
        bean.setProxyInterface(DataSource.class);
        bean.afterPropertiesSet();
        return (DataSource) bean.getObject();
    }

    private void close(DataSource dataSource) {
        try {
            Method method = dataSource.getClass().getDeclaredMethod("close");
            method.setAccessible(true);
            method.invoke(dataSource);
        } catch (ReflectiveOperationException ignored) {
        }
    }
}

