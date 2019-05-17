package com.alonelyleaf.netty.config;

public class CommonConfig {

    public static boolean epollEnable;

    public static int gatewayServerPort;

    public static int trafficLogReadLimit;

    public static String publicIp;

    // ---------------------------- getter setter -------------------------------

    public void setEpollEnable(boolean epollEnable) {
        CommonConfig.epollEnable = epollEnable;
    }

    public void setGatewayServerPort(int gatewayServerPort) {
        CommonConfig.gatewayServerPort = gatewayServerPort;
    }

    public void setTrafficLogReadLimit(int trafficLogReadLimit) {
        CommonConfig.trafficLogReadLimit = trafficLogReadLimit;
    }

    public void  setPublicIp(String publicIp) {
        CommonConfig.publicIp = publicIp;
    }
}
