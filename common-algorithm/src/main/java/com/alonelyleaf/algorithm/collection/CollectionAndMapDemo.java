package com.alonelyleaf.algorithm.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * https://github.com/CyC2018/CS-Notes/blob/master/docs/notes/Java%20%E5%AE%B9%E5%99%A8.md
 *
 * @author bijl
 * @date 2019/5/29
 */
public class CollectionAndMapDemo {


    /**
     *  ArrayList
     *
     *  因为 ArrayList 是基于数组实现的，所以支持快速随机访问。RandomAccess 接口标识着该类支持快速随机访问。
     *
     *  数组的默认大小为 10。
     *  添加元素时使用 ensureCapacityInternal() 方法来保证容量足够，如果不够时，需要使用 grow() 方法进行扩容，新容量的
     *  大小为 oldCapacity + (oldCapacity >> 1)，也就是旧容量的 1.5 倍。
     *
     *  扩容操作需要调用 Arrays.copyOf() 把原数组整个复制到新数组中，这个操作代价很高，因此最好在创建 ArrayList 对象时就
     *  指定大概的容量大小，减少扩容操作的次数。
     */
    public void arrayList(){

        ArrayList<Object> list1 = new ArrayList<>();
        ArrayList<Object> list2 = new ArrayList<>(15);

        //使用 Collections.synchronizedList(); 得到一个线程安全的 ArrayList。
        List<Object> synList = Collections.synchronizedList(list1);


        /**
         * 使用 concurrent 并发包下的 CopyOnWriteArrayList 类得到一个线程安全的 ArrayList。
         *
         * CopyOnWriteArrayList 在写操作的同时允许读操作，大大提高了读操作的性能，因此很适合读多写少的应用场景。
         *
         * 但是 CopyOnWriteArrayList 有其缺陷：
         * 内存占用： 在写操作时需要复制一个新的数组，使得内存占用为原来的两倍左右；
         * 数据不一致： 读操作不能读取实时性的数据，因为部分写操作的数据还未同步到读数组中。
         * 所以 CopyOnWriteArrayList 不适合内存敏感以及对实时性要求很高的场景。
         */
        List<Object> list = new CopyOnWriteArrayList<>();
    }

    /**
     * Vector
     *
     * 它的实现与 ArrayList 类似，但是使用了 synchronized 进行同步。
     *
     * Vector 是同步的，因此开销就比 ArrayList 要大，访问速度更慢。最好使用 ArrayList 而不是 Vector，因为同步操作完全可以由程序员自己来控制；
     * Vector 每次扩容请求其大小的 2 倍空间，而 ArrayList 是 1.5 倍。
     */
    public void vector(){

        Vector<Object> vector = new Vector<>();
        Vector<Object> vector2 = new Vector<>(15);
    }

    /**
     * LinkedList
     *
     * 基于双向链表实现，使用 Node 存储链表节点信息。
     * 每个链表存储了 first 和 last 指针;
     *
     * 与 ArrayList 的比较
     * ArrayList 基于动态数组实现，LinkedList 基于双向链表实现；
     * ArrayList 支持随机访问，LinkedList 不支持；
     * LinkedList 在任意位置添加删除元素更快。
     *
     */
    public void linkedList(){

        LinkedList<Object> linkedList = new LinkedList<>();
    }

    /**
     * HashMap
     *
     * 1. 存储结构
     * 内部包含了一个 Entry 类型的数组 table。
     * Entry 存储着键值对。它包含了四个字段，从 next 字段我们可以看出 Entry 是一个链表。即数组中的每个位置被当成一个桶，
     * 一个桶存放一个链表。HashMap 使用拉链法来解决冲突，同一个链表中存放哈希值和散列桶取模运算结果相同的 Entry。
     *
     * 2. 查找
     * 需要分成两步进行：
     * 计算键值对所在的桶；
     * 在链表上顺序查找，时间复杂度显然和链表的长度成正比。
     *
     * 3. put操作
     * HashMap 允许插入键为 null 的键值对。但是因为无法调用 null 的 hashCode() 方法，也就无法确定该键值对的桶下标，只能
     * 通过强制指定一个桶下标来存放。HashMap 使用第 0 个桶存放键为 null 的键值对。
     * 使用链表的头插法，也就是新的键值对插在链表的头部，而不是链表的尾部。
     *
     * 4. 确定桶下标
     * 4.1 计算 hash 值
     * 4.2 取模 位运算的代价比求模运算小的多，因此在进行这种计算时用位运算的话能带来更高的性能。
     *
     * 5. 扩容-基本原理
     * 当需要扩容时，令 capacity 为原来的两倍。
     * 扩容使用 resize() 实现，需要注意的是，扩容操作同样需要把 oldTable 的所有键值对重新插入 newTable 中，因此这一步是很费时的。
     *
     * 6. 扩容-重新计算桶下标
     * 在进行扩容时，需要把键值对重新放到对应的桶上。HashMap 使用了一个特殊的机制，可以降低重新计算桶下标的操作。
     * 假设原数组长度 capacity 为 16，扩容之后 new capacity 为 32：
     * capacity     : 00010000
     * new capacity : 00100000
     * 对于一个 Key，
     * 它的哈希值如果在第 5 位上为 0，那么取模得到的结果和之前一样；
     * 如果为 1，那么得到的结果为原来的结果 +16。
     *
     * 7. 计算数组容量
     * HashMap 构造函数允许用户传入的容量不是 2 的 n 次方，因为它可以自动地将传入的容量转换为 2 的 n 次方。
     *
     * 8. 链表转红黑树
     * 从 JDK 1.8 开始，一个桶存储的链表长度大于 8 时会将链表转换为红黑树。
     *
     * 9. 与 HashTable 的比较
     * HashTable 使用 synchronized 来进行同步。
     * HashMap 可以插入键为 null 的 Entry。
     * HashMap 的迭代器是 fail-fast 迭代器。
     * HashMap 不能保证随着时间的推移 Map 中的元素次序是不变的。
     */
    public void hashMap(){

        HashMap<String, String> hashMap = new HashMap<>();
    }

    /**
     * ConcurrentHashMap
     *
     * 1. 存储结构
     * ConcurrentHashMap 和 HashMap 实现上类似，最主要的差别是 ConcurrentHashMap 采用了分段锁（Segment），每个分段锁维护着
     * 几个桶（HashEntry），多个线程可以同时访问不同分段锁上的桶，从而使其并发度更高（并发度就是 Segment 的个数）。
     * Segment 继承自 ReentrantLock。默认的并发级别为 16，也就是说默认创建 16 个 Segment。
     *
     * 2. size 操作
     * 每个 Segment 维护了一个 count 变量来统计该 Segment 中的键值对个数。
     * 在执行 size 操作时，需要遍历所有 Segment 然后把 count 累计起来。
     * ConcurrentHashMap 在执行 size 操作时先尝试不加锁，如果连续两次不加锁操作得到的结果一致，那么可以认为这个结果是正确的。
     * 尝试次数使用 RETRIES_BEFORE_LOCK 定义，该值为 2，retries 初始值为 -1，因此尝试次数为 3。
     * 如果尝试的次数超过 3 次，就需要对每个 Segment 加锁。
     *
     *  3. JDK 1.8 的改动
     * JDK 1.7 使用分段锁机制来实现并发更新操作，核心类为 Segment，它继承自重入锁 ReentrantLock，并发度与 Segment 数量相等。
     * JDK 1.8 使用了 CAS 操作来支持更高的并发度，在 CAS 操作失败时使用内置锁 synchronized。
     * 并且 JDK 1.8 的实现也在链表过长时会转换为红黑树。
     *
     */
    public void concurrentHashMap(){

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
    }

    /**
     * LinkedHashMap
     *
     * 1. 存储结构
     * 继承自 HashMap，因此具有和 HashMap 一样的快速查找特性。
     * 内部维护了一个双向链表，用来维护插入顺序或者 LRU 顺序。
     *
     * 2. LRU 缓存
     * 以下是使用 LinkedHashMap 实现的一个 LRU 缓存：
     * 设定最大缓存空间 MAX_ENTRIES 为 3；
     * 使用 LinkedHashMap 的构造函数将 accessOrder 设置为 true，开启 LRU 顺序；
     * 覆盖 removeEldestEntry() 方法实现，在节点多于 MAX_ENTRIES 就会将最近最久未使用的数据移除。
     */
    public void linkedHashMap(){

        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
    }

    /**
     * WeakHashMap
     *
     * 1. 存储结构
     * WeakHashMap 的 Entry 继承自 WeakReference，被 WeakReference 关联的对象在下一次垃圾回收时会被回收。
     * WeakHashMap 主要用来实现缓存，通过使用 WeakHashMap 来引用缓存对象，由 JVM 对这部分缓存进行回收。
     *
     * 2. ConcurrentCache
     * Tomcat 中的 ConcurrentCache 使用了 WeakHashMap 来实现缓存功能。
     * ConcurrentCache 采取的是分代缓存：
     * 经常使用的对象放入 eden 中，eden 使用 ConcurrentHashMap 实现，不用担心会被回收（伊甸园）；
     * 不常用的对象放入 longterm，longterm 使用 WeakHashMap 实现，这些老对象会被垃圾收集器回收。
     * 当调用 get() 方法时，会先从 eden 区获取，如果没有找到的话再到 longterm 获取，当从 longterm 获取到就把对象放入 eden 中，从而保证经常被访问的节点不容易被回收。
     * 当调用 put() 方法时，如果 eden 的大小超过了 size，那么就将 eden 中的所有对象都放入 longterm 中，利用虚拟机回收掉一部分不经常使用的对象。
     */
    public void weakHashMap(){

        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
    }

    /**
     * ConcurrentCache
     *
     * @param <K>
     * @param <V>
     */
    public final class ConcurrentCache<K, V> {

        private final int size;

        private final Map<K, V> eden;

        private final Map<K, V> longterm;

        public ConcurrentCache(int size) {
            this.size = size;
            this.eden = new ConcurrentHashMap<>(size);
            this.longterm = new WeakHashMap<>(size);
        }

        public V get(K k) {
            V v = this.eden.get(k);
            if (v == null) {
                v = this.longterm.get(k);
                if (v != null)
                    this.eden.put(k, v);
            }
            return v;
        }

        public void put(K k, V v) {
            if (this.eden.size() >= size) {
                this.longterm.putAll(this.eden);
                this.eden.clear();
            }
            this.eden.put(k, v);
        }
    }

    /**
     * Set
     *
     * TreeSet：基于红黑树实现，支持有序性操作，例如根据一个范围查找元素的操作。但是查找效率不如 HashSet，HashSet 查找的时间复杂度为 O(1)，TreeSet 则为 O(logN)。
     *
     * HashSet：基于哈希表实现，支持快速查找，但不支持有序性操作。并且失去了元素的插入顺序信息，也就是说使用 Iterator 遍历 HashSet 得到的结果是不确定的。
     *
     * LinkedHashSet：具有 HashSet 的查找效率，且内部使用双向链表维护元素的插入顺序。
     */
    public void set(){

        TreeSet<String> treeSet = new TreeSet<>();
        HashSet<String> hashSet = new HashSet<>();
        LinkedHashSet<String> hashSet1 = new LinkedHashSet<>();
    }
}
