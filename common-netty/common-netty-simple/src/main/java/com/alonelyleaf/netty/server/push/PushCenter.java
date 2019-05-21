package com.alonelyleaf.netty.server.push;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author bijl
 * @date 2019/5/21
 */
public class PushCenter {

    private final ConcurrentMap<ChannelId, Channel> channelMap = new ConcurrentHashMap<>();

    public void add(Channel channel){

        channelMap.putIfAbsent(channel.id(), channel);
    }

    public Channel get(ChannelId channelId){
        return channelMap.get(channelId);
    }

    public void push(ChannelId channelId, Object message){

        Channel channel = channelMap.get(channelId);
        if (null == channel){
            return;
        }
        channel.writeAndFlush(message);
    }
}
