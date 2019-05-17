package com.alonelyleaf.netty.boot;


import com.alonelyleaf.netty.context.Context;

public class ServerLauncher {

    private BootChain chain;

    public void init() throws Exception {

        if (chain == null) {
            chain = new BootChain();
        }

        Context context = new Context();

        chain.boot()
                .setNext(new ServerBoot(context.getConnectServer()))
                .setNext(new ServerBoot(context.getGatewayServer()))
                .end();
    }

    public void start() {
        chain.start();
    }

    public void stop() {
        chain.stop();
    }

    public BootChain getChain() {
        return chain;
    }
}
