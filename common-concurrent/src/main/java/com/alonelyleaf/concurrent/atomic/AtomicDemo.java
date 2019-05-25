package com.alonelyleaf.concurrent.atomic;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bijl
 * @date 2019/5/25
 */
public class AtomicDemo {

    private AtomicInteger  atomicInteger = new AtomicInteger();

    private AtomicBoolean atomicBoolean = new AtomicBoolean();

    private AtomicLong atomicLong = new AtomicLong();



    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public AtomicDemo setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
        return this;
    }

    public AtomicBoolean getAtomicBoolean() {
        return atomicBoolean;
    }

    public AtomicDemo setAtomicBoolean(AtomicBoolean atomicBoolean) {
        this.atomicBoolean = atomicBoolean;
        return this;
    }

    public AtomicLong getAtomicLong() {
        return atomicLong;
    }

    public AtomicDemo setAtomicLong(AtomicLong atomicLong) {
        this.atomicLong = atomicLong;
        return this;
    }
}
