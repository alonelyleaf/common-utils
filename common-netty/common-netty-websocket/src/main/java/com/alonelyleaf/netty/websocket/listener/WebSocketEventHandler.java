package com.alonelyleaf.netty.websocket.listener;

import com.alonelyleaf.netty.websocket.connection.Connection;
import com.alonelyleaf.netty.websocket.connection.ConnectionCache;
import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.service.WebSocketBaseService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class WebSocketEventHandler extends WebSocketBaseService {

    @Autowired
    private ConnectionCache cache;

    /**
     * receive event by event name
     *
     * @param client
     */
    @OnEvent(Constant.EvenType.CONNECTION_TEST)
    public void unreadMessage(SocketIOClient client, String message) {

        Connection connection = getConnection(client);

        if (isEmpty(connection)) {
            return;
        }

        connection.send(Constant.EvenType.CONNECTION_TEST, message);
    }

    private Connection getConnection(SocketIOClient client) {

        Connection connection = cache.get(client.getSessionId().toString());

        if (isEmpty(connection)) {
            client.disconnect();
            return null;
        }

        if (!isAuth(connection.getToken())) {
            cache.removeAndDisconnect(connection);
            return null;
        }

        return connection;
    }

}
