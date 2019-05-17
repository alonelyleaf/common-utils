package com.alonelyleaf.netty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectServerConfig {

    public static int port;

    public static int bossNumber;

    public static int soBacklog;

    public static int sndBuf;

    public static int rcvBuf;

    public static boolean noDelay;

    public static boolean redirect;

    // ---------------------------- getter setter -------------------------------

    @Value("${connect.server.port}")
    public void setPort(int port) {
        ConnectServerConfig.port = port;
    }

    @Value("${connect.server.bossNumber}")
    public void setBossNumber(int bossNumber) {
        ConnectServerConfig.bossNumber = bossNumber;
    }

    @Value("${connect.server.soBacklog}")
    public void setSoBacklog(int soBacklog) {
        ConnectServerConfig.soBacklog = soBacklog;
    }

    @Value("${connect.server.snd-buf}")
    public void setSndBuf(int sndBuf) {
        ConnectServerConfig.sndBuf = sndBuf;
    }

    @Value("${connect.server.rcv-buf}")
    public void setRcvBuf(int rcvBuf) {
        ConnectServerConfig.rcvBuf = rcvBuf;
    }

    @Value("${connect.server.tcp-no-delay}")
    public void setNoDelay(boolean noDelay) {
        ConnectServerConfig.noDelay = noDelay;
    }

    @Value("${connect.server.redirect:false}")
    public void setRedirect(boolean redirect) {
        ConnectServerConfig.redirect = redirect;
    }
}
