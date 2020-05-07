package com.alonelyleaf.spring.gateway;

import com.alonelyleaf.spring.common.constant.Constant;
import com.alonelyleaf.util.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constant.Gateway.RS_MANAGER, url = Constant.Gateway.RS_MANAGER_HOST)
public interface RegisterClient {

    String ENDPOINT = "api/v1";

    //============================================= 注册接口 ===============================================

    /**
     * 注册
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = ENDPOINT + "/access")
    Result<RegisterInfo> register(@RequestParam("appId") String appId, @RequestParam("pub-sub") String pubSub);

}
