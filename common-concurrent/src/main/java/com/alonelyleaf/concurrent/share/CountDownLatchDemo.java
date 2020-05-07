package com.alonelyleaf.concurrent.share;

import java.util.concurrent.CountDownLatch;

/**
 * https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/AQS
 *
 * @author bijl
 * @date 2020/4/28
 */
public class CountDownLatchDemo {

    private static final CountDownLatch countDownLatch = new CountDownLatch(5);

    public static void main(String[] args) throws InterruptedException {
        //循环创建5个工作线程
        for( int ix = 0; ix != 5; ix++ ){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        System.out.println( Thread.currentThread().getName() + " start" );
                        Thread.sleep(1000);
                        countDownLatch.countDown();
                        System.out.println( Thread.currentThread().getName() + " stop" );
                    } catch ( InterruptedException ex ){
                    }
                }
            }, "Task-Thread-" + ix ).start();

            Thread.sleep(500);
        }
        //主线程等待所有任务完成
        countDownLatch.await();
        System.out.println("All task has completed.");
    }

    /**
     *
     * 在主线程创建了5个工作线程后，就会阻塞在countDownLatch.await()，等待5个工作线程全部完成任务后返回。
     * 任务的执行顺序可能会不同，但是任务完成的Log一定会在最后显示。CountDownLatch通过计数器值的控制，
     * 实现了允许一个或多个线程等待其他线程完成操作的并发控制。
     *
     * 运行结果
     *
     * Task-Thread-0 start
     * Task-Thread-1 start
     * Task-Thread-0 stop
     * Task-Thread-2 start
     * Task-Thread-1 stop
     * Task-Thread-3 start
     * Task-Thread-2 stop
     * Task-Thread-4 start
     * Task-Thread-3 stop
     * Task-Thread-4 stop
     * All task has completed.
     */

}
