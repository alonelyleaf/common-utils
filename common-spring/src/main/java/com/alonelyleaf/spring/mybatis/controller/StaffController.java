package com.alonelyleaf.spring.mybatis.controller;

import com.alonelyleaf.spring.mybatis.bean.Staff;
import com.alonelyleaf.spring.mybatis.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bijl
 * @date 2019/6/5
 */
@RestController
public class StaffController {

    //TODO 输入输出与数据库实体需要转换，不能直接使用，以便数据安全及隔离

    @Autowired
    private StaffService staffService;

    /**
     * 查询全部
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/listAll")
    public Object listAll(@RequestParam(value = "page",defaultValue = "1")int page,
                          @RequestParam(value = "size",defaultValue = "10")int size){

        return staffService.listAll(page, size);
    }

    /**
     * 添加数据
     * @param Staff
     * @return
     */
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public int insert (Staff Staff){
        return staffService.insert(Staff);
    }

    /**
     * 删除
     * @param StaffId
     * @return
     */
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public int remove(Integer StaffId){
        return staffService.remove(StaffId);
    }

    /**
     * 修改
     * @param staff
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(Staff staff){
        return staffService.update(staff);
    }

}
