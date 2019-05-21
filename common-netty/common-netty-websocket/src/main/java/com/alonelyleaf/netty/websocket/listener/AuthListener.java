package com.alonelyleaf.netty.websocket.listener;

import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.service.WebSocketBaseService;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;

public class AuthListener extends WebSocketBaseService implements AuthorizationListener {

    @Override
    public boolean isAuthorized(HandshakeData handshakeData) {

        String token = handshakeData.getHttpHeaders().get(Constant.WebSocket.TOKEN_HEADER);

        return isAuth(token);
    }
}
