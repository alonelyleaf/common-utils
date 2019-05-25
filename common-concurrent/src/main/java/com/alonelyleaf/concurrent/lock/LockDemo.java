package com.alonelyleaf.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author bijl
 * @date 2019/5/25
 */
public class LockDemo {

    /**
     * ReentrantLock可以说实现了synchronized的一个替代,即独占锁、重入锁,使用AQS的独占模式实现,同时还支持Condition基于条件
     * 的线程通信,同样支持公平和非公平方式。
     *
     * 类申明如下，包括AQS的子类Sync，以及Sync的两个子类分别实现公平和非公平方式。资源state=0表示目前未被占用，state=n表示
     * 资源被某个线程占用且重入次数为n。另一方面非公平模式下AQS子类tryAcquire()会直接尝试CAS修改资源，也就是说lock()调用
     * acquire()是公平的，因为大家都会尝试CAS而没有先后顺序。而公平模式下的tryAcquire()会先调用hasQueuedPredecessors()判断
     * 当前线程是否是队列第一个线程(这个线程等得最久)，如果是才允许调用CAS修改资源否则加入队列。
     */
    private ReentrantLock reentrantLock = new ReentrantLock();


    /**
     * ReentrantReadWriteLock实现了一个读共享、写独占的读写锁，以优化ReentrantLock的同步效率。也就是说读锁共享且可重入,
     * 写锁互斥且可重入,写锁只能在没有任何锁被占用的情况下才能申请成功,而读锁只能在没有写锁被占用的情况下才能申请成功。
     *
     * 使用内部类Sync继承AQS,并扩展了两个子类NonfairSync、FairSync实现公平与非公平模式下的线程同步处理，本质上是混用AQS的
     * 独占、共享模式实现的。使用state的高16为记录读锁被占用的线程数,使用低16为来记录写锁被同一个线程的重入次数,这里使用
     * state(R:0,W:0)来表示，前面说到AQS只负责节点入队或者唤醒，而子类Sync负责资源state的CAS修改，CAS尝试修改失败或者判断
     * 该线程不符合持锁规则需要交给AQS加入同步队列，这里Sync的两个子类NonfairSync、FairSync在调用CAS修改资源前检查节点是否
     * 符合公平性规则。同时使用两个内部类ReadLock、WriterLock实现了Lock接口以导出公有的上锁和解锁的客户端方法，也就是说
     * 相关的方法都委托Sync、NonfairSync、FairSync来实现。
     *
     * 读写锁规则：
     * 1.如果当前线程获取write锁,当前线程也可以获取read锁,即锁降级
     * 2.如果当前线程获取write锁,其他线程不能获取任何锁
     * 3.如果当前线程获取read锁.其他线程也可以获取read锁
     *
     * 大概说一下锁流程：
     * 首先state(R:0,W:0)
     * 线程A申请read锁,使得state(R:1,W:0)
     * 线程B申请read锁,使得state(R:2,W:0)
     * 线程C申请write锁,由于state(R:2,W:0),获取失败,线程C加入同步队列
     * 线程D申请read锁,虽然现在是read锁占有阶段,但由于同步队列中有等待中的C,线程D加入同步队列
     * 线程A、B均释放read锁,使得state(R:0,W:0),会按顺序唤醒同步队列
     * 线程C被唤醒,获取write锁,使得state(R:0,W:1)
     * 线程C可锁降级,获取read锁,使得state(R:1,W:1)
     * 在write锁被占有阶段,C之外的其他线程不能获取任何锁,均需要加入同步队列
     *
     * 关于公平与非公平：
     * Sync有两个空方法writerShouldBlock()、readerShouldBlock()交给子类实现，将公平性判断交给子类。只有返回false才允许后续调用CAS修改state。
     * NonfairSync(非公平)：写锁writerShouldBlock()直接返回false让当前线程和刚刚从队列上唤醒的线程去竞争,这个刚刚被唤醒的
     * 线程可以是请求读锁、写锁的线程。读锁readerShouldBlock()在同步队列第一个节点(head的后继)是共享节点,也就是说即将被唤醒
     * 的节点是请求读锁的节点情况下返回false让当前线程和刚刚从队列上唤醒的线程去竞争,这个刚唤醒的线程必须是请求读锁的线程。
     * 这里的竞争就是各自调用CAS修改资源谁成功看运气，使得成功与否与请求顺序无关。
     * FairSync(公平)：写锁writerShouldBlock()、读锁readerShouldBlock()均先判断当前线程是否是队列第一个节点绑定的线程(此线程
     * 等得最久)。如果不是则返回true后续由AQS加入同步队列。这样只能按照队列顺序挨个唤醒去竞争。
     *
     * 源码划分为下面几个部分：
     * 1.Sync部分：是读写锁的核心，实现了独占、共享方式的state修改，将state高16记录占用读锁的线程数，低16为记录独占写锁重入次数。
     * 2.NonfairSync、FairSync部分：分别实现了非公平、公平性过滤，AQS在调用CAS修改state前会先检查公平性。
     * 3.ReadLock、WriteLock部分：调用Sync、NonfairSync、FairSync导出一系列公有的客户端方法。
     * 4.ReentrantReadWriteLock部分：使用上面5个内部类合起来完成读写锁。
     *
     * Sync部分：
     * (1)Sync使用state来记录占用读锁的线程数和独占写锁时的重入次数。
     * (2)readerShouldBlock()、writerShouldBlock()交给子类实现以判断是否公平是否需要禁止修改state。
     * (3)tryAcquire()尝试获取资源(独占)，是交给写锁用的，所以如果已经占有写锁直接set更新重入计数，如果当前无任何锁则尝
     * 试占用写锁即调用CAS修改state，如果当前是读锁阶段，或者由子类判断当前线程需要阻塞，这两种情况会返回false由AQS添加到同步队列中。
     * (4)tryRelease()尝试释放资源(独占)，是交给写锁用的,这里调用CAS更新写锁重入记录。
     * (5)tryAcquireShared()尝试获取资源(共享)，这是给读锁用的，写锁占用节点直接返回失败由AQS添加到队列，只要不是写锁占用
     * 阶段都可以尝试获取读锁即调用CAS增加占用读锁的线程数，返回负数会由AQS添加到同步队列。
     * (6)tryReleaseShared()尝试释放资源(共享)。
     * (7)tryWriteLock()尝试上write锁，和tryAcquireShared区别在于，前者仅做一次CAS尝试,不涉及队列操作，所以立即返回结果，
     * 后者在AQS一系列调用链上，涉及队列操作，所以失败会添加到队列上阻塞直到成功。
     * (8)tryReadLock()尝试上read锁，前者仅做一次CAS尝试，不涉及队列操作，所以立即返回结果，后者在AQS一系列调用链上，涉及
     * 队列操作，所以失败会添加到队列上阻塞直到成功。
     *
     * NonfairSync、FairSync部分：
     * NonfairSync(非公平)：写锁writerShouldBlock()直接返回false让当前线程和刚刚从队列上唤醒的线程去竞争,这个刚刚被唤醒的
     * 线程可以是请求读锁、写锁的线程。读锁readerShouldBlock()在同步队列第一个节点(head的后继)是共享节点,也就是说即将被唤
     * 醒的节点是请求读锁的节点情况下返回false让当前线程和刚刚从队列上唤醒的线程去竞争,这个刚唤醒的线程必须是请求读锁的线
     * 程。这里的竞争就是各自调用CAS修改资源谁成功看运气，使得成功与否与请求顺序无关。
     *
     * FairSync(公平)：写锁writerShouldBlock()、读锁readerShouldBlock()均先判断当前线程是否是队列第一个节点绑定的线程(此
     * 线程等得最久)。如果不是则返回true后续由AQS加入同步队列。这样只能按照队列顺序挨个唤醒去竞争。
     *
     *
     */
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();


    /**
     * CountDownLatch实现了一个闭锁，计数值由构造器传入，线程调用await会阻塞在门外面,调用countDown会减少一道门闩(也就是
     * 计数器),当门闩减到0表示可以开门,所有await阻塞的线程可以继续向下执行。下面分析其源码。
     *
     * 类申明，使用内部类实现了一个AQS。前面说了AQS自己实现了同步队列的维护包括节点绑定线程的阻塞、唤醒等等。然后具体资源
     * 的加减或0/1赋值由子类调用CAS来控制。
     *
     * AQS要求子类tryAcquireShared()返回负数才能调用doAcquireShared()将线程加入同步队列进行挂起，所以这里的实现是判断资源
     * 剩余量，如果资源还有剩余说明countDown()调用次数还不够，调用await的仍然需要继续阻塞。
     *
     * AQS要求子类tryReleaseShared()返回true才能调用doRelwaseShared()连续唤醒同步队列上的节点，所以这里的实现是资源开始
     * 不为0，但是经过本次减1操作变为0，说明countDown()调用次数够了，调用await()的线程可以全部被唤醒。
     *
     * 方法await()可以在资源不为0的情况下，让调用他的线程全部挂起。具体的是调用AQS的acquireShared()的过程，先调用Sync实现
     * 的tryAcquireShared()，如果资源不为0返回负数，则AQS调用doAcquireShared()将线程加入同步队列进行挂起
     *
     * 方法countDown()可以在资源减到0的情况下，唤醒所有被挂起的线程。具体的是调用AQS的releaseShared()的过程，先调用
     * tryReleaseShared()将资源利用CAS减1，在减到0时返回true，则AQS调用doRelwaseShared()连续唤醒同步队列的的节点。
     *
     */
    private CountDownLatch countDownLatch = new CountDownLatch(10);


}
