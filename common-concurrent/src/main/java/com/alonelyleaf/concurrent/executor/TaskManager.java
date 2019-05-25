package com.alonelyleaf.concurrent.executor;

import com.alonelyleaf.concurrent.futuretask.MyFutureTask;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 *  https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * 线程资源比较重要，为了复用已经创建的线程，减少在创建和销毁线程时产生的性能开销。因为创建和管理线程非常心累，并且操作
 * 系统通常对线程数有限制，所以建议使用线程池来并发执行任务，而不是每次请求进来时创建一个线程。
 *
 * Executor：将任务本身和任务的执行分离(Thread而是将全部柔和到一起很不方面管理)。用于调度Runnable和Callable。
 *
 * ExecutorService ：对Excetor进行扩展，提供了Future异步支持(任务提交后在需要时检查 Future是否有结果，其get() 方法是阻塞
 * 式的，如果调用时任务还没有完成，会等待直到任务执行结束)。
 *
 * AbstractExecutorService给出ExecutorService的一个抽象实现。用于异步任务、任务执行的调度管理等。依次分析相关方法。
 * 1. 创建任务，使用任务来构造FutureTask并返回
 * 2. 提交任务，就是调用execute()，传入任务进行执行，这里交给子类实现
 * 3. 执行给定任务集合，返回一个已经成功完成的任务的结果(任务得到结果、没有抛出异常视为执行成功)
 * 4. 执行给定任务，所有任务成功完成()则返回其future集合
 *
 * @author bijl
 * @date 2019/5/25
 */
public class TaskManager {

    private ExecutorService blockingExecutor = Executors.newFixedThreadPool(10);

    private ExecutorService nonBlockingExecutor = Executors.newFixedThreadPool(10);

    private static final ConcurrentHashMap<String, MyFutureTask> taskMap = new ConcurrentHashMap<>();

    /**
     * 需要阻塞的任务
     *
     * @param task
     */
    private Integer addBlockingTask(MyFutureTask<Integer> task){

        blockingExecutor.submit(task);

        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 不需要阻塞的任务
     *
     * @param task
     */
    public void addNonBlockingTask(MyFutureTask<Integer> task) {

        taskMap.putIfAbsent(task.getTaskId(), task);
        nonBlockingExecutor.submit(task);
    }

    /**
     * 获取执行结果
     *
     * @param taskId
     */
    public Integer getResult(String taskId){

        if (taskMap.get(taskId) == null){
            return 0;
        }
        MyFutureTask<Integer> task = taskMap.get(taskId);
        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * ExecutorService 方法展示
     */
    public class ExecutorServiceDemo{

        private ExecutorService executor = Executors.newFixedThreadPool(100);

        //关闭线程池,不再接受新任务,等待所有任务完成
        void shutdown(){
            executor.shutdown();
        }
        //关闭线程池,不再接受新任务,返回等待执行的任务列表
        List<Runnable> shutdownNow(){
            return executor.shutdownNow();
        }
        //线程池是否关闭
        boolean isShutdown(){
            return executor.isShutdown();
        }
        //线程池关闭,所有任务均完成则返回true
        boolean isTerminated(){
            return executor.isTerminated();
        }
        //阻塞,直到所有线程执行完毕或者超时
        boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException{
            return executor.awaitTermination(timeout, unit);
        }
        //提交一个任务,交给线程池执行(callable自带返回结果)
        <T> Future<T> submit(Callable<T> task){
            return executor.submit(task);
        }
        //提交一个任务,交给线程池执行(runnable不自带返回结果,用result表示)
        <T> Future<T> submit(Runnable task, T result){
            return executor.submit(task, result);
        }
        //提交一个任务交给线程池执行,并返回该任务的future
        Future<?> submit(Runnable task){
            return executor.submit(task);
        }
        //执行给定任务集合(可指定超时时间),全部完成(未抛出异常),返回future集合
        <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException{
            return executor.invokeAll(tasks);
        }
        <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException{
            return executor.invokeAll(tasks, timeout, unit);
        }
        //执行给定任务(可指定超时时间),完成(未抛出异常),返回future
        <T> T invokeAny(Collection<? extends Callable<T>> tasks)  throws InterruptedException, ExecutionException{
            return executor.invokeAny(tasks);
        }
        <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException{
            return executor.invokeAny(tasks, timeout, unit);
        }
    }
}
