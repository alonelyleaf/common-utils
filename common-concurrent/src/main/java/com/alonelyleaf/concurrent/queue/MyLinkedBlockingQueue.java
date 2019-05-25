package com.alonelyleaf.concurrent.queue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 *
 *  LinkedBlockingQueue实现了一个可阻塞的fifo链表队列。相关方法基于双锁实现线程安全，takeLock(出队锁)、putLock(入队锁)，
 *  允许出队、入队并行,但是其他操作需要上双锁比如remove()、contains()、toArray()、toString()、clear()、drainTo()、writeObject()等等。
 *  在大部分并发场景下，LinkedBlockingQueue的吞吐量比ArrayBlockingQueue更好。利用ReentrantLock()的lock()和lockInterruptibly()实现阻塞式获取锁。
 *
 * 1. 类申明，部分成员。节点类实现链表结构，申明了两个ReentrantLock()锁进行读写的并发控制，申明了两个Condition用于线程通信。
 * 2. 构造函数，队列长度、初始化集合进行重载，
 * 3. 一些基本方法、工具函数，比如入队、出队、线程通信、双锁、删除元素、清空链表等等
 * 4. 阻塞式的入队操作，利用入队锁ReentrantLock的lockInterruptibly()、lock()上锁实现阻塞。
 * 5. 阻塞式的出队操作，利用入队锁ReentrantLock的lockInterruptibly()、lock()上锁实现阻塞。
 * 6. 迭代器，迭代是上双锁的，并行迭代器。
 *
 *
 * BlockingQueue定义的常用方法如下:
 *      抛出异常  特殊值   阻塞    超时
 * 插入  add(e)   offer(e) put(e)  offer(e, time, unit)
 * 移除 remove()  poll()   take()  poll(time, unit)
 * 检查 element() peek()  不可用  不可用
 *
 * 1)add(anObject):把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则抛出异常
 *
 * 2)offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
 *
 * 3)put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
 *
 * 4)poll(time):取走BlockingQueue里排在首位的对象,若BlockingQueue为空,则可以等time参数规定的时间,取不到时返回null
 *
 * 5)peek(): 返回BlockingQueue里排在首位的对象，并不移除,若BlockingQueue为空,则可以等time参数规定的时间,取不到时返回null
 *
 * 6)take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止
 *
 * 7)remove(): 取走BlockingQueue里排在首位的对象,若BlockingQueue为空，则抛出NoSuchElementException异常
 *
 * 8)element(): 返回BlockingQueue里排在首位的对象，并不移除,若BlockingQueue为空，则抛出NoSuchElementException异常
 *
 * 其中：BlockingQueue 不接受null 元素。试图add、put 或offer 一个null 元素时，某些实现会抛出NullPointerException。null 被用作指示poll 操作失败的警戒值。
 *
 * @author bijl
 * @date 2019/5/25
 */
public class MyLinkedBlockingQueue {

    private LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>(100);

    public void put(String content){
        try {
            linkedBlockingQueue.put(content);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String take(){
        if (linkedBlockingQueue.isEmpty()){
            return null;
        }
        try {
            return linkedBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
