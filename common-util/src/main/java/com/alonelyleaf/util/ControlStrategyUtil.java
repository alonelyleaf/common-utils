package com.alonelyleaf.util;

import jodd.system.SystemInfo;
import org.springframework.stereotype.Component;

/**
 * 控制策略工具类
 *
 */
@Component
public class ControlStrategyUtil {

    static ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

    /**
     * 判断当前启动模式是否是dev模式
     *
     * @return
     */
    public static boolean isDevProfile() {

        return SpringContextUtil.isDevProfile()
                && (new SystemInfo().isWindows() || new SystemInfo().isMac());
    }

    /**
     * 当前线程是否开启严格校验
     *
     * @return
     */
    public static boolean isStrictValidateEnable() {

        Boolean value = threadLocal.get();
        if (value == null) {
            return false;
        }
        return value;
    }

    /**
     * 设置当前线程的校验模式
     *
     * @param value
     */
    public static void setValidationStrategy(Boolean value) {

        threadLocal.set(value);
    }

    public static void clearValidationStrategy() {

        threadLocal.remove();
    }
}
