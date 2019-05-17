package com.alonelyleaf.netty.messagehandler;


import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.message.MessageHandler;

public class HeartbeatHandler implements MessageHandler<Packet> {

    @Override
    public void handle(Packet packet, Connection connection) {
        
        connection.send(packet.setResponseResult(0).setReqFlag(false));
    }
}
