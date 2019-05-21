package com.alonelyleaf.netty.websocket.push;

import com.alonelyleaf.netty.websocket.event.WebSocketMessageEvent;
import com.alonelyleaf.netty.websocket.push.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WebSocketMessagePushCenter {

    @Autowired(required = false)
    private Map<String, MessageHandler> handlers;

    public void push(WebSocketMessageEvent event) {

        if (handlers != null) {

            MessageHandler handler = handlers.get(event.getType());

            if (handler == null) {
                return;
            }

            handler.onMessage(event);
        }
    }
}
