package com.alonelyleaf.util;


import jodd.json.*;

import java.util.*;

/**
 * Map工具类
 *
 */
public class MapUtil {

    /**
     * 获取string类型的值
     *
     * @param map
     * @param key
     * @return
     */
    public static String getString(Map map, String key) {

        return (String) getValue(map, key);
    }

    /**
     * 获得integer类型的值
     *
     * @param map
     * @param key
     * @return
     */
    public static Integer getInteger(Map map, String key) {

        return (Integer) getValue(map, key);
    }

    public static Object getValue(Map map, String key) {

        if (ValidateUtil.isEmpty(map)) {
            return null;
        }
        return map.get(key);
    }

    /**
     * 数组 -> Map
     *
     * @param data
     * @return
     */
    public static Map asMap(Object... data) {

        if (data.length % 2 == 1) {
            throw new IllegalArgumentException("the length must be multiply of 2");
        }

        Map map = new HashMap();
        for (int i = 0; i < data.length; ) {
            map.put(data[i++], data[i++]);
        }
        return map;
    }

    /**
     * 数组 -> LinkedHashMap
     *
     * @param data
     * @return
     */
    public static Map asLinkedHashMap(Object... data) {

        if (data.length % 2 == 1) {
            throw new IllegalArgumentException("the length must be multiply of 2");
        }

        Map map = new LinkedHashMap();
        for (int i = 0; i < data.length; ) {
            map.put(data[i++], data[i++]);
        }
        return map;
    }

    /**
     * 强制转型
     *
     * @param object
     * @return
     */
    public static Map cast(Object object) {

        return (Map) object;
    }

    /**
     * bean -> map
     *
     * @param bean
     * @return
     */
    public static Map fromBean(Object bean, String... ignoreFields) {

        return fromBean(bean, true, ignoreFields);
    }

    public static Map fromBean(Object bean, boolean ignoreNull, String... ignoreFields) {

        final Map result = new HashMap();

        JsonContext jsonContext = new JsonSerializer().deep(true).createJsonContext(null);

        BeanSerializer beanSerializer = new BeanSerializer(jsonContext, bean) {
            @Override
            protected void onSerializableProperty(String propertyName, Class propertyType, Object value) {

                // 如果需要移除null，且当前值为null
                if (ignoreNull && value == null) {
                    return;
                }

                if (ignoreFields != null) {
                    for (String each : ignoreFields) {
                        // 如果在忽略字段中
                        if (propertyName.equals(each)) {
                            return;
                        }
                    }
                }

                result.put(propertyName, value);
            }
        };
        beanSerializer.serialize();
        return result;
    }

    /**
     * map转bean
     *
     * @param src
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T toBean(Map src, Class<T> target) {

        if (ValidateUtil.isEmpty(src)) {
            return null;
        }

        return (T) new MapToBean(new JsonParser(), target.getName()).map2bean(src, target);
    }

    public static <T> List<T> toBean(List<Map<String, ?>> src, Class<T> target) {

        List<T> result = new ArrayList<T>();

        for (Map each : src) {
            T bean = toBean(each, target);
            if (bean != null) {
                result.add(bean);
            }
        }
        return result;
    }

    /**
     * 扩展jodd MapToBean，修复对于entity中包含Map属性，如果Map有null值字段时，转换失败问题
     *
     */
    static class MapToBean extends jodd.json.MapToBean {

        public MapToBean(JsonParserBase jsonParser, String classMetadataName) {
            super(jsonParser, classMetadataName);
        }

        @Override
        protected Object convert(Object value, Class targetType) {

            if (value == null) {
                return value;
            }

            return super.convert(value, targetType);
        }
    }
}
