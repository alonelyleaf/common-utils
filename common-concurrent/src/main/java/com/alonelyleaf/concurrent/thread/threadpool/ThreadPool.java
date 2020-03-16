package com.alonelyleaf.concurrent.thread.threadpool;

/**
 * @author bijl
 * @date 2019/11/29
 */
public interface ThreadPool <Task extends Runnable>{

    //执行一个Job任务，需要实现Runnable
    void execute(Task task);

    //关闭线程池
    void shutDown();

    //增加工作者线程
    void addWorkers(int num);

    //移除工作者线程
    void removeWorkers(int num);

    //获取等待队列中的任务数量
    int getWorkingQueueSize();
}
