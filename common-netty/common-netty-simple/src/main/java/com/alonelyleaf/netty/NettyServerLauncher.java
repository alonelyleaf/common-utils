package com.alonelyleaf.netty;

import com.alonelyleaf.netty.server.channelhandler.ConnectServerChannelHandler;
import com.alonelyleaf.netty.server.nettyserver.ConnectServer;
import com.alonelyleaf.netty.server.push.PushCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author bijl
 * @date 2019/5/20
 */

@Component
public class NettyServerLauncher {

    @Value("${netty.server.port:9000}")
    private Integer port;

    private PushCenter pushCenter;

    @PostConstruct
    public void init() throws Exception {

        pushCenter = new PushCenter();

        ConnectServerChannelHandler channelHandler = new ConnectServerChannelHandler(pushCenter);

        new ConnectServer(port, channelHandler).run();
    }

    /**
     * get pushCenter to push message
     *
     * @return pushCenter
     */
    public PushCenter pushCenter(){

        return pushCenter;
    }
}
