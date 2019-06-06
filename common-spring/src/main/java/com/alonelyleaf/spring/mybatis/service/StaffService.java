package com.alonelyleaf.spring.mybatis.service;

import com.alonelyleaf.spring.mybatis.bean.Staff;

/**
 * @author bijl
 * @date 2019/6/5
 */
public interface StaffService {

    Object listAll(int page, int size);

    int insert(Staff staff);

    int remove(Integer staffId);

    int update(Staff staff);
}
