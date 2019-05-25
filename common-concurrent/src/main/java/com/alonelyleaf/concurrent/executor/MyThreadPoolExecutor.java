package com.alonelyleaf.concurrent.executor;

import com.alonelyleaf.concurrent.futuretask.MyCallable;

import java.util.concurrent.*;

/**
 * 源码分析 https://blog.csdn.net/qq_34448345/article/details/80087738   6.ThreadPoolExecutor
 *
 * 一般线程池包含三部分：调度器、线程队列、任务队列。其中调度器将任务队列中的任务分配给线程队列中的线程去执行，执行完毕
 * 之后线程又会到线程队列中。
 *
 * ThreadPoolExecutor是AbstractExecutorService的直接子类。提供了最基础的线程池功能。ThreadPoolExecutor就没有调度器，线程
 * 队列中的所有线程自动去获取任务队列中的任务进行执行。可见核心其实就是自动控制线程队列长度与任务队列长度相匹配，从而使
 * 任务按顺序合理执行。其中线程队列存放当前正在处理任务的线程或者空闲线程，任务队列存放待执行任务(正在被线程执行的任务是
 * 不在任务队列中的)。
 *
 * 1. 类申明和部分成员，需要注意的是corePoolSize和maximumPoolSize两个线程容量相关的参数，以及任务队列和工作线程队列等成员
 * 2. 构造器，构造器对重要的几个变量进行初始化。首先解释一下线程数量自动调整的策略，使用corePoolSize(合理容量)、
 * maximumPoolSize(最大容量)来控制线程队列的长度，每当调用execute(Runnable)提交新任务到线程池中会自动调整线程个数，就是
 * 如果来了新任务，线程优先增长到合理的个数，之后再来新任务则优先加入任务队列，任务队列放不下则线程个数增长到最大个数。
 * 如下：
 * 如果还没超过合理容量：则需要新建线程来处理新任务
 * 如果超过最合理容量且小于最大容量：任务队列未满则新任务放到任务队列中，任务队列满了则新建线程来处理新任务
 * 如果等于最大容量：任务队列未满则新任务放到任务队列中，任务队列满了则执行拒绝策略(RejectedExecutionHandler )
 *
 * 3. 内部类Worker，每个实例就是一个工作线程，每次调用execute()提交新任务时创建新线程就是Worker实例。Worker继承了AQS，
 * AQS是一个实现了fifo的队列且提供了队列节点(也就是线程)的排队、阻塞、持锁调度等机制，后续再分析AQS源码。注意Worker的
 * run()方法是直接调用了外部类的 runWorker(this)方法，传入当前工作线程实例然后与新建任务(也可能是从任务队列中拿到的)绑定，
 *
 * 4. 创建工作线程的函数addWorker()，可以指明是否参考corePoolSize参数，该方法实现了自动调整Worker线程数量的具体逻辑，
 * 调用CAS获取当前工作线程数量、线程池状态，然后根据这几个参数判断新提交的任务是否需要新建工作线程来处理
 *
 * 5. 提交任务的方法execute()，比较简单，就是判断是否需要新建线程直接执行任务，还是需要添加到任务队列里，还是要执行拒绝策略。
 *
 * 6. 任务获取的方法getTask()，从任务队列中去轮询任务。依赖于BlockingQueue带超时的 poll方法。
 *
 * 7. 任务调度方法runWorker()，上一步getTask()从任务队列取出任务，这一步需要将任务分配给空闲线程去执行，前面的Woker类的
 * run()方法会直接调用此方法并将工作线程实例自身传入。将一个工作线程与一个任务进行绑定，绑定方式是首先工作线程调用AQS的
 * 加锁代表正在处理任务，然后执行任务自己的run，最后工作线程调用AQS的解锁代表又变为空闲线程。
 *
 * 8. 停止所有工作线程interruptWorkers()、停止所有空闲线程interruptIdleWorkers()。首先线程队列中线程分为两种，一种是非空闲
 * 线程(即调用AQS上锁)，另一种是空闲线程(即没有调用AQS上锁，tryLock()可以拿到锁)。同时空闲线程会阻塞在getTask()上去轮询任务，
 * 可以看到getTask()操作中没有上Worker锁。
 *
 * 9. 中断线程池方法shutdown()、shutdownNow()。他们利用了上诉两个方法，前者先中断所有空闲线程，停止提交新任务，然后等待
 * 工作线程任务执行完毕，再调用tryTerminate()循环中断刚刚执行完任务变为空闲线程的工作线程。后者直接中断所有线程，返回队列
 * 上的任务(也就是没有被执行的任务)，
 *
 * 10. tryTerminate()方法，上面说到这个方法用来“补漏”，也就是利用for循环，每次循环会中断一个空闲线程，这个空闲线程可能
 * 是刚刚执行完任务从非空闲状态转为空闲状态(所以在shutdown()中没有被中断)。在所有需要中断线程池的地方都插入次方法，如果
 * 线程池已经进入终止流程，没有任务等待执行了，但线程池还有线程，保证这些线程能被中断处理。注意线程池状态、线程数量等都是调用CAS获取的。
 *
 * 11. 四种拒绝策略。分别是有下面几种：
 * CallerRunsPolicy：跑execute()的线程直接去跑任务的run()
 * AbortPolicy：直接抛出异常
 * DiscardPolicy：直接忽略本次被拒绝的任务
 * DiscardOldestPolicy：从任务队列中poll()一个任务丢弃掉，然后尝试exectue()提交本次任务
 *
 *
 * @author bijl
 * @date 2019/5/25
 */
public class MyThreadPoolExecutor {

    private ThreadPoolExecutor threadPoolExecutor;

    public MyThreadPoolExecutor(
            int corePoolSize,//核心线程池容量
            int maximumPoolSize,//线程池最大容量
            long keepAliveTime,//线程池空闲时线程存活时间,超过时间的空闲线程会销毁
            TimeUnit unit,//时间单位
            BlockingQueue<Runnable> workQueue,//任务阻塞队列
            ThreadFactory threadFactory,//线程工厂
            RejectedExecutionHandler handler //线程拒绝策略
             ){

        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private void process(){

        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        Long taskCount = threadPoolExecutor.getTaskCount();

    }
}
