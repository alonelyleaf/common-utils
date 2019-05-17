package com.alonelyleaf.netty.message;


import com.alonelyleaf.netty.connection.Connection;

public interface PacketReceiver<T> {

    void onReceive(T packet, Connection connection);
}
