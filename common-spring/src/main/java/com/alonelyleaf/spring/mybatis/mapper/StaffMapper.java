package com.alonelyleaf.spring.mybatis.mapper;

import com.alonelyleaf.spring.mybatis.bean.Staff;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author bijl
 * @date 2019/6/5
 */
@Mapper
public interface StaffMapper {

    @Select({"select * from staff"})
    List<Staff> listAll();

    @Insert({"insert into staff(`username`, `password`, `staff_age`, `nick_name`) values(#{username}, #{password}, #{staffAge}, #{nickName})"})
    int insert(Staff staff);

    @Delete({"delete from staff where id = #{userId}"})
    int remove(Integer staffId);

    @Update({"update staff set username = #{username}, password = #{password} where id = #{id}"})
    int update(Staff staff);

}
