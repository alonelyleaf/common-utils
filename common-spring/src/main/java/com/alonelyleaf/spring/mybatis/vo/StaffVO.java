package com.alonelyleaf.spring.mybatis.vo;

import java.util.Date;

/**
 * @author bijl
 * @date 2019/6/6
 */
public class StaffVO {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 性别
     */
    private String staffAge;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public StaffVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public StaffVO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getStaffAge() {
        return staffAge;
    }

    public StaffVO setStaffAge(String staffAge) {
        this.staffAge = staffAge;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public StaffVO setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public StaffVO setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public StaffVO setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}
