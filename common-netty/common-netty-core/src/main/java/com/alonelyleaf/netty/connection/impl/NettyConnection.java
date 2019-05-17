package com.alonelyleaf.netty.connection.impl;

import com.alonelyleaf.netty.connection.Connection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class NettyConnection<T> implements Connection<T>, ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyConnection.class);
    private Channel channel;
    private volatile byte status = STATUS_NEW;
    private long lastReadTime;
    private long lastWriteTime;

    private AtomicBoolean reset = new AtomicBoolean(false);

    // 连接对应的MAC
    private String mac;

    @Override
    public void init(Channel channel) {
        this.channel = channel;
        this.lastReadTime = System.currentTimeMillis();
        this.status = STATUS_CONNECTED;
    }

    @Override
    public String getId() {
        return channel.id().asShortText();
    }

    @Override
    public ChannelFuture send(T packet) {
        return send(packet, null);
    }

    @Override
    public ChannelFuture send(T packet, final ChannelFutureListener listener) {
        if (channel.isActive()) {

            ChannelFuture future = channel.writeAndFlush(packet).addListener(this);

            if (listener != null) {
                future.addListener(listener);
            }

            if (channel.isWritable()) {
                return future;
            }

            //阻塞调用线程还是抛异常？
            //return channel.newPromise().setFailure(new RuntimeException("send data too busy"));
            if (!future.channel().eventLoop().inEventLoop()) {
                future.awaitUninterruptibly(100);
            }
            return future;
        } else {
            /*if (listener != null) {
                channel.newPromise()
                        .addListener(listener)
                        .setFailure(new RuntimeException("connection is disconnected"));
            }*/
            return this.close();
        }
    }

    @Override
    public ChannelFuture close() {
        //if (status == STATUS_DISCONNECTED) return null;
        this.status = STATUS_DISCONNECTED;
        return this.channel.close();
    }

    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public boolean isReadTimeout() {

        return false;
    }

    @Override
    public boolean isWriteTimeout() {

        return false;
    }

    @Override
    public void updateLastReadTime() {
        lastReadTime = System.currentTimeMillis();
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            lastWriteTime = System.currentTimeMillis();
        } else {
            LOGGER.error("connection send msg error", future.cause());
        }
    }

    @Override
    public void updateLastWriteTime() {
        lastWriteTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "[channel=" + channel
                + ", status=" + status
                + ", lastReadTime=" + lastReadTime
                + ", lastWriteTime=" + lastWriteTime
                + "]";
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NettyConnection that = (NettyConnection) o;

        return channel.id().equals(that.channel.id());
    }

    @Override
    public int hashCode() {
        return channel.id().hashCode();
    }

    @Override
    public boolean isReset() {
        return reset.get();
    }

    @Override
    public void reset() {

        reset.set(true);
    }

    @Override
    public String getMac() {
        return mac;
    }

    @Override
    public Connection setMac(String mac) {

        this.mac = mac;
        return this;
    }
}
