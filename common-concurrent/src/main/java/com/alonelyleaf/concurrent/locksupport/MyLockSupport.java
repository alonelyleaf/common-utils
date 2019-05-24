package com.alonelyleaf.concurrent.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 *
 * https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * java下面的锁、同步机制：
 *
 * 自旋等待与阻塞等待：
 * 阻塞，线程竞争资源失败，进入block等待别人唤醒，依赖于操作系统(Linxu下pthread_mutex_lock函数)，阻塞的唤醒会造成内核态与用户态切换影响锁性能
 * 自旋，线程竞争资源失败，其他线程可能会很快释放资源，当前线程循环尝试获取锁操作，避免了系统阻塞操作以提高性能，一般是循环执行空代码占着cpu不放
 * 复合，线程竞争资源失败，先自旋等待，达到一段时间后仍然失败则进入阻塞
 *
 * 可重入性：
 * 可重入，线程可以重复获取同一把锁，可以重复获取被锁的资源，或者说外层函数锁一次而内层函数再锁一次却不会造成死锁，作用就是避免死锁
 * 偏向锁，现在几乎所有锁都可重入，在一段时间内无资源竞争情况下，线程获取锁之后在后续的获取锁操作不会触发同步或一些CAS操作
 *
 * 可中断性：
 * 线程获取资源失败进入等待状态，等待时突然不想等待而直接返回资源获取失败这一结果
 *
 * 公平性：
 * 公平，先请求资源的优先获取资源
 * 非公平，对资源的获取没有时间上面的先后顺序
 *
 * 并发策略：
 * 悲观，认为一定存在资源竞争即拿到的都是别人修改过的，读写资源独占
 * 乐观，认为可能不存在资源竞争即拿到就行不管别人有没有修改过，仅在写时资源独占
 *
 * 无锁：
 * 即比较替换(CAS)，当前线程检测当前值是否符合预期值，如果是说明期间未被修改，则替换操作。底层由sun.simc.Unsafe类提供支持，
 * 类方法可以直接操作底层资源对指定内存地址的值进行获取、覆盖等，而CAS是使用他的变量原子操作的相关方法实现的。
 *
 * synchronized同步方案：
 * synchronized是一种互斥，可重入，不可中断，非公平的隐式锁实现。synchronized 的底层是一个基于CAS操作的等待队列，jvm还
 * 提供了偏向锁、轻量级锁、自旋锁、锁消除的同步优化。锁释放由方法退出、异常抛出来自动释放。
 *
 * Lock同步方案：
 * 扩展AQS一系列子类实现显示的锁，利用CLH队列控制多线程并发。包括ReentrantLock、ReentrantReadWriteLock等。AQS中线程等待
 * 包括自旋等待和park阻塞等待，后者是LockSupport调用Unsafe的park()将线程丢给系统进行阻塞，全程未使用synchronized块，也
 * 扩展了Condition实现基于条件的资源并发控制。
 *
 * 可以看到concurrent包下面的挂起与唤醒机制是使用LockSupport实现的，而LockSupport又是基于Unsafe实现的。
 *
 * @author bijl
 * @date 2019/5/24
 */
public class MyLockSupport {

    //挂起
    private static void park(){
        LockSupport.park();
    }

    private static void park(Object blocker){
        LockSupport.park(blocker);
    }

    //挂起指定时间
    public static void parkNanos(long nanos){
        LockSupport.parkNanos(nanos);
    }

    public static void parkNanos(Object blocker, long nanos){
        LockSupport.parkNanos(blocker, nanos);
    }

    //挂起到指定时间
    public static void parkUntil(long deadline) {

        LockSupport.parkUntil(deadline);
    }

    public static void parkUntil(Object blocker, long deadline) {
        LockSupport.parkUntil(blocker, deadline);
    }


    //获取线程t的字段parkBlocker值
    public static Object getBlocker(Thread t) {

        return LockSupport.getBlocker(t);
    }


    //恢复
    private static void unpark(Thread thread){
        LockSupport.unpark(thread);
    }
}
