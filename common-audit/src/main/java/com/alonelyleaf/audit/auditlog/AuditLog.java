package com.alonelyleaf.audit.auditlog;

/**
 * @author bijl
 * @date 2019/11/28
 */
public interface AuditLog {

    String getId();

    /**
     * 因为系统名称
     *
     * @return
     */
    String getAppName();


    /**
     * 操作名称，如果需要国际化请传key
     *
     * @return
     */
    String getOperation();

    /**
     * 操作模块，如果需要国际化请传key
     *
     * @return
     */
    String getModule();

    /**
     * 操作者
     *
     * @return
     */
    Object getOperator();

    /**
     * client ip
     *
     * @return
     */
    String getIp();

    /**
     * 操作对象
     *
     * @return
     */
    Object getTarget();


    /**
     * 操作结果
     *
     * @return
     */
    String getResult();

    /**
     * 操作详情
     *
     * @return
     */
    Object getDetail();

    /**
     * 创建时间
     *
     * @return
     */
    Long getCreateTime();
}
