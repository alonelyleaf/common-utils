package com.alonelyleaf.netty.messagehandler;


import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.context.Context;
import com.alonelyleaf.netty.message.MessageHandler;
import com.alonelyleaf.netty.push.PushMessage;

/**
 * 推送处理器
 *
 */
public class PushMessageHandler implements MessageHandler<Message.GatewayMsg> {

    private final Context context;

    public PushMessageHandler(Context context) {
        this.context = context;
    }

    /**
     * @param gatewayMsg GatewayClient发来的消息
     * @param connection 与GatewayClient的连接
     */
    public void handle(Message.GatewayMsg gatewayMsg, Connection connection) {

        // TODO 添加反馈，通过connection返回给GatewayClient消息
        //pushCenter将消息发送给设备，设备的反馈通过PushMessage中传入的connection再发送给gatewayClient
        context.getPushCenter().push(new PushMessage(connection, gatewayMsg));
    }
}
