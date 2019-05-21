package com.alonelyleaf.netty.websocket.push.handler;

import com.alonelyleaf.netty.websocket.event.WebSocketMessageEvent;

public interface MessageHandler {

    void onMessage(WebSocketMessageEvent event);
}
