package com.alonelyleaf.spring.redis.pubsub;

import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author bijl
 * @date 2019/6/6
 */
@Component
public class RedisMessageReceiver{

    private static Logger logger = LoggerFactory.getLogger(RedisMessageReceiver.class);

    /**
     * 接受消息后，执行的方法
     * @param message
     */
    public void receiveColumnDictMessage(String message) {
        logger.info("redis订阅者接受消息:{},更新全局变量缓存dict_column", message);
    }

    /**
     * redis 系统配置监听器
     * @param message
     */
    public void receiveSysConfigMessage(String message) {
        logger.info("redis订阅者接受消息:{},更新全局变量缓存sysConfig", message);
    }
}
