package com.alonelyleaf.netty.websocket.listener;

import com.alonelyleaf.netty.websocket.connection.ConnectionCache;
import com.alonelyleaf.netty.websocket.service.WebSocketBaseService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;

public class WebSocketDisconnectListener extends WebSocketBaseService implements DisconnectListener {

    @Autowired
    private ConnectionCache cache;

    @Override
    public void onDisconnect(SocketIOClient client) {

        cache.remove(client.getSessionId().toString());

    }
}
