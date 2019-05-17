package com.alonelyleaf.netty.messagehandler;

import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.message.MessageHandler;
import com.google.protobuf.ByteString;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageHandler implements MessageHandler<Packet> {

    private Context context;

    private final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);

    public DefaultMessageHandler(Context context) {

        this.context = context;
    }

    @Override
    public void handle(Packet packet, Connection connection) {
       // 取出channel中的mac值
        String mac = getMac(connection);

        logger.info("received >>>>>>>>>>>>>>>:" + packet.toString());
        // 只打印json数据
        logPacketData(packet);



        // 数据交给消息队列
        try {
            //context.getProducer().publish(createMsg(packet, mac, connection.getId()));
        } catch (Exception e) {
            logger.error("packet >>>>>>>>>>>>>>>: {} ,send to mq error!", packet);
            logger.error("error >>>>>>>>>>>>>>>:", e);
        }
    }




    private Message.MqMsg createMsg(Packet packet, String mac, String channelId) {

        return Message.MqMsg.newBuilder()
                .setVersion(packet.getApiVersion() + "")
                .setType(packet.getType())
                .setSessionId(nvl(packet.getSessionId()))
                .setFinFlag(packet.isFinFlag())
                .setMac(nvl(mac))
                .setChannelId(channelId)
                .setDataType(packet.getDataType())
                .setResponseResult(packet.getResponseResult())
                .setFileName(nvl(packet.getFileName()))
                .setFileType(nvl(packet.getFileType()))
                .setContent(ByteString.copyFrom(packet.getData()))
                .build();
    }

    private String getMac(Connection connection) {

        AttributeKey macKey = AttributeKey.valueOf("mac");
        Attribute<String> attribute = connection.getChannel().attr(macKey);
        return attribute.get();
    }

    private void logPacketData(Packet packet) {

        // 只打印json数据
        if (packet.getDataType() == 0) {

            logger.info(">>>>>>>>>>>>>>>:" + new String(packet.getData()));
        }
    }

    /**
     * null值返回空字符串
     *
     * @param str
     * @return
     */
    private String nvl(String str) {

        if (str == null) {
            return "";
        }

        return str;
    }
}
