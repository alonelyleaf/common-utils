package com.alonelyleaf.netty.websocket.connection;

import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class ConnectionCache {

    /**
     * 缓存连接
     */
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public void add(SocketIOClient client, String token) {


        // 新连接
        add(new Connection(client, token));
    }

    public void add(Connection connection) {

        connectionMap.putIfAbsent(connection.getId(), connection);
    }

    public Connection get(String id) {

        return connectionMap.get(id);
    }

    public List<Connection> getAll() {

        Collection<Connection> connections = connectionMap.values();

        if (isEmpty(connections)) {
            return null;
        }

        return new ArrayList<>(connections);
    }

    public void remove(String id) {

        Connection connection = connectionMap.get(id);

        if (isEmpty(connection)) {
            return;
        }

        connectionMap.remove(id);

    }

    public void removeAndDisconnect(String id) {

        removeAndDisconnect(connectionMap.get(id));
    }

    public void removeAndDisconnect(Connection connection) {

        if (isEmpty(connection)) {
            return;
        }

        connectionMap.remove(connection.getId());
        connection.disconnect();

    }

    public int size() {
        return connectionMap.size();
    }
}

