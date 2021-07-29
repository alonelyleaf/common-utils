package com.alonelyleaf.prometheus.discovery.wrapper;

import com.alonelyleaf.prometheus.discovery.model.Service;
import com.alonelyleaf.prometheus.discovery.model.ServiceHealth;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 服务信息 Wrapper
 *
 * @author bijl
 * @date 2021/7/7 上午11:12
 */
public class ServiceInfoWrapper {

    private static final List<String> NO_SERVICE_TAGS = new ArrayList<>();

    public static Service map(ServiceInstance instanceInfo) {
        String address = instanceInfo.getHost();
        return Service.builder()
            .address(address)
            .serviceAddress(address)
            .serviceName(instanceInfo.getServiceId())
            .serviceID(getServiceId(instanceInfo))
            .servicePort(instanceInfo.getPort())
            .node(instanceInfo.getServiceId())
            .nodeMeta(Collections.emptyMap())
            .serviceMeta(instanceInfo.getMetadata())
            .serviceTags(NO_SERVICE_TAGS)
            .build();
    }

    public static ServiceHealth mapToHealth(ServiceInstance instanceInfo) {

        String address = instanceInfo.getHost();
        String serviceId = getServiceId(instanceInfo);
        ServiceHealth.Node node = ServiceHealth.Node.builder()
            .name(instanceInfo.getServiceId())
            .address(address)
            .meta(Collections.emptyMap())
            .build();
        ServiceHealth.Service service = ServiceHealth.Service.builder()
            .id(serviceId)
            .name(instanceInfo.getServiceId())
            .tags(NO_SERVICE_TAGS)
            .address(address)
            .meta(instanceInfo.getMetadata())
            .port(instanceInfo.getPort())
            .build();
        ServiceHealth.Check check = ServiceHealth.Check.builder()
            .node(instanceInfo.getServiceId())
            .checkID("service:" + instanceInfo.getServiceId())
            .name("Service '" + instanceInfo.getServiceId() + "' check")
            .status(instanceInfo.getMetadata().get("nacos.healthy"))
            .build();
        return ServiceHealth.builder()
            .node(node)
            .service(service)
            .checks(Collections.singletonList(check))
            .build();
    }

    private static String getServiceId(ServiceInstance instanceInfo) {
        return instanceInfo.getHost() + ":" + instanceInfo.getPort();
    }
}
