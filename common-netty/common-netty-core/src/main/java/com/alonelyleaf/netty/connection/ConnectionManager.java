package com.alonelyleaf.netty.connection;

import io.netty.channel.Channel;

public interface ConnectionManager<T> {

    Connection<T> get(Channel channel);

    Connection<T> removeAndClose(Channel channel);

    void add(Connection<T> connection);

    int getConnNum();

    void init();

    void destroy();
}
