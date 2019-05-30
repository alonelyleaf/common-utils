package com.alonelyleaf.algorithm.collection;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * https://www.cnblogs.com/lemon-flm/p/7877898.html
 *
 * 与Stack的区别在于, Stack的删除与添加都在队尾进行, 而Queue删除在队头, 添加在队尾.
 *
 * 　　add        增加一个元索                如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * 　　remove     移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
 * 　　element    返回队列头部的元素          如果队列为空，则抛出一个NoSuchElementException异常
 * 　　offer      添加一个元素并返回true      如果队列已满，则返回false
 * 　　poll       移除并返问队列头部的元素    如果队列为空，则返回null
 * 　　peek       返回队列头部的元素          如果队列为空，则返回null
 * 　　put        添加一个元素                如果队列满，则阻塞
 * 　  take       移除并返回队列头部的元素    如果队列为空，则阻塞
 *
 * @author bijl
 * @date 2019/5/29
 */
public class QueueDemo {

    //-----------------------------------------------------NoBlockingQueue--------------------------------------------------------

    /**
     * PriorityQueue
     *
     * 实质上维护了一个有序列表。加入到 Queue 中的元素根据它们的天然排序（通过其 java.util.Comparable 实现）或者根据传递给构造函数的 java.util.Comparator 实现来定位。
     */
    public static void priorityQueue(){

        PriorityQueue<Person> priorityQueue = new PriorityQueue<Person>();
        priorityQueue.add(new Person().setAge(12).setName("a"));
        priorityQueue.add(new Person().setAge(15).setName("b"));
        priorityQueue.add(new Person().setAge(11).setName("c"));
        while (!priorityQueue.isEmpty()){
            System.out.println(priorityQueue.poll());
        }
    }

    /**
     *  ConcurrentLinkedQueue
     *
     *  是基于链接节点的、线程安全的队列。并发访问不需要同步。因为它在队列的尾部添加元素并从头部删除它们，所以只要不需要
     *  知道队列的大小，ConcurrentLinkedQueue 对公共集合的共享访问就可以工作得很好。收集关于队列大小的信息会很慢，需要遍历队列。
     */
    public static void concurrentLinkedQueue(){

        ConcurrentLinkedQueue concurrentLinkedQueue = new ConcurrentLinkedQueue();
    }

    //-----------------------------------------------------BlockingQueue--------------------------------------------------------

    /**
     * ArrayBlockingQueue
     *
     * 在构造时需要指定容量， 并可以选择是否需要公平性，如果公平参数被设置true，等待时间最长的线程会优先得到处理（其实就
     * 是通过将ReentrantLock设置为true来 达到这种公平性的：即等待时间最长的线程会先操作）。通常，公平性会使你在性能上付
     * 出代价，只有在的确非常需要的时候再使用它。它是基于数组的阻塞循环队 列，此队列按 FIFO（先进先出）原则对元素进行排序。
     */
    public static void arrayBlockingQueue() throws InterruptedException {
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);

        queue.put(1);
        Integer a = queue.take();
    }

    /**
     * LinkedBlockingQueue
     *
     * 容量是没有上限的（说的不准确，在不指定时容量为Integer.MAX_VALUE，不要然的话在put时怎么会受阻呢），但是也可以选择
     * 指定其最大容量，它是基于链表的队列，此队列按 FIFO（先进先出）排序元素。
     */
    public static void linkedBlockingQueue() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

        queue.put(1);
        Integer a = queue.take();
    }

    /**
     * PriorityBlockingQueue
     *
     * 是一个带优先级的队列，而不是先进先出队列。元素按优先级顺序被移除，该队列也没有上限（看了一下源码，PriorityBlockingQueue
     * 是对 PriorityQueue的再次包装，是基于堆数据结构的，而PriorityQueue是没有容量限制的，与ArrayList一样，所以在优先阻塞 队列上
     * put时是不会受阻的。虽然此队列逻辑上是无界的，但是由于资源被耗尽，所以试图执行添加操作可能会导致 OutOfMemoryError），
     * 但是如果队列为空，那么取元素的操作take就会阻塞，所以它的检索操作take是受阻的。另外，往入该队列中的元 素要具有比较能力。
     *
     * @throws InterruptedException
     */
    public static void priorityBlockingQueue() throws InterruptedException {

        PriorityBlockingQueue<Person> priorityBlockingQueue = new PriorityBlockingQueue<Person>();
        priorityBlockingQueue.add(new Person().setAge(12).setName("a"));
        priorityBlockingQueue.add(new Person().setAge(15).setName("b"));
        priorityBlockingQueue.add(new Person().setAge(11).setName("c"));
        while (!priorityBlockingQueue.isEmpty()){
            System.out.println(priorityBlockingQueue.poll());
        }
    }

    public static class Person implements Comparable<Person>{

        private Integer age;

        private String name;

        public Integer getAge() {
            return age;
        }

        public Person setAge(Integer age) {
            this.age = age;
            return this;
        }

        public String getName() {
            return name;
        }

        public Person setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public int compareTo(Person person){

            return this.age.compareTo(person.getAge());
        }

        public String toString(){
            return "age : " + age + ",name : " + name;
        }

        public int hashCode(){
            return this.age;
        }
    }

    public static void main(String[] args) {
        try {
            priorityQueue();
            priorityBlockingQueue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
