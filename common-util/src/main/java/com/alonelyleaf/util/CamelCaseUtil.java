package com.alonelyleaf.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jodd.util.Format;

import java.util.concurrent.TimeUnit;

/**
 * 驼峰工具类
 */
public class CamelCaseUtil {

    private static Cache<String, String> from = Caffeine.newBuilder().maximumSize(100L)
            .expireAfterWrite(2, TimeUnit.MINUTES).build();

    private static Cache<String, String> to = Caffeine.newBuilder().maximumSize(100L)
            .expireAfterWrite(2, TimeUnit.MINUTES).build();

    public static String fromCamelCase(String input) {
        if (ValidateUtil.isEmpty(input)) {
            return input;
        }

        String result = from.getIfPresent(input);
        if (ValidateUtil.isNotEmpty(result)) {
            return result;
        }

        result = fromCamelCase(input, '_');
        from.put(input, result);

        return result;
    }

    public static String fromCamelCase(String input, char separator) {

        return Format.fromCamelCase(input, separator);
    }

    public static String toCamelCase(String input) {

        if (ValidateUtil.isEmpty(input)) {
            return input;
        }

        String result = to.getIfPresent(input);
        if (ValidateUtil.isNotEmpty(result)) {
            return result;
        }

        result = toCamelCase(input, '_');
        to.put(input, result);

        return result;
    }

    public static String toCamelCase(String input, char separator) {

        return toCamelCase(input, false, separator);
    }

    public static String toCamelCase(String input, boolean firstCharUppercase, char separator) {

        return Format.toCamelCase(input, firstCharUppercase, separator);
    }
}
