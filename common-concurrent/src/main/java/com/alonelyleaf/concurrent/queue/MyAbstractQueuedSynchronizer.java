package com.alonelyleaf.concurrent.queue;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * AQS是整个并发框架里面锁的核心，给出了一个抽象阻塞队列同步器，用一个state表示竞争资源，队列中头结点的后续节点排队等待。
 * AQS中已经实现好了同步队列节点的维护(出队入队、删除等等)，条件队列节点的维护(用于模拟线程通信)，实现了独占和共享两种方式
 * 的获取逻辑(同步队列入队以及线程挂起)、释放逻辑(同步队列出队以及线程唤醒)，而其子类扩展四个方法则做两件事情：
 * 1、通过CAS对资源加减或者0/1赋值操作，
 * 2、获取逻辑返回正/负数用于AQS判断是否需要加入同步队列进行挂起，释放逻辑返回true/false用于AQS判断是否需要唤醒同步队列的节点。
 *    比如locks包下面的ReentrantLock、Semaphore、CountDownLatch等都有一个Sync(AQS子类)引用用于实现不同功能。
 *
 * @author bijl
 * @date 2019/5/25
 */
public class MyAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer{


}
