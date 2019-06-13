package com.alonelyleaf.spring.redis.lock;

import com.alonelyleaf.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/6/13
 */
@Component
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @param key   productId - 商品的唯一标志
     * @param value 当前时间+超时时间 也就是时间戳
     * @return
     */
    public boolean lock(String key, Object value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {//对应setnx命令
            //可以成功设置,也就是key不存在
            return true;
        }

        //判断锁超时 - 防止原来的操作异常，没有运行解锁操作  防止死锁
        Object currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期
        if (!ValidateUtil.isEmpty(currentValue) && (Long) currentValue < System.currentTimeMillis()) {//currentValue不为空且小于当前时间
            //获取上一个锁的时间value
            Object oldValue = redisTemplate.opsForValue().getAndSet(key, value);//对应getset，如果key存在

            //假设两个线程同时进来这里，因为key被占用了，而且锁过期了。获取的值currentValue=A(get取的旧的值肯定是一样的),两个线程的value都是B,key都是K.锁时间已经过期了。
            //而这里面的getAndSet一次只会一个执行，也就是一个执行之后，上一个的value已经变成了B。只有一个线程获取的上一个值会是A，另一个线程拿到的值是B。
            if (!ValidateUtil.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                //oldValue不为空且oldValue等于currentValue，也就是校验是不是上个对应的商品时间戳，也是防止并发
                return true;
            }
        }
        return false;
    }


    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            Object currentValue = redisTemplate.opsForValue().get(key);
            if (!ValidateUtil.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);//删除key
            }
        } catch (Exception e) {
            logger.error("[Redis分布式锁] 解锁出现异常了，{}", e);
        }
    }

}
