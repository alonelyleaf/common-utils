package com.alonelyleaf.netty.websocket.listener;

import com.alonelyleaf.netty.websocket.connection.Connection;
import com.alonelyleaf.netty.websocket.connection.ConnectionCache;
import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.service.WebSocketBaseService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.PingListener;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.ObjectUtils.isEmpty;

public class WebSocketPingListener extends WebSocketBaseService implements PingListener {

    @Autowired
    private ConnectionCache cache;

    @Override
    public void onPing(SocketIOClient client) {

        Connection connection = cache.get(client.getSessionId().toString());

        if (isEmpty(connection)) {
            client.disconnect();
            return;
        }

        if (!isAuth(connection.getToken())) {
            connection.send(Constant.EvenType.TOKEN_INVALID, null);
            cache.removeAndDisconnect(connection);

        }
    }
}
