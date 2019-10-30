package com.alonelyleaf.ldap;

import com.alonelyleaf.ldap.support.LdapConfig;
import com.alonelyleaf.ldap.util.LdapSupport;
import com.alonelyleaf.ldap.vo.ObjectFromLdap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonLdapApplicationTests {

    @Autowired
    private LdapSupport ldapSupport;

    @Test
    public void contextLoads() {

        LdapConfig ldapConfig = ldapSupport.getLdapConfig();

        List<ObjectFromLdap> objectFromLdaps = ldapSupport.getObjectFromLdaps(ldapConfig);

        if (objectFromLdaps.size() > 0){
            objectFromLdaps.forEach(objectFromLdap -> {
                System.out.println(objectFromLdap.toString());
            });
        }
    }

}
