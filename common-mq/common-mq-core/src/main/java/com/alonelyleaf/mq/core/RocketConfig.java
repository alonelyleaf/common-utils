package com.alonelyleaf.mq.core;

import jodd.util.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RocketConfig {

    private List<MQServer> servers;

    private Producer producer;

    private Consumer consumer;

    public String getServers() {

        if (servers == null || servers.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        servers.forEach(mqServer -> result.append(mqServer.getHost()).append(StringPool.COLON).append(mqServer.getPort()).append(StringPool.SEMICOLON));
        return result.toString();
    }

    /**
     * 格式：
     * ip1:port1,ip2:port2
     *
     * @param servers
     * @return
     */
    public RocketConfig setServers(String servers) {

        if (servers == null || servers.isEmpty()) {
            return this;
        }

        List<MQServer> result = new ArrayList<>();
        String[] serverArray = servers.split(StringPool.COMMA);
        for (String server : serverArray) {
            String[] hostPortPair = server.split(StringPool.COLON);

            if (hostPortPair.length == 2) {
                RocketConfig.MQServer mqServer = new RocketConfig.MQServer();
                mqServer.setHost(hostPortPair[0]);
                mqServer.setPort(Integer.parseInt(hostPortPair[1]));
                result.add(mqServer);
            }
        }

        this.servers = result;
        return this;
    }

    public RocketConfig setServers(List<MQServer> servers) {
        this.servers = servers;
        return this;
    }

    public Producer getProducer() {
        return producer;
    }

    public RocketConfig setProducer(Producer producer) {
        this.producer = producer;
        return this;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public RocketConfig setConsumer(Consumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public static class MQServer {

        private String host;

        private int port;

        public String getHost() {
            return host;
        }

        public MQServer setHost(String host) {
            this.host = host;
            return this;
        }

        public int getPort() {
            return port;
        }

        public MQServer setPort(int port) {
            this.port = port;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MQServer mqServer = (MQServer) o;
            return port == mqServer.port && Objects.equals(host, mqServer.host);
        }

        @Override
        public int hashCode() {
            return Objects.hash(host, port);
        }

        @Override
        public String toString() {
            return "MQServer{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }


    public static class Producer {

        private String group;

        private int sendMsgTimeout = 3000;

        private int compressMsgBodyOverHowmuch = 1024 * 4;

        private int retryTimesWhenSendFailed = 2;

        private int retryTimesWhenSendAsyncFailed = 2;

        private boolean retryAnotherBrokerWhenNotStoreOk = false;

        private int maxMessageSize = 1024 * 1024 * 4; // 4M

        public String getGroup() {
            return group;
        }

        public Producer setGroup(String group) {
            this.group = group;
            return this;
        }

        public int getSendMsgTimeout() {
            return sendMsgTimeout;
        }

        public Producer setSendMsgTimeout(int sendMsgTimeout) {
            this.sendMsgTimeout = sendMsgTimeout;
            return this;
        }

        public int getCompressMsgBodyOverHowmuch() {
            return compressMsgBodyOverHowmuch;
        }

        public Producer setCompressMsgBodyOverHowmuch(int compressMsgBodyOverHowmuch) {
            this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
            return this;
        }

        public int getRetryTimesWhenSendFailed() {
            return retryTimesWhenSendFailed;
        }

        public Producer setRetryTimesWhenSendFailed(int retryTimesWhenSendFailed) {
            this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
            return this;
        }

        public int getRetryTimesWhenSendAsyncFailed() {
            return retryTimesWhenSendAsyncFailed;
        }

        public Producer setRetryTimesWhenSendAsyncFailed(int retryTimesWhenSendAsyncFailed) {
            this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
            return this;
        }

        public boolean getRetryAnotherBrokerWhenNotStoreOk() {
            return retryAnotherBrokerWhenNotStoreOk;
        }

        public Producer setRetryAnotherBrokerWhenNotStoreOk(boolean retryAnotherBrokerWhenNotStoreOk) {
            this.retryAnotherBrokerWhenNotStoreOk = retryAnotherBrokerWhenNotStoreOk;
            return this;
        }

        public int getMaxMessageSize() {
            return maxMessageSize;
        }

        public Producer setMaxMessageSize(int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
            return this;
        }

    }

    public static class Consumer {

        private String group;

        private String topic;

        private String tags = StringPool.ASTERISK;

        private int consumeThreadMin = 20;

        private int consumeThreadMax = 64;

        public String getGroup() {
            return group;
        }

        public Consumer setGroup(String group) {
            this.group = group;
            return this;
        }

        public String getTopic() {
            return topic;
        }

        public Consumer setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public String getTags() {
            return tags;
        }

        public Consumer setTags(String tags) {
            this.tags = tags;
            return this;
        }

        public int getConsumeThreadMin() {
            return consumeThreadMin;
        }

        public Consumer setConsumeThreadMin(int consumeThreadMin) {
            this.consumeThreadMin = consumeThreadMin;
            return this;
        }

        public int getConsumeThreadMax() {
            return consumeThreadMax;
        }

        public Consumer setConsumeThreadMax(int consumeThreadMax) {
            this.consumeThreadMax = consumeThreadMax;
            return this;
        }
    }

}
