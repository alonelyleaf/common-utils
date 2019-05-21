package com.alonelyleaf.netty.websocket.constant;

/**
 * @author bijl
 * @date 2019/5/21
 */
public interface Constant {

    class WebSocket{

        public static final String TOKEN_HEADER = "token";
    }

    class SocketIOClientRoom{

        public static final String room = "";
    }

    class EvenType {

        /**
         * 未读消息
         */
        public static final String MESSAGE = "unreadMessageHandler";

        /**
         * 告警
         */
        public static final String COMMAND_HANDLER = "commandHandler";

        /**
         * token失效
         */
        public static final String TOKEN_INVALID = "tokenInvalidHandler";

        /**
         * echo
         */
        public static final String CONNECTION_TEST = "connectTestHandler";
    }
}
