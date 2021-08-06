package com.alonelyleaf.spring.rpc.service.impl;

import com.alonelyleaf.spring.rpc.RpcService;
import com.alonelyleaf.spring.rpc.entity.Student;
import com.alonelyleaf.spring.rpc.service.StudentService;

/**
 * StudentServiceImpl
 */
@RpcService(StudentService.class)
public class StudentServiceImpl implements StudentService {

    @Override
    public Student getInfo() {
        Student person = new Student();
        person.setAge(18);
        person.setName("arrylist");
        person.setSex("女");
        return person;
    }

    @Override
    public boolean printInfo(Student person) {
        if (person != null) {
            System.out.println(person);
            return true;
        }
        return false;
    }
}