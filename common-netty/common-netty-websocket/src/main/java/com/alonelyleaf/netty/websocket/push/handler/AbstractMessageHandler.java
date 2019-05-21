package com.alonelyleaf.netty.websocket.push.handler;

import com.alonelyleaf.netty.websocket.event.WebSocketMessageEvent;
import com.alonelyleaf.netty.websocket.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.util.ObjectUtils.isEmpty;


public abstract class AbstractMessageHandler implements MessageHandler {

    @Value("${websocket.host:localhost}")
    protected String host;

    @Autowired
    protected WebSocketService webSocketService;

    protected Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);

    @Override
    public void onMessage(WebSocketMessageEvent event) {

        if (isEmpty(event)) {
            return;
        }

        logger.info("[ABSTRACT MESSAGE HANDLER] type: {} uuid: {} host: {}", event.getType(), event.getUuid(), event.getHost());

        handle(event);

    }

    protected abstract void handle(WebSocketMessageEvent event);

}
