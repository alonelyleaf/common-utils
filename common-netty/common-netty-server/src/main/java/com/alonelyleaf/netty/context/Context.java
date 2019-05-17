package com.alonelyleaf.netty.context;


import com.alonelyleaf.netty.push.PushCenter;
import com.alonelyleaf.netty.router.RouterServer;
import com.alonelyleaf.netty.server.ConnectServer;
import com.alonelyleaf.netty.server.GatewayServer;

public class Context {

    private ConnectServer connectServer;

    private GatewayServer gatewayServer;

    private PushCenter pushCenter;

    private RouterServer routerServer;

    public Context() {

        connectServer = new ConnectServer(this);

        gatewayServer = new GatewayServer(this);

        pushCenter = new PushCenter(this);

    }

    public ConnectServer getConnectServer() {
        return connectServer;
    }

    public Context setConnectServer(ConnectServer connectServer) {
        this.connectServer = connectServer;
        return this;
    }

    public GatewayServer getGatewayServer() {
        return gatewayServer;
    }

    public Context setGatewayServer(GatewayServer gatewayServer) {
        this.gatewayServer = gatewayServer;
        return this;
    }


    public PushCenter getPushCenter() {
        return pushCenter;
    }

    public Context setPushCenter(PushCenter pushCenter) {
        this.pushCenter = pushCenter;
        return this;
    }

    public RouterServer getRouterServer() {
        return routerServer;
    }

    public Context setRouterServer(RouterServer routerServer) {
        this.routerServer = routerServer;
        return this;
    }
}
