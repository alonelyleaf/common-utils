package com.alonelyleaf.netty.channelhandler;

import com.alonelyleaf.netty.api.protocol.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

import java.util.concurrent.ScheduledExecutorService;

@ChannelHandler.Sharable
public class TrafficCounterHandler extends GlobalTrafficShapingHandler {

    public TrafficCounterHandler(ScheduledExecutorService executor, long checkInterval) {
        super(executor, checkInterval);
    }

    @Override
    protected long calculateSize(Object msg) {

        if (msg instanceof Packet) {

            Packet packet = (Packet) msg;

            // 只统计日志流量
            if (packet.getType() == 101)
                return ((Packet) msg).getBodyLength() + 5;
        }

        return 0;

        //return super.calculateSize(msg);
    }
}
