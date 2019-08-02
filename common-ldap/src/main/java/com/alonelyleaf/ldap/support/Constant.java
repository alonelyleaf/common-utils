package com.alonelyleaf.ldap.support;

/**
 * @author bijl
 * @date 2019/7/31
 */
public interface Constant {

    interface Ldap {

        class Env {

            public static final String ENV_CONTEXT_FACTORY_CLASS = "com.sun.jndi.ldap.LdapCtxFactory";

            public static final String ENV_CONTEXT_CONNECT_TIMEOUT = "com.sun.jndi.ldap.connect.timeout";

            public static final String ENV_CONTEXT_CONNECT_TIMEOUT_TIME = "1000"; //连接超时时间1s

            public static final String ENV_SECURITY_AUTHENTICATION = "simple";

            public static final String ENV_LDAP_PROTOCOL_SCHEME = "ldap://";
        }


        class SearchFilter {

            public static final String LDAP_SEARCH_FILTER = "objectClass=*";

            public static final String LDAP_CHANGED_SEARCH_FILTER = "(&(objectClass=*)(whenChanged>=%s)(whenChanged<=%s))";

        }

        class ObjectClass {
            public static final String OBJECT_CLASS = "objectClass";

            public static final String USER = "User";
            public static final String PERSON = "Person";
            public static final String COMPUTER = "computer";
            public static final String ORGANIZATIONAL_UNIT = "organizationalUnit";
        }

        class Attribute {
            public static final String LDAP_ATTRIBUTE_MAIL = "mail";
        }

        class LdapAuthenticationType {

            public static final String AUTHENTICATION_TYPE_SIMPLE = "simple";

            public static final String AUTHENTICATION_TYPE_MD5 = "DIGEST-MD5";
        }

    }
}
