package com.alonelyleaf.netty.router;


import com.alonelyleaf.netty.api.protocol.Packet;
import com.alonelyleaf.netty.connection.Connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用以存放连接，以便后续推数据
 *
 */
public class RouterServer {

    private final ConcurrentMap<String, Connection<Packet>> connections = new ConcurrentHashMap<>();

    public void register(String mac, Connection<Packet> connection) {

        Connection origConnection = connections.get(mac);

        if (origConnection != null) {
            // 设置重置标志
            origConnection.reset();
            // 关闭原连接
            origConnection.close();
        }

        connections.put(mac, connection);
    }

    public Connection<Packet> get(String mac) {

        return connections.get(mac);
    }

    public void unRegister(String mac) {

        if (mac == null || mac.length() == 0) {
            return;
        }
        //有可能出现同步问题，新连接刚覆盖此mac，旧的线程删除此mac才执行，导致连接在，但是路由中没有
        //connections.remove(mac);
    }
}
