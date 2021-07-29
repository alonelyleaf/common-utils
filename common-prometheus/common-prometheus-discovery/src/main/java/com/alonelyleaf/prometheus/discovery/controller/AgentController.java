package com.alonelyleaf.prometheus.discovery.controller;

import com.alonelyleaf.prometheus.discovery.model.Agent;
import com.alonelyleaf.prometheus.discovery.model.Config;
import com.netflix.config.ConfigurationManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Agent Controller
 *
 * @author bijl
 * @date 2021/7/7 上午10:40
 */
@RestController
public class AgentController {

    @GetMapping(value = "/v1/agent/self", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Agent getNodes() {
        String dataCenter = ConfigurationManager.getConfigInstance().getString("archaius.deployment.datacenter");
        Config config = Config.builder().dataCenter(Optional.ofNullable(dataCenter).orElse("default")).build();
        return Agent.builder().config(config).build();
    }
}
