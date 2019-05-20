package com.alonelyleaf.netty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonConfig {

    public static boolean epollEnable;

    public static int gatewayServerPort;

    public static int trafficLogReadLimit;

    public static String publicIp;

    // ---------------------------- getter setter -------------------------------

    @Value("${epoll.enable}")
    public void setEpollEnable(boolean epollEnable) {
        CommonConfig.epollEnable = epollEnable;
    }

    @Value("${gateway.server.port}")
    public void setGatewayServerPort(int gatewayServerPort) {
        CommonConfig.gatewayServerPort = gatewayServerPort;
    }

    @Value("${traffic.readLimit}")
    public void setTrafficLogReadLimit(int trafficLogReadLimit) {
        CommonConfig.trafficLogReadLimit = trafficLogReadLimit;
    }

    @Value("${net.public-ip}")
    public void  setPublicIp(String publicIp) {
        CommonConfig.publicIp = publicIp;
    }
}
