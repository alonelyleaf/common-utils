package com.alonelyleaf.designpattern.structuremode.flyweight;


import com.alonelyleaf.designpattern.structuremode.flyweight.util.RedisUtils;

/**
 * 享元模式，主要在于共享通用对象，减少内存的使用，提升系统的访问效率。⽽而这部分共享对象通常⽐
 * 较耗费内存或者需要查询⼤量接口或者使用数据库资源，因此统⼀抽离作为共享对象使⽤。
 *
 * 关于享元模式的设计可以着学习享元⼯厂的设计，在⼀些有⼤量重复对象可复⽤用的场景下，使⽤
 * 此场景在服务端减少接口的调用，在客户端减少内存的占⽤用。是这个设计模式的主要应⽤方式。
 *
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack) @2020
 */
public class ActivityController {

    private RedisUtils redisUtils = new RedisUtils();

    public Activity queryActivityInfo(Long id) {
        Activity activity = ActivityFactory.getActivity(id);
        // 模拟从Redis中获取库存变化信息
        Stock stock = new Stock(1000, redisUtils.getStockUsed());
        activity.setStock(stock);
        return activity;
    }

}
