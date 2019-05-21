package com.alonelyleaf.netty.websocket.config;

import com.alonelyleaf.netty.websocket.listener.AuthListener;
import com.alonelyleaf.netty.websocket.listener.WebSocketConnectListener;
import com.alonelyleaf.netty.websocket.listener.WebSocketDisconnectListener;
import com.alonelyleaf.netty.websocket.listener.WebSocketPingListener;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.PingListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class WebSocketConfig {

    @Value("${websocket.host:localhost}")
    private String host;

    @Value("${websocket.port:9900}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {

        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin(":*:");
        config.setTransports(Transport.WEBSOCKET);
        config.setAuthorizationListener(authorizationListener());

        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);

        config.setSocketConfig(socketConfig);

        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(connectListener());
        server.addDisconnectListener(disconnectListener());
        server.addPingListener(pingListener());

        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(socketIOServer());
    }

    @Bean
    public WebSocketRunner webSocketRunner() {

        return new WebSocketRunner();
    }

    // ------------------------------------------- listener ---------------------------------------------

    @Bean
    public AuthorizationListener authorizationListener() {

        return new AuthListener();
    }

    @Bean
    public ConnectListener connectListener() {

        return new WebSocketConnectListener();
    }

    @Bean
    public DisconnectListener disconnectListener() {

        return new WebSocketDisconnectListener();
    }

    @Bean
    public PingListener pingListener(){

        return new WebSocketPingListener();
    }
}
