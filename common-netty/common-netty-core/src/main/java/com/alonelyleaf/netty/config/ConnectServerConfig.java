package com.alonelyleaf.netty.config;

public class ConnectServerConfig {

    public static int port;

    public static int bossNumber;

    public static int soBacklog;

    public static int sndBuf;

    public static int rcvBuf;

    public static boolean noDelay;

    // ---------------------------- getter setter -------------------------------

    public void setPort(int port) {
        ConnectServerConfig.port = port;
    }

    public void setBossNumber(int bossNumber) {
        ConnectServerConfig.bossNumber = bossNumber;
    }

    public void setSoBacklog(int soBacklog) {
        ConnectServerConfig.soBacklog = soBacklog;
    }

    public void setSndBuf(int sndBuf) {
        ConnectServerConfig.sndBuf = sndBuf;
    }

    public void setRcvBuf(int rcvBuf) {
        ConnectServerConfig.rcvBuf = rcvBuf;
    }

    public void setNoDelay(boolean noDelay) {
        ConnectServerConfig.noDelay = noDelay;
    }
}
