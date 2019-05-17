package com.alonelyleaf.netty.channelhandler;


import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.connection.ConnectionManager;
import com.alonelyleaf.netty.connection.impl.NettyConnection;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.message.PacketReceiver;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public final class ConnectServerChannelHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectServerChannelHandler.class);

    private final ConnectionManager<Packet> connectionManager;
    private final PacketReceiver<Packet> receiver;
    private Context context;

    public ConnectServerChannelHandler(ConnectionManager<Packet> connectionManager, PacketReceiver<Packet> receiver, Context context) {
        this.connectionManager = connectionManager;
        this.receiver = receiver;
        this.context = context;
    }

    /**
     * 接收消息并处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {

        Connection connection = connectionManager.get(ctx.channel());
        LOGGER.debug("channelRead conn={}, packet={}", ctx.channel(), msg);
        connection.updateLastReadTime();
        receiver.onReceive(msg, connection);
    }

    /**
     * 处理异常
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        Connection connection = connectionManager.get(ctx.channel());
        LOGGER.error("caught an ex, mac={}, channel={}, conn={}", connection.getMac(), ctx.channel(), connection, cause);
        ctx.close();
    }

    /**
     * 创建新的连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Connection<Packet> connection = new NettyConnection<>();
        connection.init(ctx.channel());
        connectionManager.add(connection);
    }

    /**
     * 连接端口
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        // 删除链接并发送关闭事件
        sendCloseEvent(connectionManager.removeAndClose(ctx.channel()));

        // 删除router中链接
        //context.getRouterServer().unRegister(mac(ctx.channel()));
    }

    /**
     * 360秒没有终端的读事件发生认为已经挂掉
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        // 有超时事件发生
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event = (IdleStateEvent) evt;

            // 360秒没收到一次信息，认为终端挂掉了
            if (event.state() == IdleState.READER_IDLE) {

                LOGGER.debug("channel offline: {}", ctx.channel());
                sendCloseEvent(connectionManager.removeAndClose(ctx.channel()));
            }
        } else {

            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 发送关闭事件
     *
     * @param connection
     */
    private void sendCloseEvent(Connection connection) {

        if (connection == null) {
            return;
        }

        // 手动重置掉的不发关闭事件
        if (connection.isReset()) {
            return;
        }

        // 发离线事件并将router中链接清除
        //EventBus.post(new ConnectionCloseEvent(connection));
        //context.getRouterServer().unRegister(connection.getMac());

        LOGGER.info(">>>>>>>>>>>>>mac: {} offline", connection.getMac());
    }

}