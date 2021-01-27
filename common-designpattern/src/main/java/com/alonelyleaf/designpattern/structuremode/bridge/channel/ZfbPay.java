package com.alonelyleaf.designpattern.structuremode.bridge.channel;

import com.alonelyleaf.designpattern.structuremode.bridge.mode.IPayMode;

import java.math.BigDecimal;

/**
 * 支付宝支付
 *
 * @author bijl
 * @date 2020/12/22 下午4:56
 */
public class ZfbPay extends Pay {

    public ZfbPay(IPayMode payMode) {
        super(payMode);
    }

    public String transfer(String uId, String tradeId, BigDecimal amount)
    {
        logger.info("模拟⽀支付宝渠道⽀支付划账开始。uId:{} tradeId:{} amount: {}", uId, tradeId, amount);
        boolean security = payMode.security(uId);
        logger.info("模拟⽀支付宝渠道⽀支付⻛风控校验。uId:{} tradeId:{} security: {}", uId, tradeId, security);
        if (!security) {
            logger.info("模拟⽀支付宝渠道⽀支付划账拦截。uId:{} tradeId:{} amount:{}", uId, tradeId, amount);
            return "0001";
        }
        logger.info("模拟⽀支付宝渠道⽀支付划账成功。uId:{} tradeId:{} amount: {}", uId, tradeId, amount);
        return "0000";
    }
}
