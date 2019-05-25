package com.alonelyleaf.concurrent.semaphore;

import java.util.concurrent.Semaphore;

/**
 * https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * Semaphore提供了一种基于许可的同步方式，构造时初始化一个许可数量n，acquire()阻塞方式获取1个许可，release()返回1个许可，
 * 这样可以控制两个方法之间的任务代码只能最多有n个持有许可的线程同时执行，其余线程绑定节点后在同步队列上挂起等待。
 *
 * 与ReentrantLock类似，Lock()上锁，Unlock()释放锁，使用的是AQS的独占模式，而Semaphore使用的是AQS的共享模式。
 *
 *
 * @author bijl
 * @date 2019/5/25
 */
public class MySemaphore {

    private Semaphore semaphore = new Semaphore(10);

    /**
     * 获取许可
     */
    public void acquire(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //释放许可
    public void release() {
        semaphore.release();
    }
    //释放多个许可
    public void release(int permits) {
        semaphore.release(permits);
    }

    //返回可用许可数
    public int availablePermits() {
        return semaphore.availablePermits();
    }
    //清空剩余许可
    public int drainPermits() {
        return semaphore.drainPermits();
    }

    //判断是否公平
    public boolean isFair() {
        return semaphore.isFair();
    }
    //判断当前线程是否是同步队列上的等待线程
    public final boolean hasQueuedThreads() {
        return semaphore.hasQueuedThreads();
    }
    //返回同步队列长度,这里是貌似是返回没有拿到许可的线程数量
    public final int getQueueLength() {
        return semaphore.getQueueLength();
    }

}
