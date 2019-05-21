package com.alonelyleaf.netty.websocket.push.handler;

import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.event.WebSocketMessageEvent;
import org.springframework.stereotype.Component;

@Component(Constant.EvenType.MESSAGE)
public class UnreadMessageHandler extends AbstractMessageHandler {

    @Override
    protected void handle(WebSocketMessageEvent event) {

        webSocketService.broadcast(Constant.EvenType.MESSAGE, null);

    }
}
