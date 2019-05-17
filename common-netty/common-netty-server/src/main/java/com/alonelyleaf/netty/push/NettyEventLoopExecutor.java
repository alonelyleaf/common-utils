package com.alonelyleaf.netty.push;


import com.alonelyleaf.netty.api.push.PushTask;
import com.alonelyleaf.netty.api.push.PushTaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * Netty 线程池来执行任务
 *
 */
public class NettyEventLoopExecutor implements PushTaskExecutor {

    @Override
    public void shutdown() {

    }

    @Override
    public void addTask(PushTask task) {

        task.getExecutor().execute(task);
    }

    @Override
    public void delayTask(long delay, PushTask task) {

        task.getExecutor().schedule(task, delay, TimeUnit.NANOSECONDS);
    }
}
