package com.alonelyleaf.netty.channelhandler;

import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.connection.ConnectionManager;
import com.alonelyleaf.netty.connection.impl.NettyConnection;
import com.alonelyleaf.netty.messagehandler.PushMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 只处理网关客户端与网关服务端的消息
 *
 */
@ChannelHandler.Sharable
public class GatewayServerChannelHandler extends SimpleChannelInboundHandler<Message.GatewayMsg> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayServerChannelHandler.class);

    private final ConnectionManager connectionManager;

    private final PushMessageHandler pushMessageHandler;

    public GatewayServerChannelHandler(ConnectionManager connectionManager, PushMessageHandler pushMessageHandler) {

        this.connectionManager = connectionManager;
        this.pushMessageHandler = pushMessageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.GatewayMsg msg) throws Exception {

        Connection connection = connectionManager.get(ctx.channel());
        LOGGER.debug("channelRead conn={}, packet={}", ctx.channel(), msg);
        connection.updateLastReadTime();
        pushMessageHandler.handle(msg, connection);
        //返回
        connection.send(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Connection connection = new NettyConnection();
        connection.init(ctx.channel());
        connectionManager.add(connection);
        LOGGER.info("channelActive conn={}", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        connectionManager.removeAndClose(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Connection connection = connectionManager.get(ctx.channel());
        LOGGER.error("caught an ex, channel={}, conn={}", ctx.channel(), connection, cause);
        ctx.close();
    }
}
