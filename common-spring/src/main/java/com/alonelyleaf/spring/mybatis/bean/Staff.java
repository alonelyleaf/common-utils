package com.alonelyleaf.spring.mybatis.bean;

import java.util.Date;

/**
 * @author bijl
 * @date 2019/6/5
 */
public class Staff {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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

    public Staff setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Staff setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Staff setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getStaffAge() {
        return staffAge;
    }

    public Staff setStaffAge(String staffAge) {
        this.staffAge = staffAge;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Staff setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public Staff setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
        return this;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public Staff setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
        return this;
    }
}
