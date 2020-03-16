package com.alonelyleaf.spring.upgradescript;

import com.alonelyleaf.util.JStopWatch;
import com.alonelyleaf.util.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpgradeScriptInitializeSchedule {

    @Autowired
    private UpgradeScriptInitializeHandler upgradeScriptInitializeHandler;

    private static final Logger logger = LoggerFactory.getLogger(UpgradeScriptInitializeSchedule.class);

    @Value("${com.alonelyleaf.spring.upgradescript.UpgradeScriptInitializeSchedule.enable:false}")
    private Boolean enable;

    @Value("${com.alonelyleaf.spring.upgradescript.UpgradeScriptInitializeSchedule.lockTime:30}")
    private int lockTime;

    @Scheduled(cron = "${com.alonelyleaf.spring.upgradescript.UpgradeScriptInitializeSchedule.cron:0 * * * * ?}")
    public void exec() {

        //TODO 进行定时任务锁定
        run();
    }

    private void run() {
        try {
            if (ValidateUtil.isNotEmpty(enable) && enable) {
                JStopWatch stopWatch = new JStopWatch();

                logger.info("[SCHEDULE] start UpgradeScriptInitializeSchedule");
                boolean result = upgradeScriptInitializeHandler.handle();
                if (result) {
                    enable = false;
                }
                logger.info("[SCHEDULE] end UpgradeScriptInitializeSchedule, elapse {} ms", stopWatch.elapsed());
            }
        } catch (Exception e) {
            logger.error("UpgradeScriptInitializeSchedule error,{}", e.getMessage(), e);
        }
    }
}
