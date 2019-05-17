package com.alonelyleaf.netty.connection.impl;


import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.connection.ConnectionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NettyConnectionManager<T> implements ConnectionManager<T> {

    private final ConcurrentMap<ChannelId, Connection<T>> connections = new ConcurrentHashMap<>();

    @Override
    public Connection<T> get(Channel channel) {
        return connections.get(channel.id());
    }

    @Override
    public Connection<T> removeAndClose(Channel channel) {

        Connection connection = connections.get(channel.id());

        if (connection == null){
            return null;
        }

        connection.close();
        return connections.remove(channel.id());
    }

    @Override
    public void add(Connection<T> connection) {
        connections.putIfAbsent(connection.getChannel().id(), connection);
    }

    @Override
    public int getConnNum() {
        return connections.size();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        connections.values().forEach(Connection::close);
        connections.clear();
    }
}
