package com.alonelyleaf.netty.websocket.service;

import com.alonelyleaf.netty.websocket.connection.Connection;
import com.alonelyleaf.netty.websocket.connection.ConnectionCache;
import com.alonelyleaf.netty.websocket.constant.Constant;
import com.corundumstudio.socketio.SocketIOServer;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author huangxb
 * @date 2018/9/20.
 */
@Service
public class WebSocketService extends WebSocketBaseService {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private ConnectionCache cache;

    @Value("${websocket.check.auth.delay:5}")
    private int delay;

    @PostConstruct
    public void init() {
        timedCheckAuth();
    }

    public void broadcast(String name, Object data) {

        server.getRoomOperations(Constant.SocketIOClientRoom.room).sendEvent(name, data);
    }

    public void send(String name, String id, Object data) {

        Connection connection = cache.get(id);

        if (isEmpty(connection)) {
            return;
        }

        connection.send(name, data);
    }

    /**
     * 定时清除token过期的Connection
     */
    private void timedCheckAuth() {

        Timer timer = new HashedWheelTimer();
        timer.newTimeout(new CheckAuth(timer), delay, TimeUnit.MINUTES);
    }


    private class CheckAuth implements TimerTask {

        private Timer timer;

        public CheckAuth(Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run(Timeout timeout) throws Exception {

            //检查token是否过期
            checkAuth();

            //重启任务
            restart();
        }

        private void restart() {
            timer.newTimeout(this, delay, TimeUnit.MINUTES);
        }

        private void checkAuth() {

            if (cache.size() > 0) {
                List<Connection> all = cache.getAll();
                for (Connection connection : all) {
                    if (!isAuth(connection.getToken())) {
                        tokenInvalid(connection);
                    }
                }
                all = null;
            }

        }

        /**
         * token失效时执行
         * @param connection
         */
        private void tokenInvalid(Connection connection) {

            connection.send(Constant.EvenType.TOKEN_INVALID, null);
            cache.removeAndDisconnect(connection);

        }
    }
}
