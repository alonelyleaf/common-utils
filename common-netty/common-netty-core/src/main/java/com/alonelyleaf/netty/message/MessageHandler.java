package com.alonelyleaf.netty.message;


import com.alonelyleaf.netty.connection.Connection;

/**
 * 消息处理器
 */
public interface MessageHandler<T> {

    void handle(T packet, Connection connection);
}
