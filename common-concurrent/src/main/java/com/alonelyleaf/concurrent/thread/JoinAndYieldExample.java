package com.alonelyleaf.concurrent.thread;

/**
 * @author bijl
 * @date 2020/5/20
 */
public class JoinAndYieldExample {

    public static void main(String[] args) throws InterruptedException
    {
        joinExample();
        yieldExample();
    }

    private static void joinExample() throws InterruptedException{
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("First task started");
                System.out.println("Sleeping for 2 seconds");
                try
                {
                    Thread.sleep(2000);

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println("First task completed");
            }
        });
        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Second task completed");
            }
        });
        //在t执行完毕后t1执行
        t.start(); // Line 15
        t.join(); // Line 16
        t1.start();

    }

    private static void yieldExample() throws InterruptedException{
        Thread t = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("First task started");
                System.out.println("Sleeping for 2 seconds");
                try
                {
                    Thread.yield();
                    Thread.sleep(2000);

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println("First task completed");
            }
        });
        Thread t1 = new Thread(new Runnable()
        {
            public void run()
            {
                System.out.println("Second task completed");
            }
        });
        //在t执行完毕后t1执行
        t.start(); // Line 15
        t1.start();

    }
}

