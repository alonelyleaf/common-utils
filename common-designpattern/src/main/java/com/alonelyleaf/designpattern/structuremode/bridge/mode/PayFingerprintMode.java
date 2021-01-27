package com.alonelyleaf.designpattern.structuremode.bridge.mode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指纹支付
 *
 * @author bijl
 * @date 2020/12/22 下午4:59
 */
public class PayFingerprintMode implements IPayMode{

    protected Logger logger = LoggerFactory.getLogger(PayFingerprintMode.class);

    public boolean security(String uId) {
        logger.info("指纹⽀支付，⻛风控校验指纹信息");
        return true;
    }
}
