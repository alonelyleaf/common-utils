package com.alonelyleaf.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_TEST = "test";
    public static final String PROFILE_UNIT_TEST = "unit-test";
    public static final String PROFILE_DEV_LINK_TEST = "dev-link-test";

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static String getActiveProfile() {
        String[] profiles = getActiveProfiles();

        if (ValidateUtil.isEmpty(profiles)) {
            return null;
        }

        return profiles[0];
    }

    public static boolean isDevProfile() {
        return PROFILE_DEV.equals(getActiveProfile()) || PROFILE_DEV_LINK_TEST.equals(getActiveProfile());
    }

    public static boolean isTestProfile() {
        return PROFILE_TEST.equals(getActiveProfile());
    }

    // ============================== private method ==============================

    private static String[] getActiveProfiles() {
        return context.getEnvironment().getActiveProfiles();
    }
}
