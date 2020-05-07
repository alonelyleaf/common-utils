package com.alonelyleaf.concurrent.share;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://zhuanlan.zhihu.com/p/28049343
 *
 * @author bijl
 * @date 2020/4/28
 */
public class CyclicBarrierDemo {

    /**
     *
     * 首先在线程没有调用够N次cyclicBarrier.await()时，所有线程都会阻塞在cyclicBarrier.await()上，也就是说必须N个线程都到达屏障，所有线程才会越过屏障继续执行。
     * 验证了BarrierAction的执行时机是所有阻塞线程都到达屏障之后，并且BarrierAction执行后，所有线程才会从await()方法返回，继续执行。
     *
     * 在执行BarrierAction之后，count值被重置，重新等待线程到达屏障
     *
     * 运行结果
     *
     * Thread-0 start
     * Thread-1 start
     * Thread-2 start
     * Thread-3 start
     * Thread-4 start
     * MyBarrierAction is call.
     * Thread-4 stop
     * Thread-0 stop
     * Thread-1 stop
     * Thread-2 stop
     * Thread-3 stop
     */

    private final static CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new MyBarrierAction());
    private final static AtomicInteger atcIx = new AtomicInteger(1);

    public static void main(String[] args) {
        for (int ix = 0; ix != 30; ix++){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        System.out.println(Thread.currentThread().getName() + " start");
                        //Thread.sleep(atcIx.getAndIncrement() * 1000 );
                        cyclicBarrier.await();
                        System.out.println( Thread.currentThread().getName() + " stop" );
                    } catch ( Exception ex){
                    }
                }
            }, "Thread-" + ix).start();
        }
    }

    private static class MyBarrierAction implements Runnable {
        @Override
        public void run() {
            System.out.println("MyBarrierAction is call.");
        }
    }
}
