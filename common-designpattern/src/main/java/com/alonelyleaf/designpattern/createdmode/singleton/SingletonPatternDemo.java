package com.alonelyleaf.designpattern.createdmode.singleton;

/**
 * @author bijl
 * @date 2019/6/25
 */
public class SingletonPatternDemo {

    public static void main(String[] args) {

        //获取唯一可用的对象
        LazySingleton lazySingleton = LazySingleton.getInstance();

        //获取唯一可用的对象
        HungrySingleton hungrySingleton = HungrySingleton.getInstance();

    }
}
