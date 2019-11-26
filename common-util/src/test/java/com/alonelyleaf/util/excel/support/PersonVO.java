package com.alonelyleaf.util.excel.support;

/**
 * @author bijl
 * @date 2019/11/26
 */
public class PersonVO {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public PersonVO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public PersonVO setAge(Integer age) {
        this.age = age;
        return this;
    }
}
