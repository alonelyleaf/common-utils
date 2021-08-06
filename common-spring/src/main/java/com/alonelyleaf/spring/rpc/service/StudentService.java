package com.alonelyleaf.spring.rpc.service;

import com.alonelyleaf.spring.rpc.entity.Student;

/**
 * 类的描述
 *
 * @author bijl
 * @date 8/6/21
 */
public interface StudentService {

    /**
     * 获取信息
     *
     * @return
     */
    public Student getInfo();

    public boolean printInfo(Student student);
}
