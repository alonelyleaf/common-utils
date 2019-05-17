package com.alonelyleaf.netty.api.protocol;

/**
 * 自定义协议包定义
 *
 */
public class Packet {

    public byte cmd = 1; //命令

    // ============================== 头部 ==============================
    // 版本
    private int version;

    // 头长度
    private int headerLength;

    // body长度
    private int bodyLength;

    // ============================== 数据 ==============================

    // api版本
    private int apiVersion;

    // 数据头长度
    private int dataHeaderLength;

    // 请求标识
    private boolean reqFlag;

    // 结束标识
    private boolean finFlag;

    // 请求类型或响应结果
    private int type;

    // 响应结果
    private int responseResult;

    // 数据类型
    private int dataType;

    // 数据编码格式
    private int charset;

    // 会话id
    private String sessionId;

    // 数据内容
    private byte[] data;

    // 文件名称(api版本0x2引入)
    private String fileName;

    // 文件类型(api版本0x2引入)
    private String fileType;

    // ---------------------------- getter setter -------------------------------

    public byte getCmd() {
        return cmd;
    }

    public Packet setCmd(byte cmd) {
        this.cmd = cmd;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public Packet setVersion(int version) {
        this.version = version;
        return this;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public Packet setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
        return this;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public Packet setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
        return this;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public Packet setApiVersion(int apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public int getDataHeaderLength() {
        return dataHeaderLength;
    }

    public Packet setDataHeaderLength(int dataHeaderLength) {
        this.dataHeaderLength = dataHeaderLength;
        return this;
    }

    public boolean isReqFlag() {
        return reqFlag;
    }

    public Packet setReqFlag(boolean reqFlag) {
        this.reqFlag = reqFlag;
        return this;
    }

    public boolean isFinFlag() {
        return finFlag;
    }

    public Packet setFinFlag(boolean finFlag) {
        this.finFlag = finFlag;
        return this;
    }

    public int getType() {
        return type;
    }

    public Packet setType(int type) {
        this.type = type;
        return this;
    }

    public int getResponseResult() {
        return responseResult;
    }

    public Packet setResponseResult(int responseResult) {
        this.responseResult = responseResult;
        return this;
    }

    public int getDataType() {
        return dataType;
    }

    public Packet setDataType(int dataType) {
        this.dataType = dataType;
        return this;
    }

    public int getCharset() {
        return charset;
    }

    public Packet setCharset(int charset) {
        this.charset = charset;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Packet setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public Packet setData(byte[] data) {
        this.data = data;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Packet setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public Packet setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    @Override
    public String toString() {

        return "{" +
                "version=" + version + "," +
                "headerLength=" + headerLength + "," +
                "bodyLength=" + bodyLength + "," +
                "apiVersion=" + apiVersion + "," +
                "dataHeaderLength=" + dataHeaderLength + "," +
                "reqFlag=" + reqFlag + "," +
                "finFlag=" + finFlag + "," +
                "type=" + type + "," +
                "responseResult=" + responseResult + "," +
                "dataType=" + dataType + "," +
                "charset=" + charset + "," +
                "sessionId=" + sessionId + "," +
                "fileName=" + fileName + "," +
                "fileType=" + fileType + "," +
                "body=" + (data == null ? 0 : data.length) +
                "}";
    }
}
