package com.alonelyleaf.audit;

import com.alonelyleaf.audit.annotation.AuditOperator;
import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.MapUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.exception.business.BadRequestException;
import com.alonelyleaf.util.exception.business.BusinessException;
import com.alonelyleaf.util.result.Result;
import com.alonelyleaf.util.result.error.Error;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bijl
 * @date 2019/11/28
 */
public class DefaultAuditInfoProvider implements AuditInfoProvider {

    @Configuration
    public static class DefaultAuditInfoProviderAutoConfiguration {
        @Bean
        @ConditionalOnMissingBean(AuditInfoProvider.class)
        DefaultAuditInfoProvider defaultAuditInfoProvider() {
            return new DefaultAuditInfoProvider();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(DefaultAuditInfoProvider.class);

    @Override
    public String getAppName() {
        return "";
    }

    @Override
    public Object getOperator(JoinPoint joinPoint) {
        Object operator = getOperatorFromAnnotation(AuditAspect.getMethod(joinPoint), joinPoint.getArgs());
        if (ValidateUtil.isEmpty(operator)) {
            operator = getOperatorFromSubject();
        }
        return operator;
    }

    /**
     * 获取操作结果详情
     *
     * @param joinPoint
     * @param returnResult
     * @param e
     * @return
     */
    @Override
    public Object getDetail(JoinPoint joinPoint, Object returnResult, Exception e) {
        if (ValidateUtil.isNotEmpty(e)) {
            return getExceptionDetail(e);
        }
        return returnResult;
    }

    @Override
    public String getOperation(String operation, Object target) {
        return operation;
    }
//------------------------------------impl---------------------------------------------

    /**
     * 从session中获取操作者
     *
     * @return
     */
    protected Object getOperatorFromSubject() {
        try {
            //TODO 从session中获取操作者
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 从注解上获取操作者
     *
     * @param method
     * @param args
     * @return
     */
    protected Object getOperatorFromAnnotation(Method method, Object[] args) {

        if (ValidateUtil.isEmpty(args) || ValidateUtil.isEmpty(method) || method.getParameterAnnotations().length <= 0) {
            return null;
        }

        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        //遍历查找@AuditOperator标记的参数
        for (int i = 0; i < paramAnnotations.length; i++) {

            for (int j = 0; j < paramAnnotations[i].length; j++) {

                if (paramAnnotations[i][j] instanceof AuditOperator) {

                    AuditOperator auditOperator = (AuditOperator) paramAnnotations[i][j];

                    if (ValidateUtil.isEmpty(auditOperator.field())) {
                        return args[i];
                    }

                    if (args[i] instanceof Map) {
                        return MapUtil.fromBean(args[i]).get(auditOperator.field());
                    }

                    return BeanUtil.pojo.getProperty(args[i], auditOperator.field());
                }
            }
        }
        return null;
    }

    /**
     * 获取异常操作结果详情
     *
     * @param e
     * @return
     */
    protected Object getExceptionDetail(Exception e) {
        if (e instanceof BadRequestException) {

            BadRequestException be = (BadRequestException) e;

            Result result = new Result().error(new Error().setErrorCode(be.getCode())
                    .setFieldErrors(be.getFieldErrors()));

            return result;
        }

        if (e instanceof BusinessException) {

            BusinessException be = (BusinessException) e;

            Result result = new Result().error(new Error().setErrorCode(be.getCode()).setMsg(e.getMessage()));

            return result;
        }

        return e.getMessage();
    }

    @Override
    public Object[] getArgs(JoinPoint joinPoint) {

        Object[] objectArgs = joinPoint.getArgs();
        if (objectArgs == null) {
            return null;
        }

        List argsExceptBindingResult = new ArrayList(java.util.Arrays.asList(objectArgs));
        argsExceptBindingResult.removeIf(o -> o instanceof BindingResult || o instanceof HttpServletResponse || o instanceof HttpServletRequest);

        return argsExceptBindingResult.toArray();
    }

}
