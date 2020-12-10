# websocket 协议java实现

这里介绍的实现都是使用**注解**的方式进行使用。

## 基于spring-websocket 实现

### 1.引入依赖

```xml
<!-- 引入 websocket 依赖类-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 2.server实现

todo

## 基于netty实现

 https://github.com/YeautyYE/netty-websocket-spring-boot-starter
 
 ### 1.引入依赖
 
 ```xml
 <!-- 引入 websocket 依赖类-->
 <dependency>
      <groupId>org.yeauty</groupId>
      <artifactId>netty-websocket-spring-boot-starter</artifactId>
      <version>0.9.5</version>
</dependency>
 ```
 
 ### 2.server实现

todo
 
## nginx配置

nginx需要配置支持websocket协议，路由配置中增加如下内容，并且需要根据心跳时间修改连接超时时间，防止nginx连接超时断掉连接。

```
proxy_set_header Upgrade $http_upgrade; # 协议升级
proxy_set_header Connection "upgrade"; # 协议升级
```

完整nginx配置如下：

```
upstream api.xx.com80 {
    server 12.12.12.112:81;
    server 12.12.12.113:81;
}
server {
    listen       80;
    listen       443 ssl;
    server_name  gateway.xx.com api.xx.com openapi.xx.com;
    access_log   /apps/log/nginx/gateway.xx.com.access.log main;
    error_log    /apps/log/nginx/gateway.xx.com.error.log;

    ssl_certificate      /apps/srv/nginx/conf/xx.com.pem;
    ssl_certificate_key  /apps/srv/nginx/conf/xx.com.key;
    ssl_session_cache    shared:SSL:1m;
    ssl_session_timeout  5m;
    ssl_ciphers  HIGH:!aNULL:!MD5;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers  on;
    location  ~ .*\.(txt)$ {
        root /apps/webroot/xx_www/;
    }

    location / {
        proxy_pass http://api.xx.com80;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size 500m;
        client_body_buffer_size 128k;
        proxy_connect_timeout 60;
        proxy_read_timeout 60;
        proxy_buffer_size 4k;
    }
    ## 配置websocket路由
    location /websocket/ {
        proxy_pass http://api.xx.com80;
        proxy_http_version 1.1; # http协议版本
        proxy_set_header Upgrade $http_upgrade; # 协议升级
        proxy_set_header Connection "upgrade"; # 协议升级
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        client_max_body_size 500m;
        client_body_buffer_size 128k;
        proxy_connect_timeout 60;
        proxy_read_timeout 300; # 空闲链接超时时间
        proxy_buffer_size 4k;
    }
}
```

## gateway配置

### 配置路由转发

**注意**

（1）`xx-websocket` 为websocket服务注册到注册中心的名字；

（2）`/websocket/live/**` 是路由匹配的规则；

（3）`filters` 是由于使用netty实现，websocket端口与注册到注册中心的服务端口不一致，需要自定义filter，调整路由转发的端口。

```yaml
spring.cloud.gateway:
  routes:
    #表示websocket的转发
    - id: xx-websocket
      uri: lb:ws://xx-websocket
      predicates:
        - Path=/websocket/live/**
      filters:
        - name: WebSocket
          args:
            port: 8899
```

自定义websocket协议路由转发filter，并且调整目标端口

```java
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * WebSocket 网关过滤器工厂
 * <p>
 * 参照LoadBalancerClientFilter 实现，调整目标地址端口号
 *
 */
@Slf4j
public class WebSocketGatewayFilterFactory extends AbstractGatewayFilterFactory<WebSocketGatewayFilterFactory.Config> {

    private static final String websocket_port = "port";

    @Resource
    private LoadBalancerProperties properties;

    @Resource
    private LoadBalancerClient loadBalancer;

    public WebSocketGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(websocket_port);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return new WebSocketGatewayFilter(config);
    }

    /**
     * WebSocket 网关过滤器
     */
    public class WebSocketGatewayFilter implements GatewayFilter, Ordered {

        private Config config;

        WebSocketGatewayFilter(Config config) {
            this.config = config;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

            URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
            String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
            if (url == null
                    || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
                return chain.filter(exchange);
            }
            // preserve the original url
            addOriginalRequestUrl(exchange, url);

            log.info("LoadBalancerClientFilter url before: " + url);

            final ServiceInstance instance = choose(exchange);

            if (instance == null) {
                throw NotFoundException.create(properties.isUse404(),
                        "Unable to find instance for " + url.getHost());
            }

            URI uri = exchange.getRequest().getURI();

            // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
            // if the loadbalancer doesn't provide one.
            String overrideScheme = instance.isSecure() ? "https" : "http";
            if (schemePrefix != null) {
                overrideScheme = url.getScheme();
            }

            URI requestUrl = loadBalancer.reconstructURI(
                    new DelegatingServiceInstance(instance, overrideScheme), uri);

            // 调整端口号
            if (ObjectUtil.isNotEmpty(config.getPort()) && config.getPort() > 0) {

                log.info("WebSocketGatewayFilter host {}, change port from {} to {}", requestUrl.getHost()
                        , requestUrl.getPort(), config.getPort());

                String newUriString = requestUrl.getScheme() + "://" + requestUrl.getHost() + ":"
                        + config.getPort() + requestUrl.getPath();

                if (ObjectUtil.isNotEmpty(requestUrl.getQuery())) {
                    newUriString = newUriString + "?" + requestUrl.getQuery();
                }

                requestUrl = UriComponentsBuilder.fromUriString(newUriString).encode().build().toUri();
            }

            log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
            exchange.getAttributes().put(GATEWAY_SCHEME_PREFIX_ATTR, requestUrl.getScheme());
            return chain.filter(exchange);
        }

        /**
         * 过滤器执行顺序，比 LoadBalancerClientFilter 早执行
         *
         * @return
         */
        @Override
        public int getOrder() {
            return LoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER - 1;
        }
    }

    private ServiceInstance choose(ServerWebExchange exchange) {
        return loadBalancer.choose(
                ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost());
    }

    public static class Config {

        private Integer port;

        public Integer getPort() {
            return port;
        }

        public Config setPort(Integer port) {
            this.port = port;
            return this;
        }
    }
}

/** 
 * 配置类
 */
@Configuration
public class RouterFunctionConfiguration {
    
    @Bean
    public WebSocketGatewayFilterFactory webSocketGatewayFilterFactory() {
        return new WebSocketGatewayFilterFactory();
    }
}

```

