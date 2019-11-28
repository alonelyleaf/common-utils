package com.alonelyleaf.audit.auditlog.impl;

import com.alonelyleaf.audit.auditlog.AuditRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author bijl
 * @date 2019/11/28
 */
@Component
public class AuditLogRepository implements AuditRepository {

    @Override
    public void save(String appName, String operation, String module, Object operator, Object target, String result, Object detail, String ip, Object[] args) {

        AuditLogEntity audit = new AuditLogEntity()
                .setAppName(appName)
                .setIp(ip)
                .setOperator(operator)
                .setTarget(target)
                .setOperation(operation)
                .setModule(module)
                .setResult(result)
                .setDetail(detail)
                .setArgs(args);

        //TODO 保存到数据库
    }
    /**
     * 删除指定时间之前的数据
     *
     * @param ttl
     */
    public void clear(Long ttl){

        //TODO 根据时间删除记录
    }
}
