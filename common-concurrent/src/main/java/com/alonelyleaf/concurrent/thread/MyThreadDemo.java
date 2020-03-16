package com.alonelyleaf.concurrent.thread;

import java.util.Map;

/**
 * @author bijl
 * @date 2019/11/29
 */
public class MyThreadDemo extends Thread{

    private ThreadLocal<Map<String, Object>> map = new ThreadLocal<>();

    public void test(){

        new Thread(()->{

        }).start();
    }
}
