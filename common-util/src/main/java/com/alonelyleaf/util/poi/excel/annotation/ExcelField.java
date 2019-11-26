package com.alonelyleaf.util.poi.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 导出字段字段排序（升序）
     */
    int sort() default 0;

    /**
     * 数据类型
     * String
     * java.util.Date yyyy-MM-DD格式日期
     * java.sql.Date YYYY-MM-DD hh:mm:ss格式日期
     * Double
     * Long
     * Integer
     * Float
     */
    Class<?> fieldType() default Class.class;
}
