package com.alonelyleaf.audit.auditlog;

/**
 * @author bijl
 * @date 2019/11/28
 */
public interface AuditRepository {

    /**
     * 保存操作日志
     *
     * @param appName
     * @param operation
     * @param module
     * @param operator
     * @param target
     * @param result
     * @param detail
     * @param ip
     * @param args
     */
    default void save(String appName, String operation, String module, Object operator, Object target, String result, Object detail, String ip, Object[] args) {

    }

    /**
     * 删除指定时间之前的数据
     *
     * @param ttl
     */
    void clear(Long ttl);
}
