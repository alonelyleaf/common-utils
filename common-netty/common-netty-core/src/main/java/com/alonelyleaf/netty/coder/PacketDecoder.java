package com.alonelyleaf.netty.coder;

import com.alonelyleaf.netty.api.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义协议包解析
 *
 */
public class PacketDecoder extends ByteToMessageDecoder {

    // 数据头部固定部分长度
    private static final int DATA_HEADER_LENGTH = 4;

    private static final int FIRST_HALF_OF_BYTE = 0xF0;
    private static final int BOTTOM_HALF_OF_BYTE = 0x0F;
    private static final int FIRST_BIT_OF_BYTE = 0x80;
    private static final int SECOND_BIT_OF_BYTE = 0x40;

    // 跳过第一位
    private static final int SKIP_FIRST_BIT_OF_BYTE = 0x7F;


    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        Packet packet = new Packet();

        // 版本号、头长度
        parseHeader(in, packet);

        // body长度
        parseBodyLength(in, packet);

        // api版本、数据头部长度
        parseDataHeader(in, packet);

        // 请求标识、结束标识
        parseReqFlag(in, packet);

        // 请求类型
        parseType(in, packet);

        // 响应结果
        parseResponseResult(in, packet);

        // 数据类型、编码格式
        parseCharset(in, packet);

        // 数据头部扩展，包括 1:会话ID,2:文件名,3:文件类型
        parseDataHeaderExtension(in, packet);

        // 业务数据
        parseData(in, packet);

        out.add(packet);
    }

    /**
     * 解析头部
     *
     * @param in
     * @param packet
     */
    private void parseHeader(ByteBuf in, Packet packet) {

        // 版本号、头长度
        byte versionAndLength = in.readByte();
        packet.setVersion((versionAndLength & FIRST_HALF_OF_BYTE) >> 4);
        packet.setHeaderLength(versionAndLength & BOTTOM_HALF_OF_BYTE);
    }

    /**
     * 数据长度
     *
     * @param in
     * @param packet
     */
    private void parseBodyLength(ByteBuf in, Packet packet) {

        // body长度
        packet.setBodyLength(in.readInt());
    }

    /**
     * 数据头长度
     *
     * @param in
     * @param packet
     */
    private void parseDataHeader(ByteBuf in, Packet packet) {

        // api版本、数据头部长度
        packet.setApiVersion(in.readByte());
        //packet.setDataHeaderLength(in.readByte());
        parseDataHeaderLength(in, packet);
    }

    /**
     * 解析数据头长度
     *
     * @param in
     * @param packet
     */
    private void parseDataHeaderLength(ByteBuf in, Packet packet) {

        byte lengthFlag = in.readByte();

        // 第1位为0，长度为低7位值
        int firstBitFlag = lengthFlag >> 7;
        int length = lengthFlag & SKIP_FIRST_BIT_OF_BYTE;

        if (firstBitFlag == 0) {

            packet.setDataHeaderLength(length);
            return;
        }

        // 第1位为1，长度为低7位乘以4
        packet.setDataHeaderLength(length * 4);
    }

    /**
     * +------------------------+
     * | 0      | 1       | 2-7 |
     * | 请求标识 | 结束标识 | 预留 |
     * +------------------------+
     * <p>
     * 解析请求标识和结束标识
     *
     * @param in
     * @param packet
     */
    private void parseReqFlag(ByteBuf in, Packet packet) {

        // 请求标识、结束标识
        byte flag = in.readByte();
        packet.setReqFlag((flag & FIRST_BIT_OF_BYTE) != 0);
        packet.setFinFlag((flag & SECOND_BIT_OF_BYTE) != 0);
    }

    /**
     * 请求类型
     *
     * @param in
     * @param packet
     */
    private void parseType(ByteBuf in, Packet packet) {

        // 请求类型/响应结果
        packet.setType(in.readByte());
    }

    /**
     * 响应结果
     *
     * @param in
     * @param packet
     */
    private void parseResponseResult(ByteBuf in, Packet packet) {

        // 响应结果
        packet.setResponseResult(in.readByte());
    }

    /**
     * 数据类型和编码格式
     * <p>
     * +-------------------+
     * | 0-3    | 4-7      |
     * | 数据类型 | 编码格式  |
     * +-------------------+
     *
     * @param in
     * @param packet
     */
    private void parseCharset(ByteBuf in, Packet packet) {

        // 数据类型、编码格式
        byte typeAndCharset = in.readByte();
        packet.setDataType((typeAndCharset & FIRST_HALF_OF_BYTE) >> 4);
        packet.setCharset(typeAndCharset & BOTTOM_HALF_OF_BYTE);
    }

    /**
     * 数据头扩展，TLV编码
     * T：类型，一个字节
     * L：长度，一个字节
     * V：数据
     *
     * @param in
     * @param packet
     */
    private void parseDataHeaderExtension(ByteBuf in, Packet packet) {

        // 扩展长度
        int extLength = packet.getDataHeaderLength() - DATA_HEADER_LENGTH;

        int type;
        int length;

        while (extLength > 0) {

            // tlv-type
            type = in.readByte();

            // tlv-length
            length = in.readByte();

            // tlv-value
            byte[] value = new byte[length];
            in.readBytes(value);
            setDataHeaderExtension(type, new String(value), packet);

            // type占1个字节，length占1个字节，value占length个字节
            extLength = extLength - 1 - 1 - length;
        }
    }

    /**
     * 程序数据
     *
     * @param in
     * @param packet
     */
    private void parseData(ByteBuf in, Packet packet) {

        // 业务数据
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        packet.setData(data);
    }

    /**
     * 数据头部扩展数据：
     * 0x01: sessionId
     * 0x02: FileName
     * 0x03: FileType
     *
     * @param type
     * @param value
     * @param packet
     */
    private void setDataHeaderExtension(int type, String value, Packet packet) {

        switch (type) {
            case 1:
                packet.setSessionId(value);
                break;
            case 2:
                packet.setFileName(value);
                break;
            case 3:
                packet.setFileType(value);
                break;
        }
    }
}
