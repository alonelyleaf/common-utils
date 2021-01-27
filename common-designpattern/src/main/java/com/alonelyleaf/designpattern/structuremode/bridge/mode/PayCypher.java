package com.alonelyleaf.designpattern.structuremode.bridge.mode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码支付
 *
 * @author bijl
 * @date 2020/12/22 下午5:00
 */
public class PayCypher implements IPayMode{

    protected Logger logger = LoggerFactory.getLogger(PayCypher.class);

    public boolean security(String uId) {
        logger.info("密码⽀支付，⻛风控校验环境安全");
        return true;
    }
}
