package com.alonelyleaf.netty.push;


import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.connection.Connection;

public class PushMessage {

    // 到GatewayClient的连接，不是到终端的连接
    private Connection connection;

    private Message.GatewayMsg msg;

    public PushMessage(Connection connection, Message.GatewayMsg gatewayMsg) {

        this.connection = connection;
        this.msg = gatewayMsg;
    }


    public Connection getConnection() {
        return connection;
    }

    public PushMessage setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public Message.GatewayMsg getMsg() {
        return msg;
    }

    public PushMessage setMsg(Message.GatewayMsg msg) {
        this.msg = msg;
        return this;
    }
}
