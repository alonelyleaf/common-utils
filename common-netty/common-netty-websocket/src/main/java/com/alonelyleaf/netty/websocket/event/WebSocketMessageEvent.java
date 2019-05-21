package com.alonelyleaf.netty.websocket.event;

/**
 * @author bijl
 * @date 2019/5/21
 */
public class WebSocketMessageEvent {

    private String sessionId;

    private String host;

    private String connectionId;

    private String content;

    private String uuid;

    private Long modifyTime;

    private String type;

    public String getSessionId() {
        return sessionId;
    }

    public WebSocketMessageEvent setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getHost() {
        return host;
    }

    public WebSocketMessageEvent setHost(String host) {
        this.host = host;
        return this;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public WebSocketMessageEvent setConnectionId(String connectionId) {
        this.connectionId = connectionId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public WebSocketMessageEvent setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public WebSocketMessageEvent setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public WebSocketMessageEvent setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public String getType() {
        return type;
    }

    public WebSocketMessageEvent setType(String type) {
        this.type = type;
        return this;
    }
}
