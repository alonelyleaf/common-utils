package com.alonelyleaf.concurrent.share;

import java.util.concurrent.Semaphore;

/**
 * @author bijl
 * @date 2020/4/28
 */
public class SemaphoreDemo {

    private static final Semaphore semaphoreToken = new Semaphore(10);

    /**
     * aquire()函数获取许可证的顺序和调用的先后顺序有关系吗，也就是说该例子中客户端是否是排队获取令牌的？答案不是，
     * 因为Semaphore默认是非公平的，当然其构造函数提供了设置为公平信号量的参数。
     */

    public static void main(String[] args) {
        for (int ix = 0; ix != 100; ix++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        semaphoreToken.acquire();
                        System.out.println("select * from xxx");
                        semaphoreToken.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
