package com.alonelyleaf.netty.boot;

import java.util.function.Supplier;

public abstract class BootJob {

    protected BootJob next;

    /**
     * 启动
     */
    protected abstract void start();

    /**
     * 停止
     */
    protected abstract void stop();

    /**
     * 启动下一只任务
     */
    public void startNext() {

        if (next != null) {
            next.start();
        }
    }

    /**
     * 停止下一只
     */
    public void stopNext() {

        if (next != null) {
            next.stop();
        }
    }

    public BootJob setNext(BootJob next) {

        this.next = next;
        return next;
    }

    public BootJob setNext(Supplier<BootJob> next, boolean enabled) {

        if (enabled) {
            return setNext(next.get());
        }

        return this;
    }

    protected String getNextName() {

        return next.getName();
    }

    protected String getName() {

        return this.getClass().getSimpleName();
    }
}
