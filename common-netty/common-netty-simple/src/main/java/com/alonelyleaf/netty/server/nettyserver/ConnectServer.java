package com.alonelyleaf.netty.server.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * https://netty.io/wiki/user-guide-for-4.x.html
 *
 * @author bijl
 * @date 2019/5/20
 */
public class ConnectServer {

    private int port;

    private ChannelHandler channelHandler;

    public ConnectServer(int port, ChannelHandler channelHandler) {
        this.port = port;
        this.channelHandler = channelHandler;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)

            b.group(bossGroup, workerGroup);

            b.channel(NioServerSocketChannel.class); // (3)

            b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            initPipeline(ch.pipeline());
                        }
                    });

            b.option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /**
     * this is called when a connection is created
     *
     * @param pipeline
     */
    protected void initPipeline(ChannelPipeline pipeline) {

        //channel handler
        pipeline.addLast("handler", channelHandler);
    }
}
