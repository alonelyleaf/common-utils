package com.alonelyleaf.netty.websocket.push.handler;

import com.alonelyleaf.netty.websocket.constant.Constant;
import com.alonelyleaf.netty.websocket.event.WebSocketMessageEvent;
import org.springframework.stereotype.Component;

@Component(Constant.EvenType.COMMAND_HANDLER)
public class commandHandler extends AbstractMessageHandler {

    @Override
    protected void handle(WebSocketMessageEvent event) {

        if (host.equals(event.getHost())) {

            webSocketService.send(Constant.EvenType.COMMAND_HANDLER, event.getConnectionId(), event.getContent());
        }

    }
}
