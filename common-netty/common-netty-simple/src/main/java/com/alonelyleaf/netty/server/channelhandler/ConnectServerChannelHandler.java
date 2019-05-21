package com.alonelyleaf.netty.server.channelhandler;

import com.alonelyleaf.netty.server.push.PushCenter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author bijl
 * @date 2019/5/20
 */
@ChannelHandler.Sharable
public class ConnectServerChannelHandler extends ChannelInboundHandlerAdapter {

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // verify channel from pushCenter
        Channel channel = pushCenter.get(ctx.channel().id());
        // Do something with msg
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
