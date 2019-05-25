package com.alonelyleaf.concurrent.futuretask;

import com.sun.istack.internal.NotNull;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 *
 * https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * Future接口：用来实现异步任务获取任务结果。Callable任务返回Future对象。
 *
 * Runnable接口：用来创建任务
 *
 * RunnableFuture接口：Future和Runnable的子接口。任务的创建、异步支持。
 *
 * FutureTask类：RunnableFuture的接口实现类。是一个支持取消的异步任务执行器，支持Callable或者Runnable类型任务的执行。
 * 他方便的将任务的执行、结果的获取放到了一起，可以阻塞式地获取任务执行结果，如果结果还未返回可以在内部维护的单链表上阻塞。
 *
 * 类申明，构造器，相关成员如下。使用state来记录任务的各种状态，调用CAS读写state以指挥任务的执行和和取消，指挥执行任务的
 * 线程的中断，指挥其他线程在链表上阻塞等待结果的返回。构造器可以直接用callable创建，也可以用runnable和result创建。
 *
 * 任务的执行，run()中直接调用callable的call()任务方法，而执行者runner是当前线程，执行完毕设置结果或者异常对象然后调用
 * finishCompletion()唤醒链表上等待结果的线程。注意最后对cancel(true)的处理是调用handlePossibleCancellationInterrupt()
 * 来响应由于取消任务使得执行线程runner的中断标记。
 *
 * 获取结果，其他线程调用get()获取结果，如果结果还未被设置，那么awaitDone创建新节点绑定该线程并加入到链表上使用park()挂起。
 * 当然也包括removeWaiter()移除队列上指定节点的逻辑。
 *
 * 任务取消。修改state为INTERRUPTING或者CANCELLED，修改失败直接返回false，修改成功则调用finishCompletion()唤醒链表上的等待线程。
 *
 * @author bijl
 * @date 2019/5/24
 */
public class MyFutureTask<V> extends FutureTask<V> {

    //增加taskId，对任务进行标记
    private String taskId;

    public MyFutureTask(@NotNull Callable<V> callable) {
        super(callable);
        taskId = UUID.randomUUID().toString();
    }

    public MyFutureTask(@NotNull Runnable runnable, V result) {
        super(runnable, result);
        taskId = UUID.randomUUID().toString();
    }

    //可重写run(),cancel()等方法进行定制
    @Override
    public void run(){

        //run()方法中会调用callable的call()任务方法
        super.run();
    }

    public String getTaskId() {
        return taskId;
    }

    public MyFutureTask<V> setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }
}
