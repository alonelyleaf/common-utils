package com.alonelyleaf.netty;

import com.alonelyleaf.netty.boot.ServerLauncher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 启动类
 *
 */
@DependsOn({"commonConfig", "connectServerConfig"})
@Component
public class Launcher {

    @PostConstruct
    public void init() throws Exception {

        ServerLauncher serverLauncher = new ServerLauncher();
        serverLauncher.init();
        serverLauncher.start();
    }
}
