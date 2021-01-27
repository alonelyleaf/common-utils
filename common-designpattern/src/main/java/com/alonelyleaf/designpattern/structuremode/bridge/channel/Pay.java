package com.alonelyleaf.designpattern.structuremode.bridge.channel;

import com.alonelyleaf.designpattern.structuremode.bridge.mode.IPayMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author bijl
 * @date 2020/12/22 下午4:54
 */

public abstract class Pay {
    protected Logger logger = LoggerFactory.getLogger(Pay.class);

    protected IPayMode payMode;

    public Pay(IPayMode payMode) {
        this.payMode = payMode;
    }

    public abstract String transfer(String uId, String tradeId, BigDecimal amount);
}
