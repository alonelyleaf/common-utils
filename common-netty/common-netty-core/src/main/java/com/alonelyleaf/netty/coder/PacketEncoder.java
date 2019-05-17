package com.alonelyleaf.netty.coder;


import com.alonelyleaf.netty.api.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<Packet> {

    public static final PacketEncoder INSTANCE = new PacketEncoder();

    private static final int VERSION_AND_HEADER_LENGTH = 0x14;
    private static final int API_VERSION = 0x02;
    private static final int DATA_HEADER_LENGTH = 4;
    // tvl中type+length总长度
    private static final int TLV_TYPE_LENGTH_LENGTH = 2;

    private static final int TLV_TYPE = 0x01;

    // 作为请求时响应结果填0
    private static final int RESPONSE_RESULT_FOR_REQUEST = 0x00;


    /**
     * 结束的请求
     */
    private static final int REQUEST_FINISH = 0xC0;
    /**
     * 未结束请求
     */
    private static final int REQUEST_UNFINISH = 0x80;
    /**
     * 结束的响应
     */
    private static final int RESPONSE_FINISH = 0x40;
    /**
     * 未结束响应
     */
    private static final int RESPONSE_UNFINISH = 0x00;


    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {

        // 写版本和头长度，固定值
        out.writeByte(VERSION_AND_HEADER_LENGTH);

        out.writeInt(bodyLength(msg));

        // api版本，固定值
        out.writeByte(API_VERSION);

        out.writeByte(dataHeaderLength(msg));

        out.writeByte(reqFlagAndFinFlag(msg));

        // 请求类型待定
        out.writeByte(msg.getType());

        // 响应结果
        out.writeByte(responseResult(msg));

        // 数据编码格式
        out.writeByte(dataType(msg));

        // sessionId
        if (msg.getSessionId() != null && msg.getSessionId().length() > 0) {
            out.writeByte(TLV_TYPE);
            int length = msg.getSessionId().length();
            out.writeByte(length);
            out.writeBytes(msg.getSessionId().getBytes());
        }

        // 业务数据
        out.writeBytes(msg.getData());

        msg.setData(null);
    }

    /**
     * body长度
     *
     * @param msg
     * @return
     */
    private int bodyLength(Packet msg) {

        // 加的两个字节是 api版本和数据头长度两个的长度
        return dataHeaderLength(msg) + msg.getData().length + 2;
    }

    private int dataType(Packet msg) {

        if (msg.getDataType() == 0) {
            return 0;
        }

        return 0x10;
    }

    private int dataHeaderLength(Packet msg) {

        if (msg.getSessionId() == null || msg.getSessionId().length() == 0) {
            return DATA_HEADER_LENGTH;
        }

        return DATA_HEADER_LENGTH + msg.getSessionId().length() + TLV_TYPE_LENGTH_LENGTH;
    }

    private int reqFlagAndFinFlag(Packet msg) {

        // 请求-结束
        if (isFinishedRequest(msg)) {
            return REQUEST_FINISH;
        }

        // 请求-未结束
        if (isUnFinishedRequest(msg)) {
            return REQUEST_UNFINISH;
        }

        // 响应-结束
        if (isFinishedResponse(msg)) {
            return RESPONSE_FINISH;
        }

        return RESPONSE_UNFINISH;
    }

    private int responseResult(Packet msg) {

        if (msg.isReqFlag()) {
            return RESPONSE_RESULT_FOR_REQUEST;
        }

        return msg.getResponseResult();
    }


    private boolean isFinishedResponse(Packet msg) {

        return !msg.isReqFlag() && msg.isFinFlag();
    }

    private boolean isUnFinishedRequest(Packet msg) {

        return msg.isReqFlag() && !msg.isFinFlag();
    }

    private boolean isFinishedRequest(Packet msg) {

        return msg.isReqFlag() && msg.isFinFlag();
    }

}
