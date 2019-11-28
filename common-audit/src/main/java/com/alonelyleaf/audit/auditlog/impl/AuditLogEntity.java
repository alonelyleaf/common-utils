package com.alonelyleaf.audit.auditlog.impl;

import com.alonelyleaf.audit.auditlog.AuditLog;

import java.util.Date;

/**
 * @author bijl
 * @date 2019/11/28
 */
public class AuditLogEntity implements AuditLog {

    /**
     * 业务标识
     */
    private String appName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * id
     */
    private String _id;


    /**
     * 操作名称
     */
    private String operation;

    /**
     * 操作模块
     */
    private String module;

    /**
     * 操作者用户名
     */
    private Object operator;

    /**
     * 操作对象
     */
    private Object target;

    /**
     * 操作者IP
     */
    private String ip;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 操作详情
     */
    private Object detail;

    /**
     * 上报时间
     */
    private Long uptime;

    /**
     * 请求信息参数
     */
    private Object[] args;

    //-------------------------------------------------- constructor -------------------------------------------------

    public AuditLogEntity() {
        this.createTime = System.currentTimeMillis();
        this.uptime = System.currentTimeMillis();
    }

    public AuditLogEntity(String appName, String operation, String module, Object operator, Object target, String ip, String result, Object detail, Long uptime, Object[] args) {
        this.appName = appName;
        this.operation = operation;
        this.module = module;
        this.operator = operator;
        this.target = target;
        this.ip = ip;

        this.args = args;
        this.result = result;
        this.detail = detail;

        this.createTime = System.currentTimeMillis();
        this.uptime = uptime;
    }


    //----------------------------------- getter and setter ----------------------------------------
    @Override
    public String getAppName() {
        return appName;
    }

    public AuditLogEntity setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    public AuditLogEntity setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String getId() {
        return _id;
    }

    public String get_id() {
        return _id;
    }

    public AuditLogEntity set_id(String _id) {
        this._id = _id;
        return this;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    public AuditLogEntity setOperation(String operation) {
        this.operation = operation;
        return this;
    }

    @Override
    public String getModule() {
        return module;
    }

    public AuditLogEntity setModule(String module) {
        this.module = module;
        return this;
    }

    @Override
    public Object getOperator() {
        return operator;
    }

    public AuditLogEntity setOperator(Object operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    public AuditLogEntity setTarget(Object target) {
        this.target = target;
        return this;
    }

    @Override
    public String getIp() {
        return ip;
    }

    public AuditLogEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }

    @Override
    public String getResult() {
        return result;
    }

    public AuditLogEntity setResult(String result) {
        this.result = result;
        return this;
    }

    @Override
    public Object getDetail() {
        return detail;
    }


    public Object[] getArgs() {
        return args;
    }

    public AuditLogEntity setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public AuditLogEntity setDetail(Object detail) {
        this.detail = detail;
        return this;
    }

    public Long getUptime() {
        return uptime;
    }

    public AuditLogEntity setUptime(Long uptime) {
        this.uptime = uptime;
        return this;
    }

}
