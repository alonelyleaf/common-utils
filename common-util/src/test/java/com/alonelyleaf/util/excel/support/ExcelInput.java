package com.alonelyleaf.util.excel.support;

import com.alonelyleaf.util.poi.excel.annotation.ExcelField;

/**
 * @author bijl
 * @date 2019/11/26
 */
public class ExcelInput {

    @ExcelField(sort = 1, fieldType = String.class)
    private String name;

    @ExcelField(sort = 2, fieldType = String.class)
    private Integer age;

    public String getName() {
        return name;
    }

    public ExcelInput setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public ExcelInput setAge(Integer age) {
        this.age = age;
        return this;
    }
}
