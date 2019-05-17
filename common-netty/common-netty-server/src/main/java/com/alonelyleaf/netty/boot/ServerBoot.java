package com.alonelyleaf.netty.boot;

import com.alonelyleaf.netty.api.listener.Listener;
import com.alonelyleaf.netty.api.service.Service;

public final class ServerBoot extends BootJob {

    private final Service server;

    public ServerBoot(Service server) {
        this.server = server;
    }

    @Override
    public void start() {
        server.init();
        server.start(new Listener() {
            @Override
            public void onSuccess(Object... args) {
                startNext();
            }

            @Override
            public void onFailure(Throwable cause) {
                System.exit(-1);
            }
        });
    }

    @Override
    protected void stop() {
        stopNext();
        //server.stop().join();
    }

    @Override
    protected String getName() {
        return super.getName() + '(' + server.getClass().getSimpleName() + ')';
    }
}
