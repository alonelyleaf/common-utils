package com.alonelyleaf.spring.mybatis.service.impl;

import com.alonelyleaf.spring.mybatis.bean.Staff;
import com.alonelyleaf.spring.mybatis.mapper.StaffMapper;
import com.alonelyleaf.spring.mybatis.service.StaffService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bijl
 * @date 2019/6/5
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Resource
    private StaffMapper StaffMapper;

    @Override
    public Object listAll(int page, int size) {
        PageHelper.startPage(page, size);
        List<Staff> StaffList = StaffMapper.listAll();
        PageInfo<Staff> pageInfo = new PageInfo<>(StaffList);
        return pageInfo;
    }

    @Override
    public int insert(Staff Staff) {
        return StaffMapper.insert(Staff);
    }

    @Override
    public int remove(Integer StaffId) {
        return StaffMapper.remove(StaffId);
    }

    @Override
    public int update(Staff Staff) {
        return StaffMapper.update(Staff);
    }

}
