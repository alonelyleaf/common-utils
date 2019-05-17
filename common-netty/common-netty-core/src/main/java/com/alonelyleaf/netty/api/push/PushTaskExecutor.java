package com.alonelyleaf.netty.api.push;

public interface PushTaskExecutor {

    void shutdown();

    void addTask(PushTask task);

    void delayTask(long delay, PushTask task);
}
