package com.alonelyleaf.audit;

import org.aspectj.lang.JoinPoint;

/**
 * @author bijl
 * @date 2019/11/28
 */
public interface AuditInfoProvider {

    /**
     * 成功
     */
    String RESULT_SUCCESS = "SUCCESS";

    /**
     * 失败
     */
    String RESULT_FAIL = "FAIL";

    /**
     * 获取服务名称
     *
     * @return
     */
    String getAppName();

    /**
     * 获取操作者，实现类应该不能抛错
     *
     * @param joinPoint
     * @return
     */
    Object getOperator(JoinPoint joinPoint);

    /**
     * 获取操作详情，实现类应该不能抛错
     *
     * @param joinPoint
     * @param returnResult
     * @param e
     * @return
     */
    Object getDetail(JoinPoint joinPoint, Object returnResult, Exception e);


    /**
     * 获取请求信息
     */
    default Object[] getArgs(JoinPoint joinPoint) {
        return null;
    }

    /**
     * 获取opration
     *
     * @param operation
     * @param target
     * @return
     */
    default String getOperation(String operation, Object target) {return operation; }

    /**
     * 是否需要处理该操作
     * @return
     */
    default boolean needHandle() {
        return true;
    }
}
