package com.alonelyleaf.netty.util;

import com.alibaba.fastjson.JSON;

/**
 * @author bijl
 * @date 2019/5/17
 */
public class JSONUtils {

    public static String serialize(Object object) {

        return JSON.toJSONString(object);
    }

    public static <T> T deserialize(String jsonString, Class<T> clazz) {

        return JSON.parseObject(jsonString, clazz);
    }
}
