package com.alonelyleaf.netty.boot;


import java.util.function.Supplier;

public class BootChain {

    private final BootJob boot = new BootJob() {

        @Override
        protected void start() {
            startNext();
        }

        @Override
        protected void stop() {
            stopNext();
        }
    };

    private BootJob last = boot;

    public void start() {
        boot.start();
    }

    public void stop() {
        boot.stop();
    }

    public static BootChain chain() {
        return new BootChain();
    }

    public BootChain boot() {
        return this;
    }

    public void end() {
        setNext(new BootJob() {
            @Override
            protected void start() {
            }

            @Override
            protected void stop() {
            }

            @Override
            protected String getName() {
                return "LastBoot";
            }
        });
    }

    public BootChain setNext(BootJob bootJob) {
        this.last = last.setNext(bootJob);
        return this;
    }

    public BootChain setNext(Supplier<BootJob> next, boolean enabled) {
        if (enabled) {
            return setNext(next.get());
        }
        return this;
    }
}
