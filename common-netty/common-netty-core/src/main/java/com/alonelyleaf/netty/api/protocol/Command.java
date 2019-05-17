package com.alonelyleaf.netty.api.protocol;

public enum Command {

    TEST(1),
    UNKNOWN(-1),

    // 连接
    CONNECT(1),
    HEARTBEAT(2),
    CLOSE(3),
    GATEWAY_PUSH(4),
    DEFAULT(999);

    Command(int cmd) {
        this.cmd = cmd;
    }

    public final int cmd;
    
}
