package com.alonelyleaf.prometheus.discovery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Config {

    @JsonProperty("Datacenter")
    private String dataCenter;

}
