# 基于mybatis的多数据源功能

## 1.功能

- 可配置多个数据库连接
- 打通nacos，可动态变更数据库连接配置
- 增加shardingjdbc支持
- 支持读写分离配置(sharding情况下、无sharding情况下)
- 代码层通过注解切换数据源
- 记录慢sql日志并埋点(可配置告警)，配置： alonelyleaf.slow.sql.log.time 配置慢操作阈值，单位毫秒，默认500

## 2.引入方法(不带分库分表)

```xml

<dependency>
    <groupId>com.alonelyleaf</groupId>
    <artifactId>common-multi-mybatis</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

启动类添加自动加载排除@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})

### 2.1 数据库配置

```yaml

# 默认数据源，如果配置，默认master
spring.datasource.dynamic.primary=master
  # 下面五项配置含义：定义了一个数据源名称为master的连接池，多个数据源修改名称即可
spring.datasource.dynamic.datasource.master.url=jdbc:mysql://10.210.112.3:3307/message_center?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.datasource.dynamic.datasource.master.username=msg_center
spring.datasource.dynamic.datasource.master.password=nTwb0aRM6XDFJXsx
spring.datasource.dynamic.datasource.master.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.dynamic.datasource.master.driver-class-name=com.mysql.jdbc.Driver
  # 以下是通用配置
spring.datasource.dynamic.druid.maxActive=50
spring.datasource.dynamic.druid.initialSize=0
spring.datasource.dynamic.druid.maxWait=60000
spring.datasource.dynamic.druid.minIdle=1
spring.datasource.dynamic.druid.timeBetweenEvictionRunsMillis=60000
spring.datasource.dynamic.druid.minEvictableIdleTimeMillis=300000
spring.datasource.dynamic.druid.validationQuery=SELECT 1
spring.datasource.dynamic.druid.testWhileIdle=true
spring.datasource.dynamic.druid.testOnBorrow=false
spring.datasource.dynamic.druid.testOnReturn=false
spring.datasource.dynamic.druid.poolPreparedStatements=true
spring.datasource.dynamic.druid.maxOpenPreparedStatements=20
spring.datasource.dynamic.druid.keepAlive=true
spring.datasource.dynamic.druid.connectProperties=socketTimeout\=3000;connectTimeout\=1200

spring.datasource.dynamic.druid.filters=

  # 关闭数据源配置修改自动刷新
sftc.tech.datasource.refresh=false
```

上图配置中的 master 是数据源名称，多个数据源修改名称即可，这个名称将用于代码中的数据源切换。

相关配置以及不分库分表的读写分离配置参考： [https://github.com/baomidou/dynamic-datasource-spring-boot-starter](https://github.com/baomidou/dynamic-datasource-spring-boot-starter)

## 3.分库分表

```xml

<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
</dependency>
```

### 3.1数据库配置

注意:如果使用分库分表，2.1中的配置可以去掉

分表配置示例：

```yaml
# 分库分表数据源名称，例如下面配置的ds0；如果多个：ds0,ds1
spring.shardingsphere.datasource.names=ds0
  # ds0数据源配置，多个的话修改ds0再配置一份
spring.shardingsphere.datasource.ds0.type=com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.ds0.driver-class-name=com.mysql.jdbc.Driver
spring.shardingsphere.datasource.ds0.url=jdbc:mysql://127.0.0.1:3306/message_center?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.shardingsphere.datasource.ds0.username=name
spring.shardingsphere.datasource.ds0.password=password

spring.shardingsphere.datasource.ds0.maxActive=100
spring.shardingsphere.datasource.ds0.initialSize=0
spring.shardingsphere.datasource.ds0.maxWait=60000
spring.shardingsphere.datasource.ds0.minIdle=1
spring.shardingsphere.datasource.ds0.timeBetweenEvictionRunsMillis=60000
spring.shardingsphere.datasource.ds0.minEvictableIdleTimeMillis=300000
spring.shardingsphere.datasource.ds0.validationQuery=SELECT 1
spring.shardingsphere.datasource.ds0.testWhileIdle=true
spring.shardingsphere.datasource.ds0.testOnBorrow=false
spring.shardingsphere.datasource.ds0.testOnReturn=false
spring.shardingsphere.datasource.ds0.poolPreparedStatements=true
spring.shardingsphere.datasource.ds0.maxOpenPreparedStatements=20
spring.shardingsphere.datasource.ds0.keepAlive=true
spring.shardingsphere.datasource.ds0.connectProperties=socketTimeout\=3000;connectTimeout\=1200

spring.shardingsphere.datasource.ds0.filters=

  # 分表规则
spring.shardingsphere.sharding.tables.user_message_relation.actual-data-nodes=ds0.user_message_relation_$->{0..9}_20$->{20..30}$->{['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']}
spring.shardingsphere.sharding.tables.user_message_relation.table-strategy.complex.algorithm-class-name=com.alonelyleaf.maple.utils.UserMessageRelationTableComplexKeysShardingAlgorithm
spring.shardingsphere.sharding.tables.user_message_relation.table-strategy.complex.sharding-columns=user_id,create_time
spring.shardingsphere.sharding.binding-tables=user_message_relation
spring.shardingsphere.props.sql.show=false
spring.shardingsphere.props.executor.size=32
spring.shardingsphere.props.max.connections.size.per.query=4
```

上述配置只是分表示例，分表配置规则参考：
[https://shardingsphere.apache.org/document/legacy/4.x/document/cn/manual/sharding-jdbc/configuration/config-spring-boot/](https://shardingsphere.apache.org/document/legacy/4.x/document/cn/manual/sharding-jdbc/configuration/config-spring-boot/)

同时分库分表的读写分离参照：
[https://github.com/apache/shardingsphere/blob/4.1.1/examples/sharding-jdbc-example/sharding-example/sharding-spring-boot-mybatis-example/src/main/resources/application-sharding-master-slave.properties](https://github.com/apache/shardingsphere/blob/4.1.1/examples/sharding-jdbc-example/sharding-example/sharding-spring-boot-mybatis-example/src/main/resources/application-sharding-master-slave.properties)

## 4.数据源切换

sharding的数据源统一命名为sharding，即切换时指定该名字，其他非sharding数据源按照原来的名字使用；常量定义在CustomDynamicDataSourceConfig.SHARDING,可直接使用；切换示例：

```java
@DS(CustomDynamicDataSourceConfig.SHARDING)
public String getMessage(){}
```

@DS注解的属性即前面说的数据源名称，注解加载service实现方法上，其他各种示例参照： [https://github.com/dynamic-datasource/dynamic-datasource-samples](https://github.com/dynamic-datasource/dynamic-datasource-samples)
，可关注 nest-sample 的示例，一个业务方法里切换多个数据源

## 5.开启sql日志

```yaml
# mybatis sql日志配置，PS：com.alonelyleaf.maple.mapper 替换成服务对应的mapper所在包
logging.level.com.alonelyleaf.maple.mapper = debug
  # sharding sql日志配置(可以单独开，不开mybatis日志)
  # 4.1.1版本
spring.shardingsphere.props.sql.show=true
  # 5.0.0版本
spring.shardingsphere.props.sql-show=true
```
