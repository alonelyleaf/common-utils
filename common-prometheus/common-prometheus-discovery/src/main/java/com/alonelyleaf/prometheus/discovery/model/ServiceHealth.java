package com.alonelyleaf.prometheus.discovery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 服务健康检查
 *
 * model details see https://www.consul.io/api/health.html#list-nodes-for-service
 */
@Getter
@Builder
public class ServiceHealth {

    @JsonProperty("Node")
    private Node node;

    @JsonProperty("Service")
    private Service service;

    @JsonProperty("Checks")
    private List<Check> checks;

    @Getter
    @Builder
    public static class Node {

        @JsonProperty("Node")
        private String name;

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Meta")
        private Map<String, String> meta;
    }

    @Getter
    @Builder
    public static class Service {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Service")
        private String name;

        @JsonProperty("Tags")
        private List<String> tags;

        @JsonProperty("Address")
        private String address;

        @JsonProperty("Meta")
        private Map<String, String> meta;

        @JsonProperty("Port")
        private int port;
    }

    @Getter
    @Builder
    public static class Check {

        @JsonProperty("Node")
        private String node;

        @JsonProperty("CheckID")
        private String checkID;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Status")
        private String status;
    }
}
