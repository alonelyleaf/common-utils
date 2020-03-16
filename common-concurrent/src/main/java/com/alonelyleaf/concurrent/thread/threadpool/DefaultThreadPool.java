package com.alonelyleaf.concurrent.thread.threadpool;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bijl
 * @date 2019/11/29
 */
public class DefaultThreadPool<Task extends Runnable> implements ThreadPool<Task> {

    private static final int MAX_WORKER_NUMBERS = 10;
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    private static final int MIN_WORKER_NUMBERS = 1;

    //工作队列
    private final LinkedList<Task> bolockingQueue = new LinkedList<>();
    //线程池中处理任务的线程列表
    private final LinkedList<Worker> workers = new LinkedList<>();
    private int workerNum = DEFAULT_WORKER_NUMBERS;

    private AtomicLong threadNum = new AtomicLong();

    //创建默认大小的线程池
    public DefaultThreadPool() {
        initializeWorkers(DEFAULT_WORKER_NUMBERS);
    }

    //创建指定大小的线程池
    public DefaultThreadPool(int num) {
        workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS
                : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initializeWorkers(workerNum);
    }

    //初始化线程池中的线程,即事先创建多个线程放入线程池中
    private void initializeWorkers(int num) {
        for(int i = 0; i < num; i++){
            Worker workerThread = new Worker();
            workers.add(workerThread);

            Thread thread = new Thread(workerThread, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }
    }

    //放入工作队列中，执行任务
    @Override
    public void execute(Task task) {
        if(task != null){
            synchronized (bolockingQueue) {
                bolockingQueue.addLast(task);
                bolockingQueue.notify();
            }
        }

    }

    @Override
    public void shutDown() {
        for(Worker worker : workers){
            worker.shutDown() ;
        }

    }

    //添加工作线程
    @Override
    public void addWorkers(int num) {
        synchronized (bolockingQueue) {
            if(num + this.workerNum > MAX_WORKER_NUMBERS){
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum += num;
        }

    }

    //获取工作队列大小
    @Override
    public int getWorkingQueueSize() {
        return bolockingQueue.size();
    }

    //停止工作线程
    @Override
    public void removeWorkers(int num) {
        synchronized (bolockingQueue) {
            if(num >= this.workerNum){
                throw new IllegalArgumentException("参数错误");
            }
            int count = 0;
            while(count < num){
                Worker worker = workers.get(count);
                if(workers.remove(worker)){
                    worker.shutDown();
                    count++;
                }
            }
            this.workerNum -= count;
        }

    }

    //工作线程，负责处理任务
    class Worker implements Runnable{

        private volatile boolean running = true;
        @Override
        public void run() {
            while(running){
                Task task;
                synchronized (bolockingQueue) {
                    while(bolockingQueue.isEmpty()){
                        try {
                            bolockingQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    //取出工作队列的头任务执行
                    task = bolockingQueue.removeFirst();
                }
                if(task != null){
                    task.run();
                }
            }
        }

        public void shutDown(){
            running = false;
        }

    }

}
