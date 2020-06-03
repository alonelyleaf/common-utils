package com.alonelyleaf.spring.template.entity;

import java.util.Date;

/**
 * @author bijl
 * @date 2020/2/20
 */
public class Template {

    /**
     * 主键
     */
    private String _id;

    private Long createTime = new Date().getTime();

    private Long modifyTime = createTime;

    private Boolean deleted = false;

    /**
     * 模版编码
     */
    private String code;

    /**
     * 内容
     */
    private String content;

    /**
     * 模板内容类型,区分纯文本与HTML
     */
    private String contentType;

    /**
     * 主题
     */
    private String subject;

    /**
     * 语言
     */
    private String language;

    /**
     * 模板顶部的脚本
     */
    private String script;

    /**
     * 模板参数说明
     */
    private String params;

    /**
     * 模板说明
     */
    private String description;

    /**
     * 触发规则
     */
    private String rule;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 邮件接收者类型
     */
    private String receiverType;

    /**
     * 模板类型
     */
    private String type = "MAIL";

    /**
     * 原始名称（reset使用）
     */
    private String originSubject;

    /**
     * 原始内容(reset使用)
     */
    private String originContent;

    public String get_id() {
        return _id;
    }

    public Template set_id(String _id) {
        this._id = _id;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Template setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public Template setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Template setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Template setCode(String code) {
        this.code = code;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Template setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Template setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Template setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Template setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getScript() {
        return script;
    }

    public Template setScript(String script) {
        this.script = script;
        return this;
    }

    public String getParams() {
        return params;
    }

    public Template setParams(String params) {
        this.params = params;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Template setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public Template setRule(String rule) {
        this.rule = rule;
        return this;
    }

    public String getName() {
        return name;
    }

    public Template setName(String name) {
        this.name = name;
        return this;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public Template setReceiverType(String receiverType) {
        this.receiverType = receiverType;
        return this;
    }

    public String getType() {
        return type;
    }

    public Template setType(String type) {
        this.type = type;
        return this;
    }

    public String getOriginSubject() {
        return originSubject;
    }

    public Template setOriginSubject(String originSubject) {
        this.originSubject = originSubject;
        return this;
    }

    public String getOriginContent() {
        return originContent;
    }

    public Template setOriginContent(String originContent) {
        this.originContent = originContent;
        return this;
    }
}
