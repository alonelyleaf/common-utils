package com.alonelyleaf.audit;

import com.alonelyleaf.audit.annotation.Audit;
import com.alonelyleaf.audit.annotation.AuditTarget;
import com.alonelyleaf.audit.auditlog.AuditRepository;
import com.alonelyleaf.util.BeanUtil;
import com.alonelyleaf.util.MapUtil;
import com.alonelyleaf.util.ServletUtil;
import com.alonelyleaf.util.ValidateUtil;
import com.alonelyleaf.util.result.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author bijl
 * @date 2019/11/28
 */

@Aspect
@Component
public class AuditAspect {

    @Value("${audit.record.detail.enable:true}")
    private boolean recordDetail;

    @Autowired
    private AuditRepository service;

    @Autowired
    private AuditInfoProvider auditInfoProvider;

    @Pointcut("@annotation(com.alonelyleaf.audit.annotation.Audit)")
    public void auditPointcut() {
    }

    /**
     * 方法执行后
     *
     * @param joinPoint 切点
     * @param result    方法返回结果
     */
    @AfterReturning(pointcut = "auditPointcut()", returning = "result")
    public void handle(JoinPoint joinPoint, Object result) {
        handleAudit(joinPoint, result, null);
    }

    /**
     * 抛出异常后
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "auditPointcut()", throwing = "e")
    public void handleException(JoinPoint joinPoint, Exception e) {
        handleAudit(joinPoint, null, e);
    }

    protected void handleAudit(JoinPoint joinPoint, Object returnResult, Exception e) {

        if (!auditInfoProvider.needHandle()) {
            return;
        }

        Audit audit = isAudit(joinPoint);
        if (audit == null) {
            return;
        }

        Object detail = recordDetail ? getDetail(joinPoint, returnResult, e) : null;
        Object[] args = recordDetail ? getArgs(joinPoint) : null;
        Object target = getTarget(joinPoint);

        service.save(getAppName(),
                getOperation(audit.operation(), target),
                audit.module(),
                getOperator(joinPoint),
                target,
                getResult(returnResult, e),
                detail,
                ServletUtil.getClientIP(),
                args
        );
    }

    protected Object[] getArgs(JoinPoint joinPoint) {

        return auditInfoProvider.getArgs(joinPoint);

    }

    protected String getAppName() {
        return auditInfoProvider.getAppName();
    }

    protected Object getOperator(JoinPoint joinPoint) {
        return auditInfoProvider.getOperator(joinPoint);
    }

    protected Object getDetail(JoinPoint joinPoint, Object returnResult, Exception e) {
        return auditInfoProvider.getDetail(joinPoint, returnResult, e);
    }


    /**
     * 获取操作对象
     *
     * @param joinPoint
     * @return
     */
    protected Object getTarget(JoinPoint joinPoint) {
        return getTarget(getMethod(joinPoint), joinPoint.getArgs());
    }

    /**
     * 获取操作对象
     *
     * @param method
     * @param args
     * @return
     */
    protected Object getTarget(Method method, Object[] args) {

        if (ValidateUtil.isEmpty(args) || ValidateUtil.isEmpty(method) || method.getParameterAnnotations().length <= 0) {
            return null;
        }

        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        //遍历查找@AuditTarget标记的参数
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (int j = 0; j < paramAnnotations[i].length; j++) {

                if (paramAnnotations[i][j] instanceof AuditTarget) {

                    AuditTarget auditTarget = (AuditTarget) paramAnnotations[i][j];

                    if (ValidateUtil.isEmpty(auditTarget.field())) {
                        return args[i];
                    }

                    if (args[i] instanceof Map) {
                        return MapUtil.fromBean(args[i]).get(auditTarget.field());
                    }

                    return BeanUtil.pojo.getProperty(args[i], auditTarget.field());
                }
            }
        }

        return null;
    }

    /**
     * 获取操作结果
     *
     * @param returnResult
     * @param e
     * @return
     */
    protected String getResult(Object returnResult, Exception e) {

        //存在异常
        if (ValidateUtil.isNotEmpty(e)) {
            return AuditInfoProvider.RESULT_FAIL;
        }

        //Result对象中的ret<0即失败
        if (returnResult != null && returnResult instanceof Result) {
            Result result = (Result) returnResult;
            if (result.getRet() < 0) {
                return AuditInfoProvider.RESULT_FAIL;
            }
        }

        return AuditInfoProvider.RESULT_SUCCESS;
    }

    protected String getOperation(String operation, Object target) {

        return auditInfoProvider.getOperation(operation, target);
    }

    /**
     * 是否存在审计注解
     *
     * @param joinPoint
     * @return
     */
    private Audit isAudit(JoinPoint joinPoint) {
        if (getMethod(joinPoint) == null) {
            return null;
        }
        return getMethod(joinPoint).getAnnotation(Audit.class);
    }

    public static Method getMethod(JoinPoint joinPoint) {

        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        return method;
    }
}
