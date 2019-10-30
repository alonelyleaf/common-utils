package com.alonelyleaf.ldap.util;

import com.alonelyleaf.ldap.support.LdapConfig;
import com.alonelyleaf.ldap.vo.ObjectFromLdap;
import com.alonelyleaf.util.CommonUtil;
import com.alonelyleaf.util.JSONUtil;
import com.alonelyleaf.util.exception.business.BadRequestException;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static com.alonelyleaf.ldap.support.Constant.Ldap.Env.*;
import static com.alonelyleaf.ldap.support.Constant.Ldap.ObjectClass.*;
import static com.alonelyleaf.ldap.support.Constant.Ldap.SearchFilter.LDAP_SEARCH_FILTER;

/**
 * LDAP工具类：
 * 1.匹配搜索：findObjectFromLdapByKey
 * 2.身份认证：authentication
 * 3.测试连接：connectTest
 * 4.获取部门和用户数据：loadDataFromLDAP
 * 5.根据更新时间增量获取用户信息
 *
 */
@Component
public class LdapSupport extends CommonUtil {

    private static Logger logger = LoggerFactory.getLogger(LdapSupport.class);

    public LdapConfig getLdapConfig(){

        return JSONUtil.deserialize("{" +
                "\"host\" : \"10.200.108.65\"," +
                "\"port\" : 389," +
                "\"transmissionMode\" : 0," +
                "\"username\" : \"LDAP\\\\Administrator\"," +
                "\"password\" : \"Yealink!1105\"," +
                "\"baseDn\" : \"CN=Users,DC=ldap,DC=yealink,DC=cn\"," +
                "\"nameAttr\" : \"name\"," +
                "\"numberAttr\" : \"telephoneNumber\"," +
                "\"ldapAccountAttr\" : \"sAMAccountName\"," +
                "\"telephoneNumberAttr\" : \"telephoneNumber\"," +
                "\"mailAttr\" : \"mail\", " +
                "\"authenticationType\" : null," +
                "}", LdapConfig.class);
    }

    /**
     * 从LDAP拉取数据
     */
    public List<ObjectFromLdap> getObjectFromLdaps(LdapConfig ldapConfig) {
        LdapContext ctx = null;
        List<ObjectFromLdap> ObjectFromLdaps = new ArrayList<>();
        try {
            logger.info("[LDAP] Connecting to LDAP server...");
            ctx = new InitialLdapContext(getEnvConfig(ldapConfig), null);
            logger.info("[LDAP] Connected to LDAP server");

            logger.info("[LDAP] Loading data from LDAP...");
            SearchControls searchControls = new SearchControls();

            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

            String[] searchReturnAttrs = getSearchAttrFromConfig(ldapConfig);

            searchControls.setReturningAttributes(searchReturnAttrs);

            traverseChildTree(ctx, ldapConfig.getBaseDn(), searchControls,
                searchReturnAttrs, ldapConfig, LDAP_SEARCH_FILTER, ObjectFromLdaps
            );
            logger.info("[LDAP] Loaded data from LDAP...");

        } catch (NamingException e) {
            logger.error("[LDAP] naming exception", e);
            throw new BadRequestException("connection", "connection error");
        } finally {
            ldapDisconnect(ctx);
        }
        return ObjectFromLdaps;
    }

    /**
     * 根据数据类型和值从LDAP搜索匹配的条目
     *
     * @param attrName LDAP中的属性字段
     * @param value    属性值
     * @return 根据匹配条目构建的ObjectFromLdap实体
     */
    public List<ObjectFromLdap> findObjectFromLdapByKey(String attrName, String value, LdapConfig ldapConfig) {
        List<ObjectFromLdap> result = new ArrayList<>();
        LdapContext ctx = null;
        try {
            ctx = new InitialLdapContext(getEnvConfig(ldapConfig), null);

            SearchControls searchControls = new SearchControls();

            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String[] searchReturnAttrs = getSearchAttrFromConfig(ldapConfig);

            searchControls.setReturningAttributes(searchReturnAttrs);

            NamingEnumeration answer = ctx.search(ldapConfig.getBaseDn(), attrName + "=" + value.trim(), searchControls);

            while (answer != null && answer.hasMoreElements()) {
                SearchResult searchResult = (SearchResult) answer.next();

                Attributes attributes = ctx.getAttributes(searchResult.getNameInNamespace(), searchReturnAttrs);
                Attribute objectClassAttr = attributes.get(OBJECT_CLASS);

                if (isUser(objectClassAttr)) {
                    ObjectFromLdap ObjectFromLdap = buildObjectFromLdap(attributes, searchResult.getNameInNamespace(), ldapConfig);
                    if (ObjectFromLdap == null) {
                        throw new BadRequestException(attrName, "attrNotExist");
                    }
                    result.add(ObjectFromLdap);
                }
            }
        } catch (InvalidSearchFilterException | InvalidSearchControlsException e) {
            logger.error("[LDAP] search exception", e);
            throw new BadRequestException(attrName,"attrNotExist");
        } catch (NamingException e) {
            logger.error("[LDAP] naming exception", e);
            throw new BadRequestException("connection", "attrNotExist");
        } finally {
            ldapDisconnect(ctx);
        }
        return result;
    }

    /**
     * 身份认证
     *
     * @param username 认证用户名
     * @param password 认证密码
     * @return 成功/失败
     */
    public boolean authentication(String username, String password, LdapConfig ldapConfig) {

        LdapContext ctx = null;

        if (paramInvalid(username, password, ldapConfig)) {
            return false;
        }

        try {
            List<ObjectFromLdap> staffs = findObjectFromLdapByKey(ldapConfig.getLdapAccountAttr(), username, ldapConfig);

            if (CollectionUtils.isEmpty(staffs)) {
                return false;
            }

            ctx = new InitialLdapContext(getEnvConfig(ldapConfig, staffs.get(0).getDn(), password), null);

            return true;
        } catch (AuthenticationException e) {
            logger.info("[LDAP] authenticate failed");
            return false;
        } catch (NamingException e) {
            logger.error("[LDAP] naming exception", e);
            throw new BadRequestException("connection", "connection Error");
        } finally {
            ldapDisconnect(ctx);
        }
    }

    private boolean paramInvalid(String username, String password, LdapConfig ldapConfig) {
        return StringUtils.isEmpty(username)
            || StringUtils.isEmpty(password)
            || isEmpty(ldapConfig);
    }

    /**
     * 测试利用提提供的配置项能否建立有效的连接
     *
     * @param ldapConfig LDAP配置项
     * @return 配置项是否有效
     */
    public boolean connectTest(LdapConfig ldapConfig) {

        LdapContext ctx = ldapConnectTest(ldapConfig);

        return ldapBaseOnTest(ldapConfig, ctx);
    }

    private boolean ldapBaseOnTest(LdapConfig ldapConfig, LdapContext ctx) {
        try {
            ctx = new InitialLdapContext(
                getEnvConfig(ldapConfig, ldapConfig.getUsername(), ldapConfig.getPassword()), null);

            SearchControls searchControls = new SearchControls();

            searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

            String[] searchReturnAttrs = getSearchAttrFromConfig(ldapConfig);

            searchControls.setReturningAttributes(searchReturnAttrs);

            NamingEnumeration answer = ctx.search(ldapConfig.getBaseDn(), LDAP_SEARCH_FILTER, searchControls);

            return isNotEmpty(answer) && answer.hasMoreElements();

        } catch (NamingException e) {
            logger.error("[LDAP] naming exception", e);
            throw new BadRequestException("baseDn", "baseDn Error");
        } finally {
            ldapDisconnect(ctx);
        }
    }

    private LdapContext ldapConnectTest(LdapConfig ldapConfig) {
        LdapContext ctx = null;
        try {

            ctx = new InitialLdapContext(getEnvConfig(ldapConfig, ldapConfig.getUsername(), ldapConfig.getPassword()), null);

        } catch (AuthenticationException e) {
            throw new BadRequestException("auth", "auth Error");

        } catch (NamingException e) {
            throw new BadRequestException("connection", "connection Error");

        } finally {
            ldapDisconnect(ctx);
        }
        return ctx;
    }

    /**
     * 递归查找所有用户节点，并存入ObjectFromLdaps中
     *
     * @param ctx               LDAP连接
     * @param searchBaseDn      子树根节点DN
     * @param searchCtrls       searchControls
     * @param searchReturnAttrs 搜索返回的属性
     */
    private void traverseChildTree(LdapContext ctx,
                                   String searchBaseDn,
                                   SearchControls searchCtrls,
                                   String[] searchReturnAttrs,
                                   LdapConfig config,
                                   String filter,
                                   List<ObjectFromLdap> ObjectFromLdaps) {
        try {
            NamingEnumeration answer = ctx.search(searchBaseDn, filter, searchCtrls);

            while (isNotEmpty(answer) && answer.hasMoreElements()) {
                SearchResult searchResult = (SearchResult) answer.next();

                Attributes attributes = ctx.getAttributes(searchResult.getNameInNamespace(), searchReturnAttrs);
                Attribute objectClassAttr = attributes.get(OBJECT_CLASS);

                if (isOrg(objectClassAttr)) {
                    traverseChildTree(
                        ctx,
                        searchResult.getName() + "," + searchBaseDn,
                        searchCtrls,
                        searchReturnAttrs,
                        config,
                        filter,
                        ObjectFromLdaps
                    );
                }
                if (isUser(objectClassAttr)) {
                    ObjectFromLdap ObjectFromLdap = buildObjectFromLdap(attributes, searchResult.getNameInNamespace(), config);

                    if (isNotEmpty(ObjectFromLdap)) {
                        ObjectFromLdaps.add(ObjectFromLdap);
                    }
                }
            }
        } catch (NamingException e) {
            logger.error("[LDAP] naming exception", e);
            throw new BadRequestException("connection", "connection Error");
        }
    }

    /**
     * 从LdapConfig读取相关配置
     *
     * @param config LDAP配置信息
     * @return Hash table
     */
    @SuppressWarnings("unchecked")
    private Hashtable getEnvConfig(LdapConfig config) {
        if (validateLdapConfig(config)) {
            Hashtable env = new Hashtable();
            String ldapUrl = ENV_LDAP_PROTOCOL_SCHEME + config.getHost() + ":" + config.getPort();
            env.put(Context.INITIAL_CONTEXT_FACTORY, ENV_CONTEXT_FACTORY_CLASS);
            env.put(Context.SECURITY_AUTHENTICATION, ENV_SECURITY_AUTHENTICATION);
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_PRINCIPAL, config.getUsername());
            env.put(Context.SECURITY_CREDENTIALS, config.getPassword());
            env.put(ENV_CONTEXT_CONNECT_TIMEOUT, ENV_CONTEXT_CONNECT_TIMEOUT_TIME);
            return env;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    /**
     * 用户鉴权配置
     *
     * @param config LDAP配置信息
     * @param principal 用户名
     * @param credential 密码
     * @return Hash table
     */
    private Hashtable getEnvConfig(LdapConfig config, String principal, String credential) {
        if (validateLdapConfig(config)) {
            Hashtable env = new Hashtable();
            String ldapUrl = ENV_LDAP_PROTOCOL_SCHEME + config.getHost() + ":" + config.getPort();
            env.put(Context.INITIAL_CONTEXT_FACTORY, ENV_CONTEXT_FACTORY_CLASS);
            env.put(Context.SECURITY_AUTHENTICATION, ENV_SECURITY_AUTHENTICATION);
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_PRINCIPAL, principal);
            env.put(Context.SECURITY_CREDENTIALS, credential);
            env.put(ENV_CONTEXT_CONNECT_TIMEOUT, ENV_CONTEXT_CONNECT_TIMEOUT_TIME);
            return env;
        }

        return null;
    }

    /**
     * 校验LDAP必要配置项：服务器地址 host:port，登录用户名和密码
     *
     * @param config LDAP配置信息
     * @return 校验结果
     */
    private boolean validateLdapConfig(LdapConfig config) {
        if (config == null) {
            return false;
        }

        if (configInvalid(config)) {

            logger.error("[LDAP] LDAP Configuration is wrong");

            return false;
        }
        return true;
    }

    private boolean configInvalid(LdapConfig config) {
        return StringUtil.isEmpty(config.getHost())
            || config.getPort() == 0
            || StringUtil.isEmpty(config.getUsername())
            || StringUtil.isEmpty(config.getPassword());
    }

    private void ldapDisconnect(LdapContext ctx) {
        if (ctx != null) {
            try {
                ctx.close();
            } catch (NamingException e) {
                logger.error("[LDAP] Disconnect LDAP server failed", e);
            }
        }
    }

    private boolean isUser(Attribute objectClassAttr) {
        if (objectClassAttr == null) {
            return false;
        }
        String attrStr = objectClassAttr.toString();
        return (attrStr.contains(USER) || attrStr.contains(PERSON)) && !attrStr.contains(COMPUTER);
    }

    private boolean isOrg(Attribute objectClassAttr) {

        return objectClassAttr != null && objectClassAttr.toString().contains(ORGANIZATIONAL_UNIT);
    }

    /**
     * 将从LDAP获取的用户数据转换成有效的数据结构
     *
     * @param attrs   条目属性
     * @param staffDn staff DN
     * @return 有效的数据结构
     */
    private ObjectFromLdap buildObjectFromLdap(Attributes attrs, String staffDn, LdapConfig ldapConfig) {
        List<String> attrStrs = new ArrayList<>();

        //获取邮箱信息
        if (isNotEmpty(ldapConfig.getMailAttr()) && isNotEmpty(attrs.get(ldapConfig.getMailAttr()))) {
            attrStrs.add(attrs.get(ldapConfig.getMailAttr()).toString());
        }

        //获取电话号码属性
        if (isNotEmpty(ldapConfig.getTelephoneNumberAttr()) && isNotEmpty(attrs.get(ldapConfig.getTelephoneNumberAttr()))) {
            attrStrs.add(attrs.get(ldapConfig.getTelephoneNumberAttr()).toString());
        }

        if (isNotEmpty(attrs.get(ldapConfig.getLdapAccountAttr()))) {
            attrStrs.add(attrs.get(ldapConfig.getLdapAccountAttr()).toString());
        }

        if (isNotEmpty(attrs.get(ldapConfig.getNameAttr()))) {
            attrStrs.add(attrs.get(ldapConfig.getNameAttr()).toString());
        }
        if (isNotEmpty(attrs.get(ldapConfig.getNumberAttr()))) {
            attrStrs.add(attrs.get(ldapConfig.getNumberAttr()).toString());
        }

        return buildObjectFromLdap(attrStrs, staffDn, ldapConfig);
    }

    private ObjectFromLdap buildObjectFromLdap(List<String> attrStrs, String staffDn, LdapConfig ldapConfig) {
        if (isNotEmpty(attrStrs)) {
            ObjectFromLdap ObjectFromLdap = new ObjectFromLdap();

            for (String attr : attrStrs) {

                String attrName = attr.substring(0, attr.indexOf(":"));

                String attrValue = attr.substring(attr.indexOf(": ") + 2);

                if (ldapConfig.getNameAttr().equals(attrName)) {
                    ObjectFromLdap.setName(attrValue);
                }

                if (ldapConfig.getNumberAttr().equals(attrName)) {
                    ObjectFromLdap.setNumber(attrValue);
                }

                if (ldapConfig.getLdapAccountAttr().equals(attrName)) {
                    ObjectFromLdap.setLdapAccountName(attrValue);
                }

                //如果电话号码配置不为空，且属性中有值，则设置电话号码属性
                if (isNotEmpty(ldapConfig.getMailAttr()) && ldapConfig.getMailAttr().equals(attrName)) {
                    ObjectFromLdap.setMail(attrValue);
                }

                //如果电话号码配置不为空，且属性中有值，则设置电话号码属性
                if (isNotEmpty(ldapConfig.getTelephoneNumberAttr()) && ldapConfig.getTelephoneNumberAttr().equals(attrName)) {
                    ObjectFromLdap.setTelephoneNumber(attrValue);
                }

            }

            ObjectFromLdap.setDn(staffDn);

            return ObjectFromLdap;
        }
        return null;
    }

    private String[] getSearchAttrFromConfig(LdapConfig ldapConfig) {
        if (isEmpty(ldapConfig)) {
            return null;
        }

        List<String> attrList = new ArrayList<>();
        attrList.add(ldapConfig.getLdapAccountAttr());
        attrList.add(ldapConfig.getNameAttr());
        attrList.add(ldapConfig.getNumberAttr());
        attrList.add(OBJECT_CLASS);

        if (isNotEmpty(ldapConfig.getMailAttr())){
            attrList.add(ldapConfig.getMailAttr());
        }

        if (isNotEmpty(ldapConfig.getTelephoneNumberAttr())){
            attrList.add(ldapConfig.getTelephoneNumberAttr());
        }

        return attrList.toArray(new String[attrList.size()]);
    }

}
