package com.alonelyleaf.designpattern.structuremode.bridge.mode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 刷脸支付
 *
 * @author bijl
 * @date 2020/12/22 下午4:57
 */
public class PayFaceMode implements IPayMode{

    protected Logger logger = LoggerFactory.getLogger(PayFaceMode.class);

    public boolean security(String uId) {
        logger.info("⼈人脸⽀支付，⻛风控校验脸部识别");
        return true;
    }
}
