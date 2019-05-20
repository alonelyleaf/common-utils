package com.alonelyleaf.netty.server;

import com.alonelyleaf.netty.api.listener.Listener;
import com.alonelyleaf.netty.api.protocol.Command;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.channelhandler.ConnectServerChannelHandler;
import com.alonelyleaf.netty.channelhandler.TrafficCounterHandler;
import com.alonelyleaf.netty.config.ConnectServerConfig;
import com.alonelyleaf.netty.connection.ConnectionManager;
import com.alonelyleaf.netty.connection.impl.NettyConnectionManager;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.messagehandler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectServer extends NettyTCPServer {

    private Logger logger = LoggerFactory.getLogger(ConnectServer.class);

    private ConnectServerChannelHandler serverChannelHandler;

    private ConnectionManager<Packet> connectionManager;

    private MessageDispatcher messageDispatcher;

    private SslContext sslContext;

    private Context context;

    private GlobalTrafficShapingHandler trafficShapingHandler;
    private ScheduledExecutorService trafficShapingExecutor;


    public ConnectServer(Context context) {

        super(ConnectServerConfig.port);

        this.context = context;

        this.connectionManager = new NettyConnectionManager<>();

        trafficShapingExecutor = Executors.newSingleThreadScheduledExecutor();
        trafficShapingHandler = new TrafficCounterHandler(trafficShapingExecutor, 1000);

        this.messageDispatcher = new MessageDispatcher(trafficShapingHandler.trafficCounter());

        this.serverChannelHandler = new ConnectServerChannelHandler(this.connectionManager, this.messageDispatcher, this.context);

        sslContext = sslContext();

        //EventBus.create(DefaultExecutorFactory.getEventBusExecutor());
    }

    @Override
    public void init() {
        super.init();

        messageDispatcher.register(Command.CONNECT, () -> new ConnectHandler(context));
        messageDispatcher.register(Command.DEFAULT, () -> new DefaultMessageHandler(context));
        messageDispatcher.register(Command.CLOSE, () -> new CloseHandler(context));
        messageDispatcher.register(Command.HEARTBEAT, () -> new HeartbeatHandler());


        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> logger.info("[connection size]: {}", connectionManager.getConnNum()), 10, 30, TimeUnit.SECONDS);
    }

    @Override
    protected void initPipeline(ChannelPipeline pipeline) {

        SSLEngine sslEngine = sslContext.newEngine(pipeline.channel().alloc());
        sslEngine.setUseClientMode(false);
        sslEngine.setNeedClientAuth(true);
        sslEngine.setEnabledCipherSuites(new String[]{"TLS_RSA_WITH_AES_128_CBC_SHA"});

        // 设置ssl握手超时时间
        SslHandler sslHandler = new SslHandler(sslEngine);
        sslHandler.setHandshakeTimeout(120, TimeUnit.SECONDS);

        pipeline.addLast("ssl", sslHandler);

        //pipeline.addLast(new LoggingHandler(LogLevel.ERROR));

        // 拆包
        pipeline.addLast(new LengthFieldBasedFrameDecoder(128 * 1024 * 10, 1, 4, 0, 0));

        pipeline.addLast("decoder", getDecoder());

        if (trafficShapingHandler != null) {
            pipeline.addLast(trafficShapingHandler);
        }

        pipeline.addLast("encoder", getEncoder());

        // 心跳
        pipeline.addLast("heartbeat", new IdleStateHandler(300, 0, 0));

        pipeline.addLast("handler", getChannelHandler());

    }

    @Override
    public ChannelHandler getChannelHandler() {
        return serverChannelHandler;
    }

    @Override
    public void stop(Listener listener) {
        super.stop(listener);

        connectionManager.destroy();
    }

    @Override
    public void start(Listener listener) {
        super.start(listener);
    }

    private SslContext sslContext() {

        SslContext sslContext = null;
        try {

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(ConnectServer.class.getClassLoader().getResourceAsStream("temp.jks"), "yealink1105".toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "yealink1105".toCharArray());

            // truststore
            KeyStore ts = KeyStore.getInstance("JKS");
            ts.load(ConnectServer.class.getClassLoader().getResourceAsStream("temp.jks"), "yealink1105".toCharArray());

            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);

            sslContext = SslContextBuilder.forServer(kmf).trustManager(tmf).build();

        } catch (Exception e) {

            logger.error("load jks error!", e);
        }

        return sslContext;
    }

    @Override
    protected void initOptions(ServerBootstrap b) {
        super.initOptions(b);

        b.option(ChannelOption.SO_BACKLOG, ConnectServerConfig.soBacklog);

        b.childOption(ChannelOption.SO_RCVBUF, ConnectServerConfig.rcvBuf);
        b.childOption(ChannelOption.SO_SNDBUF, ConnectServerConfig.sndBuf);
    }

    @Override
    protected int getBossThreadNum() {

        return ConnectServerConfig.bossNumber;
    }
}
