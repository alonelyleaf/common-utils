package com.alonelyleaf.netty.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public interface Connection<T> {

    // 连接状态
    byte STATUS_NEW = 0;
    byte STATUS_CONNECTED = 1;
    byte STATUS_DISCONNECTED = 2;

    void init(Channel channel);

    ChannelFuture send(T packet);

    ChannelFuture send(T packet, ChannelFutureListener listener);

    String getId();

    ChannelFuture close();

    boolean isConnected();

    boolean isReadTimeout();

    boolean isWriteTimeout();

    void updateLastReadTime();

    void updateLastWriteTime();

    Channel getChannel();

    boolean isReset();

    void reset();

    // mac getter
    String getMac();

    // mac setter
    Connection setMac(String mac);
}
