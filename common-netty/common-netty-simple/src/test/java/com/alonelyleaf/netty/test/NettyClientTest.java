package com.alonelyleaf.netty.test;

import org.junit.Test;

/**
 * @author bijl
 * @date 2019/10/28
 */
public class NettyClientTest {

    @Test
    public void test() throws InterruptedException {
        new NettyClient().connect(9001, "127.0.0.1");
    }

}
