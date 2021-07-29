package com.alonelyleaf.prometheus.discovery.service;

import com.alonelyleaf.prometheus.discovery.model.ChangeItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import rx.Single;

import java.util.*;
import java.util.function.Supplier;

/**
 * 注册服务
 *
 * @author bijl
 * @date 2021/7/7 上午10:43
 */
@Service
public class RegistrationService {

    private static final String[] NO_SERVICE_TAGS = new String[0];

    @Autowired
    private DiscoveryClient discoveryClient;

    public Single<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index) {

        return returnDeferred(waitMillis, index, () -> {
            List<String> services = discoveryClient.getServices();
            Set<String> set = new HashSet<>();
            set.addAll(services);

            Map<String, String[]> result = new HashMap<>();
            for (String item : set) {
                result.put(item, NO_SERVICE_TAGS);
            }
            return result;
        });
    }

    public Single<ChangeItem<List<ServiceInstance>>> getService(String appName, long waitMillis, Long index) {

        return returnDeferred(waitMillis, index,
            () -> {
                List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(appName);
                if (serviceInstanceList == null) {
                    return Collections.emptyList();
                } else {
                    return serviceInstanceList;
                }
            });
    }


    private <T> Single<ChangeItem<T>> returnDeferred(long waitMillis, Long index, Supplier<T> fn) {

        return Single.just(new ChangeItem<>(fn.get(), System.currentTimeMillis()));
    }

}
