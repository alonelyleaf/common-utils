package com.alonelyleaf.spring.bean;

import com.alonelyleaf.util.ValidateUtil;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bijl
 * @date 2020/1/10
 */
public class BeanBuilder {

    private static Map<String, Object> beanMap = new HashMap<>();

    private static ConfigurableApplicationContext applicationContext;

    private static DefaultListableBeanFactory beanFactory;

    private static final BeanBuilder INSTANCE = new BeanBuilder();

    public static BeanBuilder build(ApplicationContext context) {
        if (applicationContext == null) {
            applicationContext = (ConfigurableApplicationContext) context;
            beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        }
        return INSTANCE;
    }

    /**
     * 获取bean
     *
     * @param interfaceClass
     * @return
     */
    public Object getBean(Class interfaceClass) {

        return  beanMap.get(interfaceClass.getCanonicalName());
    }

    /**
     * 注册bean
     * //fixme
     * beanFactory.getBean(implClazz.getName(), implClazz)
     * beanFactory.getBean(implClazz)
     */
    private Object registerBean(Class implClazz, Class interfaceClass) {

        //移除已存在的bean
        removeBean(interfaceClass);

        BeanDefinitionBuilder classBeanBuilder = BeanDefinitionBuilder.rootBeanDefinition(implClazz);

        beanFactory.registerBeanDefinition(implClazz.getName(), classBeanBuilder.getBeanDefinition());

        Object bean = beanFactory.getBean(implClazz.getName(), implClazz);

        beanMap.put(interfaceClass.getCanonicalName(), bean);

        return bean;
    }


    private void removeBean(Class interfaceClass){
        //如果对于bean已存在，则先移除
        if (ValidateUtil.isNotEmpty(beanMap.get(interfaceClass.getCanonicalName()))){

            Object bean = beanMap.get(interfaceClass.getCanonicalName());
            // 此处需要获得原始类而不是spring代理类的Class对象
            beanFactory.removeBeanDefinition(AopProxyUtils.ultimateTargetClass(bean).getName());
            beanMap.remove(interfaceClass.getCanonicalName());
        }
    }
}
