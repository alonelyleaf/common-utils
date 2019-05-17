package com.alonelyleaf.netty.messagehandler;

import com.alonelyleaf.netty.api.protocol.Command;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.config.CommonConfig;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.message.MessageHandler;
import com.alonelyleaf.netty.message.PacketReceiver;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public final class MessageDispatcher implements PacketReceiver<Packet> {
    public static final int POLICY_REJECT = 2;
    public static final int POLICY_LOG = 1;
    public static final int POLICY_IGNORE = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);
    private final Map<Integer, MessageHandler> handlers = new HashMap<>();
    private final int unsupportedPolicy;

    private TrafficCounter trafficCounter;

    public MessageDispatcher(TrafficCounter trafficCounter) {
        unsupportedPolicy = POLICY_REJECT;
        this.trafficCounter = trafficCounter;
    }

    public MessageDispatcher(int unsupportedPolicy) {
        this.unsupportedPolicy = unsupportedPolicy;
    }

    public void register(Command command, MessageHandler handler) {
        handlers.put(command.cmd, handler);
    }

    public void register(Command command, Supplier<MessageHandler> handlerSupplier, boolean enabled) {
        if (enabled && !handlers.containsKey(command.cmd)) {
            register(command, handlerSupplier.get());
        }
    }

    public void register(Command command, Supplier<MessageHandler> handlerSupplier) {
        this.register(command, handlerSupplier, true);
    }

    public MessageHandler unRegister(Command command) {
        return handlers.remove(command.cmd);
    }

    @Override
    public void onReceive(Packet packet, Connection connection) {

        // 类型为日志
        if (packet.getType() == 101) {

            // 日志流量超过限速时丢弃日志数据
            if (trafficCounter.lastReadBytes() > CommonConfig.trafficLogReadLimit) {
                LOGGER.warn("[Traffic] last 1s read bytes: {}, over read limit {}, log data will be discarded !", trafficCounter.lastReadBytes(), CommonConfig.trafficLogReadLimit);
                return;
            }
        }

        MessageHandler handler = getHandler(packet.getType());

        try {

            handler.handle(packet, connection);

        } catch (Throwable throwable) {

            LOGGER.error("hand message error :", throwable);
        }
    }

    private MessageHandler getHandler(Integer key) {

        MessageHandler result = handlers.get(key);

        if (result != null) {
            return result;
        }

        return handlers.get(Command.DEFAULT.cmd);
    }
}
