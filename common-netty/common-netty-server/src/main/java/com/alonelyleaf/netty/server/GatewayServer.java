package com.alonelyleaf.netty.server;

import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.channelhandler.GatewayServerChannelHandler;
import com.alonelyleaf.netty.config.CommonConfig;
import com.alonelyleaf.netty.connection.ConnectionManager;
import com.alonelyleaf.netty.connection.impl.NettyConnectionManager;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.messagehandler.PushMessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 网关服务（与GatewayClient之间用ProtoBuf通信）
 *
 */
public class GatewayServer extends NettyTCPServer {

    private ConnectionManager connectionManager;

    private GatewayServerChannelHandler channelHandler;

    private PushMessageHandler pushMessageHandler;

    private Context context;


    public GatewayServer(Context context) {

        super(CommonConfig.gatewayServerPort);

        this.context = context;

        this.connectionManager = new NettyConnectionManager<Message.GatewayMsg>();

        this.pushMessageHandler = new PushMessageHandler(this.context);

        this.channelHandler = new GatewayServerChannelHandler(connectionManager, pushMessageHandler);
    }

    /**
     * GatewayClient <-> GatewayServer之间使用ProtoBuf
     *
     * @param pipeline
     */
    @Override
    protected void initPipeline(ChannelPipeline pipeline) {

        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(Message.GatewayMsg.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(getChannelHandler());
    }

    public ChannelHandler getChannelHandler() {

        return channelHandler;
    }
}
