package com.alonelyleaf.concurrent.unsafe;

import sun.misc.Unsafe;

/**
 * concurrent包下面用到了Unsafe的CAS功能、线程挂起和恢复等等功能。当然他不止这些功能，Unsafe还可以直接操纵内存。
 *  Unsafe虽然不属于concurrent包，但concurrent.locks包下面的核心类都使用到了sun.simc.Unsafe这个类
 *
 *  https://blog.csdn.net/qq_34448345/article/details/80087738
 *
 * 1. 类申明和构造器，单例且限制用户获取实例，一般需要用反射获取
 * 2. 内存操作，一系列native方法，可以直接分配、重新分配、释放指定的内存，直接修改和获取指定内存保存的值
 * 3. 对象操作，获取指定对象的属性的偏移量，可以根据对象和偏移量修改属性
 * 4. 创建对象，不同于反射和构造器的创建对象的方式
 * 5. 操作数组，两个方法结合可以定位数组中任意元素的地址，另一方面java规定数组长度最大为Integer.MaxValue，使用Unsafe分配内存可以实现超大数组
 * 6. AS操作，原子性的，cpu可以直接读取相关指令，后续concurent包下并发类封装的所有CAS操作都基于这三个方法。
 *   赋新值时，期望值与旧值相对比可以知道操作期间是否被别人修改。
 * 7. CAS操作，封装基础方法给出的getAndAddXXX()方法，乐观方式的失败重试机制。例如AtomicInteger.getAndIncrement()就是调用这类方法实现自增。
 * 8. 线程状态操作，注意park()挂起、unpark()恢复，是一种基于许可的方式。park()会用掉许可，unpark()可以设置许可，调用顺序无要求，
 *    但许可只有一个不能叠加。也就是说在之前调用了unpark()设置许可的情况下，后调用park()不会造成线程挂起。
 * 9. 线程屏障，java的内存模型是线程+线程工作内存+主内存，在三者之间一共有4对原子操作用于变量值在三者之间的传递，具体的想
 *    要实现变量可见就要保证变量每次获取值都不从工作内存获取而是从主内存获取，这一点是volite的原理。Unsafe提供的线程屏障
 *    实现了read(从主内存读入工作内存)、write(从工作内存保存到主内存)的屏障
 *
 *
 * @author bijl
 * @date 2019/5/24
 */
public class MyUnsafe {

    private Unsafe unsafe = Unsafe.getUnsafe();

    public void operation(){

    }
}
