package com.alonelyleaf.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommonUtil {

    /**
     * 转小写
     *
     * @param data
     * @return
     */
    public static String lowerCase(String data) {

        if (data == null) {
            return null;
        }

        return data.toLowerCase();
    }

    /**
     * 转大写
     *
     * @param data
     * @return
     */
    public static String upperCase(String data) {

        if (data == null) {
            return null;
        }

        return data.toUpperCase();
    }

    /**
     * 去两端空格
     *
     * @param data
     * @return
     */
    public static String trim(String data) {

        if (data == null) {
            return null;
        }

        return data.trim();
    }

    /**
     * trim并转小写
     *
     * @param data
     * @return
     */
    public static String trimLower(String data) {

        return lowerCase(trim(data));
    }

    /**
     * trim并转大写
     *
     * @param data
     * @return
     */
    public static String trimUpper(String data) {

        return upperCase(trim(data));
    }

    /**
     * 空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(Object value) {

        return ValidateUtil.isEmpty(value);
    }

    /**
     * 非空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(Object value) {

        return ValidateUtil.isNotEmpty(value);
    }

    /**
     * 是否合法的手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {

        return ValidateUtil.isMobile(mobile);
    }

    /**
     * 合法邮箱
     *
     * @param email
     * @return
     */
    public static boolean isMail(String email) {

        return ValidateUtil.isMail(email);
    }

    /**
     * 非法邮箱
     *
     * @param email
     * @return
     */
    public static boolean isNotMail(String email) {

        return ValidateUtil.isNotMail(email);
    }

    /**
     * 判断Boolean是否为true
     */
    @Deprecated
    public static Boolean isTrue(Boolean check) {

        return ValidateUtil.isTrue(check);
    }

    public static boolean isEquals(String a, String b) {

        return jodd.util.Util.equals(a, b);
    }

    public static boolean isEquals(Integer a, Integer b) {

        return jodd.util.Util.equals(a, b);
    }

    public static boolean isEquals(Long a, Long b) {

        return jodd.util.Util.equals(a, b);
    }

    public static boolean isNotEquals(String a, String b) {

        return !isEquals(a, b);
    }

    public static boolean isNotEquals(Integer a, Integer b) {

        return !isEquals(a, b);
    }

    public static boolean isNotEquals(Long a, Long b) {

        return !isEquals(a, b);
    }

    /**
     * uuid
     *
     * @return
     */
    public static String uuid() {

        return UUIDUtil.uuid();
    }

    /**
     * map快速生成
     *
     * @param data
     * @return
     */
    public static Map asMap(Object... data) {

        return MapUtil.asMap(data);
    }

    /**
     * 获取string类型的值
     *
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map map, String key) {

        return MapUtil.getString(map, key);
    }

    /**
     * 获得integer类型的值
     *
     * @param map
     * @param key
     * @return
     */
    public static Integer getInteger(Map map, String key) {

        return MapUtil.getInteger(map, key);
    }

    /**
     * 系统当前时间
     *
     * @return
     */
    public static long now() {

        return System.currentTimeMillis();
    }


    /**
     * 是否满足正则表达式
     *
     * @param str
     * @param reg
     * @return
     */
    public static boolean matchReg(String str, String reg) {

        return ValidateUtil.matchReg(str, reg);
    }

    public static boolean notMatchReg(String str, String reg) {

        return !matchReg(str, reg);
    }

    /**
     * 是否满足正则表达式
     *
     * @param str
     * @param pattern
     * @return
     */
    public static boolean matchReg(String str, Pattern pattern) {

        return ValidateUtil.matchReg(str, pattern);
    }

    public static boolean notMatchReg(String str, Pattern pattern) {

        return !matchReg(str, pattern);
    }



    /**
     * 对象转map，并且移除值为null的项
     *
     * @param object
     * @param ignoreFields
     * @return
     */
    public static Map fromBean(Object object, String... ignoreFields) {

        return MapUtil.fromBean(object, true, ignoreFields);
    }

    /**
     * 对象转map
     *
     * @param object
     * @param ignoreNull
     * @param ignoreFields
     * @return
     */
    public static Map fromBean(Object object, boolean ignoreNull, String... ignoreFields) {

        return MapUtil.fromBean(object, ignoreNull, ignoreFields);
    }

    /**
     * bean copy
     *
     * @param src
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T copy(Object src, Class<T> targetClass, String... excludes) {

        return BeanUtil.copy(src, targetClass, excludes);
    }

    /**
     * bean copy
     *
     * @param src
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> copy(List src, Class<T> targetClass, String... excludes) {

        return BeanUtil.copy(src, targetClass, excludes);
    }

    public static <T> T copy(Object src, T target, String... excludes) {

        return BeanUtil.copy(src, target, excludes);
    }

    public static <T> T copy(Object src, Class<T> targetClass, boolean ignoreNullValues, String... excludes) {

        return BeanUtil.copy(src, targetClass, ignoreNullValues, excludes);
    }

    public static <T> List<T> copy(List src, Class<T> targetClass, boolean ignoreNullValues, String... excludes) {

        return BeanUtil.copy(src, targetClass, ignoreNullValues, excludes);
    }

    public static <T> T copy(Object src, T target, boolean ignoreNullValues, String... excludes) {

        return BeanUtil.copy(src, target, ignoreNullValues, excludes);
    }

    /**
     * 数组转集合
     *
     * @param data
     * @param <T>
     * @return
     */
    public <T> List<T> asList(T... data) {

        return Stream.of(data).collect(Collectors.toList());
    }


    /**
     * value为空返回defaultValue，否则返回value
     *
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T nvl(T value, T defaultValue) {

        if (isEmpty(value)) {
            return defaultValue;
        }

        return value;
    }

    /**
     * 抽集合中第一个的某个特定字段值，不需要判断source是否为空
     *
     * @param source
     * @param function
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Optional<S> extractFirst(List<T> source, Function<T, S> function) {

        Optional<List<T>> optionalSource = Optional.ofNullable(source);
        return optionalSource.map(list -> list.stream().findFirst().orElse(null)).map(function);
    }

    /**
     * 抽取某个字段，或对某些字段做处理，不需要判断source是否为空
     *
     * @param source
     * @param function
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> Optional<List<S>> extract(List<T> source, Function<T, S> function) {

        Optional<List<T>> optionalSource = Optional.ofNullable(source);
        return optionalSource.map(list -> list.stream().map(function).collect(Collectors.toList()));
    }
}
