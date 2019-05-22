package com.alonelyleaf.netty.websocket.listener;

import com.alonelyleaf.netty.websocket.connection.ConnectionCache;
import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.service.WebSocketBaseService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.util.StringUtils.isEmpty;

public class WebSocketConnectListener extends WebSocketBaseService implements ConnectListener {

    @Autowired
    private ConnectionCache cache;

    @Override
    public void onConnect(SocketIOClient socketIOClient) {

        String token = socketIOClient.getHandshakeData().getHttpHeaders().get(Constant.WebSocket.TOKEN_HEADER);

        if (isEmpty(token) || !isAuth(token)){
            return;
        }

        //每个企业一个room，用于群发
        socketIOClient.joinRoom(Constant.SocketIOClientRoom.room);

        cache.add(socketIOClient, token);
    }
}
