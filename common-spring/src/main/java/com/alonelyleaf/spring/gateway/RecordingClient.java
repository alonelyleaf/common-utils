package com.alonelyleaf.spring.gateway;

import com.alonelyleaf.util.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public interface RecordingClient {

    String ENDPOINT = "api/v1";

    //============================================= 资源 ===============================================

    @RequestMapping(method = RequestMethod.GET, value = ENDPOINT + "/system/resource")
    Result resource();

}
