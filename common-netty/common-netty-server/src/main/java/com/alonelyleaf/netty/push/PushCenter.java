package com.alonelyleaf.netty.push;


import com.alonelyleaf.netty.api.push.PushTask;
import com.alonelyleaf.netty.api.push.PushTaskExecutor;
import com.alonelyleaf.netty.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 真正推送给终端的地方
 *
 */
public class PushCenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushCenter.class);

    private Context context;

    private PushTaskExecutor taskExecutor;

    public PushCenter(Context context) {

        this.context = context;
        taskExecutor = new NettyEventLoopExecutor();
    }

    /**
     * 接收业务端发来的消息，将其推给终端
     *
     * @param pushMessage
     */
    public void push(PushMessage pushMessage) {

        addTask(new SinglePushTask(context, pushMessage));
    }

    private void addTask(PushTask task) {

        taskExecutor.addTask(task);
    }

}
