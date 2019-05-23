package com.alonelyleaf.netty.messagehandler;

import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.config.CommonConfig;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.message.MessageHandler;
import com.alonelyleaf.util.JSONUtils;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 连接处理器
 *
 */
public class ConnectHandler implements MessageHandler<Packet> {

    private Logger logger = LoggerFactory.getLogger(ConnectHandler.class);

    private Context context;

    public ConnectHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handle(Packet packet, Connection connection) {

        String loginData = new String(packet.getData());
        Login login = JSONUtils.deserialize(loginData, Login.class);

        if (check(login)) {

            connect(packet, connection, login);

            return;
        }

        // 鉴权失败
        authFailed(packet, connection);

    }

    private void connect(Packet packet, Connection connection, Login login) {

        // 回成功消息
        returnSuccess(packet, connection);

        // 将mac设置到channel中
        AttributeKey<String> macKey = AttributeKey.valueOf("mac");
        Attribute<String> attribute = connection.getChannel().attr(macKey);
        attribute.set(login.getMac().toLowerCase());
        logger.info(">>>>>>>>>>>>>>>>>>>>>mac: {} connected", login.getMac().toLowerCase());

        // connection中设置mac
        connection.setMac(login.getMac().toLowerCase());

        // 连接注册到路由
        context.getRouterServer().register(login.getMac().toLowerCase(), connection);

        // 注册信息传到消息队列
        try {
            //context.getProducer().publish(createRegisterMsg(login.getMac()));
        } catch (Exception e) {
            logger.error("packet >>>>>>>>>>>>>>>: {} ,send to mq error!", createRegisterMsg(login.getMac()));
            logger.error("error >>>>>>>>>>>>>>>:", e);
        }
    }

    private Message.MqMsg createRegisterMsg(String mac) {

        String host = getHost();

        logger.error("host=" + host);
        return Message.MqMsg.newBuilder()
                .setType(4)
                .setMac(mac == null ? "" : mac)
                .setHost(host)
                .setPort(CommonConfig.gatewayServerPort)
                .build();
    }

    private String getHost() {

        return CommonConfig.publicIp;
    }

    /**
     * 鉴权失败
     *
     * @param packet
     * @param connection
     */
    private void authFailed(Packet packet, Connection connection) {

        packet.setResponseResult(64)
                .setReqFlag(false)
                .setData("" .getBytes());

        connection.send(packet);
    }

    private void returnSuccess(Packet packet, Connection connection) {

        connection.send(connectResult(packet, 0, null, 0));
    }

    private void returnRedirect(Packet packet, Connection connection, String host, int port) {

        connection.send(connectResult(packet, 36, host, port));
    }

    private Packet connectResult(Packet packet, int code, String host, int port) {

        Packet result = new Packet();

        result.setResponseResult(code)
                .setReqFlag(false)
                .setType(1)
                .setDataType(0)
                .setSessionId(packet.getSessionId())
                .setApiVersion(2)
                .setData(JSONUtils.serialize(new ConnectResult(host, port)).getBytes());

        return result;
    }

    private class ConnectResult {

        private String data = "Connect success.";

        private String error;

        private Server redirection;

        public ConnectResult() {
        }

        public ConnectResult(String address, int port) {

            if (address != null) {
                redirection = new Server(address, port);
            }
        }

        public String getData() {
            return data;
        }

        public ConnectResult setData(String data) {
            this.data = data;
            return this;
        }

        public String getError() {
            return error;
        }

        public ConnectResult setError(String error) {
            this.error = error;
            return this;
        }

        public Server getRedirection() {
            return redirection;
        }

        public ConnectResult setRedirection(Server redirection) {
            this.redirection = redirection;
            return this;
        }
    }

    private class Server {

        private String address;

        private int port;

        public Server() {
        }

        public Server(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public Server setAddress(String address) {
            this.address = address;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Server setPort(int port) {
            this.port = port;
            return this;
        }
    }

    private boolean check(Login login) {

        // 目前不再做鉴权，默认连接上来的都是可信任的终端
        return true;
    }

}

class Login implements Serializable {
    public Login() {
    }

    private String mac;
    private Authentication authentication;

    public Authentication getAuthentication() {
        return authentication;
    }

    public Login setAuthentication(Authentication authentication) {
        this.authentication = authentication;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public Login setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public static class Authentication implements Serializable {

        public Authentication() {
        }

        private String username;

        private String password;

        public String getUsername() {
            return username;
        }

        public Authentication setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Authentication setPassword(String password) {
            this.password = password;
            return this;
        }
    }
}