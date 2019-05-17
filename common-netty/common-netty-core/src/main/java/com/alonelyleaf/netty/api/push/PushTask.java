package com.alonelyleaf.netty.api.push;

import java.util.concurrent.ScheduledExecutorService;

public interface PushTask extends Runnable {

    ScheduledExecutorService getExecutor();
}
