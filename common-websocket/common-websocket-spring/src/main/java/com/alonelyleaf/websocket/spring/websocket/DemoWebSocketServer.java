package com.alonelyleaf.websocket.spring.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * webscoket 服务
 *
 * @author bijl
 * @date 2020/12/10 下午4:09
 */
@Component
@ServerEndpoint(value = "/webSocketServer/{token}")
public class DemoWebSocketServer {

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {

    }

    @OnMessage
    public String onMessage(String param, Session session) {

        return "";
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {

    }

    @OnError
    public void onError(Throwable throwable) {

    }
}
