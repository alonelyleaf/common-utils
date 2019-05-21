package com.alonelyleaf.netty.websocket.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;

@DependsOn("socketIOServer")
public class WebSocketRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketRunner.class);

    @Autowired
    private SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {

        server.start();

        logger.info("[WEB SOCKET RUNNER] start web socket success!");
    }
}
