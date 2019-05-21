package com.alonelyleaf.netty.websocket.connection;

import com.corundumstudio.socketio.SocketIOClient;

public class Connection {

    private String id;
    private String token;
    private SocketIOClient client;

    public Connection(SocketIOClient client, String token) {
        this.token = token;
        this.client = client;
        this.id = client.getSessionId().toString();
    }

    public void disconnect() {
        client.disconnect();
    }

    /**
     * 同步发送文本消息
     *
     * @param data
     */
    public void send(String event, Object data) {
        client.sendEvent(event, data);
    }

    //-------------------------------------- getter setter ---------------------------------------------

    public String getId() {
        return id;
    }

    public Connection setId(String id) {
        this.id = id;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Connection setToken(String token) {
        this.token = token;
        return this;
    }

    public SocketIOClient getClient() {
        return client;
    }

    public Connection setClient(SocketIOClient client) {
        this.client = client;
        return this;
    }
}