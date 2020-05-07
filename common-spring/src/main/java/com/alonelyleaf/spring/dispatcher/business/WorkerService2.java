package com.alonelyleaf.spring.dispatcher.business;

import com.alonelyleaf.spring.dispatcher.annotations.DispatchClass;
import com.alonelyleaf.spring.dispatcher.annotations.DispatchMethod;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * @author bijl
 * @date 2019/5/29
 */
@Service
@DispatchClass
@DependsOn("dispatchRegistry")
public class WorkerService2 {


    @DispatchMethod(2)
    public void work(Object data){

        //deal with data
    }
}
