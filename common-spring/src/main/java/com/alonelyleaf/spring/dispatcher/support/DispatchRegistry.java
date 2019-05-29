package com.alonelyleaf.spring.dispatcher.support;

import com.alonelyleaf.spring.dispatcher.annotations.DispatchClass;
import com.alonelyleaf.spring.dispatcher.annotations.DispatchMethod;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class DispatchRegistry implements BeanPostProcessor {

    /**
     * 存放type及其对应的接口的信息
     */
    private Map<Integer, OperationContext> registry = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //获得原始类而不是spring代理类的Class对象
        Class clazz = AopProxyUtils.ultimateTargetClass(bean);
        Annotation dispatch = clazz.getAnnotation(DispatchClass.class);
        if (dispatch == null) {
            return bean;
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(DispatchMethod.class)) {
                DispatchMethod dispatchMethod = method.getAnnotation(DispatchMethod.class);
                registry.put(dispatchMethod.value(), new OperationContext(clazz, method, method.getParameterTypes()));
            }
        }

        return bean;
    }

    public Map<Integer, OperationContext> getRegistry() {
        //// TODO:  进行保护性拷贝
        return registry;
    }

    public static class OperationContext {

        /**
         * 接口对应的类的Class对象
         */
        private Class clazz;

        /**
         * 使用到的方法
         */
        private Method method;

        /**
         * 所使用的方法的参数类型
         */
        private Class[] argTypes;

        public Class getClazz() {
            return clazz;
        }

        public OperationContext(Class clazz, Method method, Class[] argTypes) {
            this.clazz = clazz;
            this.method = method;
            this.argTypes = argTypes;
        }

        public OperationContext() {
        }

        //-------------------------------------- getter setter ---------------------------------------------

        public OperationContext setClazz(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Method getMethod() {
            return method;
        }

        public OperationContext setMethod(Method method) {
            this.method = method;
            return this;
        }

        public Class[] getArgTypes() {
            return argTypes;
        }

        public OperationContext setArgTypes(Class[] argTypes) {
            this.argTypes = argTypes;
            return this;
        }
    }
}
