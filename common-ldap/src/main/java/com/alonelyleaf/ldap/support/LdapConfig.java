package com.alonelyleaf.ldap.support;

/**
 * @author bijl
 * @date 2019/7/31
 */
public class LdapConfig {

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * TODO 初期只支持LDAP
     * LDAP加密传输模式：0-LDAP、1-LDAP TLS start、2-LDAPs, 默认为LDAP
     */
    private Integer transmissionMode = 0;

    /**
     * LDAP服务器上拥有读权限账号的用户名
     */
    private String username;

    /**
     * LDAP服务器上拥有读权限账号的密码
     */
    private String password;

    /**
     * 基准DN，执行用户所在根节点
     */
    private String baseDn;

    /**
     * LDAP服务器上存储用户名称的属性名称
     */
    private String nameAttr;

    /**
     * LDAP服务器上存储号码的属性名称
     */
    private String numberAttr;

    /**
     * LDAP服务器上唯一标识的属性名称
     */
    private String ldapAccountAttr;

    /**
     * LDAP服务器上存储电话号码的属性名称
     */
    private String telephoneNumberAttr;

    /**
     * LDAP服务器上存储邮箱的属性名称
     */
    private String mailAttr;

    /**
     * 鉴权类型
     * simple
     * DIGEST-MD5
     */
    private String authenticationType;

    public String getHost() {
        return host;
    }

    public LdapConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public LdapConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public Integer getTransmissionMode() {
        return transmissionMode;
    }

    public LdapConfig setTransmissionMode(Integer transmissionMode) {
        this.transmissionMode = transmissionMode;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public LdapConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LdapConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getBaseDn() {
        return baseDn;
    }

    public LdapConfig setBaseDn(String baseDn) {
        this.baseDn = baseDn;
        return this;
    }

    public String getNameAttr() {
        return nameAttr;
    }

    public LdapConfig setNameAttr(String nameAttr) {
        this.nameAttr = nameAttr;
        return this;
    }

    public String getNumberAttr() {
        return numberAttr;
    }

    public LdapConfig setNumberAttr(String numberAttr) {
        this.numberAttr = numberAttr;
        return this;
    }

    public String getLdapAccountAttr() {
        return ldapAccountAttr;
    }

    public LdapConfig setLdapAccountAttr(String ldapAccountAttr) {
        this.ldapAccountAttr = ldapAccountAttr;
        return this;
    }

    public String getTelephoneNumberAttr() {
        return telephoneNumberAttr;
    }

    public LdapConfig setTelephoneNumberAttr(String telephoneNumberAttr) {
        this.telephoneNumberAttr = telephoneNumberAttr;
        return this;
    }

    public String getMailAttr() {
        return mailAttr;
    }

    public LdapConfig setMailAttr(String mailAttr) {
        this.mailAttr = mailAttr;
        return this;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public LdapConfig setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
        return this;
    }
}
