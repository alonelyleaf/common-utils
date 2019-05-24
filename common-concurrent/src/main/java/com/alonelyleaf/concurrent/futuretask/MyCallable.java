package com.alonelyleaf.concurrent.futuretask;

import java.util.concurrent.Callable;

/**
 * @author bijl
 * @date 2019/5/24
 */
public class MyCallable implements Callable<Integer> {

    public Integer call(){

        return 1;
    }
}
