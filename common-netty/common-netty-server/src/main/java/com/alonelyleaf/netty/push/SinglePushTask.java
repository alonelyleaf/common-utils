package com.alonelyleaf.netty.push;

import com.alonelyleaf.netty.api.protocol.Message;
import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.api.push.PushTask;
import com.alonelyleaf.netty.connection.Connection;
import com.alonelyleaf.netty.context.Context;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 一次只能给一只终端推送信息
 *
 */
public class SinglePushTask implements PushTask, ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SinglePushTask.class);

    private Context context;

    private PushMessage pushMessage;

    public SinglePushTask(Context context, PushMessage pushMessage) {

        this.context = context;
        this.pushMessage = pushMessage;
    }

    @Override
    public ScheduledExecutorService getExecutor() {

        return pushMessage.getConnection().getChannel().eventLoop();
    }

    @Override
    public void run() {

        Message.GatewayMsg gatewayMsg = pushMessage.getMsg();

        Connection<Packet> connection = context.getRouterServer().get(gatewayMsg.getMac().toLowerCase());

        if (connection == null) {

            LOGGER.error("connection for mac:{} lost!!!", gatewayMsg.getMac());
            return;
        }

        // 终端发送数据（最终是由ConnectServer将数据发出）
        connection.send(createPushMsg(gatewayMsg), this);
    }

    /**
     * 向终端发送信息后会回调这只方法
     *
     * @param future
     * @throws Exception
     */
    @Override
    public void operationComplete(ChannelFuture future) throws Exception {

        if (future.isSuccess()) {

            LOGGER.info("push message <<<<<<<<<<<<<: {} success !", pushMessage.getMsg().getContent().toStringUtf8());

        } else {

            LOGGER.error("push message <<<<<<<<<<<<<: {} fail !", pushMessage.getMsg().getContent().toStringUtf8());
        }
    }

    private Packet createPushMsg(Message.GatewayMsg gatewayMsg) {

        return new Packet()
                .setType(gatewayMsg.getType())
                .setReqFlag(true)
                .setFinFlag(gatewayMsg.getFinFlag())
                .setSessionId(nvl(gatewayMsg.getSessionId()))
                .setDataType(gatewayMsg.getDataType() == 1 ? 1 : 0)
                .setData(gatewayMsg.getContent().toByteArray());
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
