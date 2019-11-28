package com.alonelyleaf.audit.schedule;

import com.alonelyleaf.audit.auditlog.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author bijl
 * @date 2019/11/28
 */
@Component
public class AuditLogClearSchedule {

    @Autowired
    private AuditRepository auditRepository;

    @Value("${audit.schedule.AuditLogClearSchedule.ttl: 30}")
    private Integer day;

    @Scheduled(cron = " 0 0 0 * ?")
    public void exec(){

        auditRepository.clear(calcTime(day));
    }

    private Long calcTime(Integer day) {
        return System.currentTimeMillis() - (24 * 3600 * day);
    }
}
