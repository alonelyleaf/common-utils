package com.alonelyleaf.designpattern.createdmode.singleton;

/**
 * Effective Java 作者推荐使⽤枚举的⽅式解决单例模式，此种方式可能是平时最少用到的。
 * 这种⽅方式解决了了最主要的;线程安全、⾃由串行化、单一实例。
 *
 * 这种写法在功能上与共有域方法相近，但是它更简洁，无偿地提供了了串行化机制，绝对防止对此实例化，即使是在⾯对复杂的串行化或者反射攻击的时候。虽然
 * 这种方法还没有⼴泛采用，但是单元素的枚举类型已经成为实现Singleton的最佳⽅方法。但也要知道此种⽅式在存在继承场景下是不可用的。
 *
 * @author bijl
 * @date 2020/12/21 下午8:23
 */
public enum EnumSingleton {

    INSTANCE;

    public void test(){
        System.out.println("hi~");
    }
}
