package com.alonelyleaf.netty.messagehandler;

import com.alonelyleaf.netty.api.protocol.Command;
import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.message.MessageHandler;
import com.alonelyleaf.util.JSONUtil;
import com.google.protobuf.ByteString;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseHandler implements MessageHandler<Packet> {

    private static final Logger logger = LoggerFactory.getLogger(CloseHandler.class);

    private Context context;

    public CloseHandler(Context context) {
        this.context = context;

    }

    @Override
    public void handle(Packet packet, Connection connection) {
        String mac = getMac(connection);
        try {
            logger.info("Take the initiative to close mac ={},channelId={}", mac, connection.getId());
            //context.getProducer().publish(createMsg(mac, connection.getId()));
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Message.MqMsg createMsg(String mac, String channelId) {

        OfflineMessage offlineMessage = new OfflineMessage(mac, channelId);

        String offlineMsgStr = JSONUtil.serialize(offlineMessage);

        return Message.MqMsg.newBuilder()
                .setType(Command.CLOSE.cmd)
                .setMac(mac)
                .setFinFlag(true)
                .setDataType(0)
                .setContent(ByteString.copyFrom(offlineMsgStr.getBytes()))
                .build();
    }

    private String getMac(Connection connection) {

        AttributeKey macKey = AttributeKey.valueOf("mac");
        Attribute<String> attribute = connection.getChannel().attr(macKey);
        return attribute.get();
    }

    class OfflineMessage {

        private String mac;

        private String channelId;

        public OfflineMessage(String mac, String channelId) {
            this.mac = mac;
            this.channelId = channelId;
        }

        public OfflineMessage() {
        }

        public String getMac() {
            return mac;
        }

        public OfflineMessage setMac(String mac) {
            this.mac = mac;
            return this;
        }

        public String getChannelId() {
            return channelId;
        }

        public OfflineMessage setChannelId(String channelId) {
            this.channelId = channelId;
            return this;
        }
    }

}
