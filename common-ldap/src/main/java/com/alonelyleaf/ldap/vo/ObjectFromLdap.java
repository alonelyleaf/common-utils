package com.alonelyleaf.ldap.vo;

public class ObjectFromLdap {
    /**
     * 从LDAP获取的名称，与YMS的STAFF的名称对应，用于显示
     */
    private String name;

    /**
     * 从LDAP获取的号码，与YMS的STAFF的账号对应，为四位数字
     */
    private String number;

    /**
     * 从LDAP获取的邮箱信息
     */
    private String mail;

    /**
     * 条目信息，用于解析部门
     */
    private String dn;

    /**
     * 域账号
     */
    private String ldapAccountName;

    /**
     * 电话号码
     */
    private String telephoneNumber;

    public String getName() {
        return name;
    }

    public ObjectFromLdap setName(String name) {
        this.name = name;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public ObjectFromLdap setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public ObjectFromLdap setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getDn() {
        return dn;
    }

    public ObjectFromLdap setDn(String dn) {
        this.dn = dn;
        return this;
    }

    public String getLdapAccountName() {
        return ldapAccountName;
    }

    public ObjectFromLdap setLdapAccountName(String ldapAccountName) {
        this.ldapAccountName = ldapAccountName;
        return this;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public ObjectFromLdap setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
        return this;
    }

    @Override
    public String toString(){
        return "name: " + name + "; number: " + number + "; mail: " + mail  + "; dn: " + dn  + "; ldapAccountName: " + ldapAccountName  + "; telephoneNumber: " + telephoneNumber;
    }
}
