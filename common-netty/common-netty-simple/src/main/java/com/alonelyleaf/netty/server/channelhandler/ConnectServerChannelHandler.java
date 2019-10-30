package com.alonelyleaf.netty.server.channelhandler;

import com.alonelyleaf.netty.server.push.PushCenter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

/**
 * @author bijl
 * @date 2019/5/20
 */
@ChannelHandler.Sharable
public class ConnectServerChannelHandler extends SimpleChannelInboundHandler {

    private PushCenter pushCenter;

    public ConnectServerChannelHandler(PushCenter pushCenter) {

        this.pushCenter = pushCenter;
    }

    /**
     * receive a new message
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        // verify channel from pushCenter
        Channel channel = pushCenter.get(ctx.channel().id());
        // Do something with msg
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] response = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(response);
        System.out.println("Server: " + new String(response, "UTF-8"));

        // send message
        final ByteBuf firstMsg;
        byte[] req = "nice to meet you".getBytes();
        firstMsg = Unpooled.buffer(req.length);
        firstMsg.writeBytes(req);
        channel.writeAndFlush(firstMsg);
    }

    /**
     * create a new connection
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //put the channel to pushCenter to push message in some times
        pushCenter.add(ctx.channel());
    }
}
