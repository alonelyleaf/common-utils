package com.alonelyleaf.util;

import java.util.UUID;

/**
 * UUID工具类
 *
 */
public class UUIDUtil {

    /**
     * alias method for randomUUID
     *
     * @return
     */
    public static String uuid() {
        return randomUUID();
    }

    /**
     * 去掉-的uuid
     *
     * @return
     */
    public static String randomUUID() {
        String uuid = randomRawUUID();
        return new StringBuilder()
                .append(uuid.substring(0, 8))
                .append(uuid.substring(9, 13))
                .append(uuid.substring(14, 18))
                .append(uuid.substring(19, 23))
                .append(uuid.substring(24))
                .toString();
    }

    /**
     * 不去掉-的uuid
     *
     * @return
     */
    private static String randomRawUUID() {
        return UUID.randomUUID().toString();
    }
}
