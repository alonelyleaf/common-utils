package com.alonelyleaf.concurrent.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author bijl
 * @date 2019/10/10
 */
public class RunnableGroup {

    private final ExecutorService defaultExecutor;
    private final Collection<KeyValue<ExecutorService, Runnable>> tasks = new ArrayList<>();

    public RunnableGroup(ExecutorService defaultExecutor) {
        this.defaultExecutor = defaultExecutor;
    }

    public RunnableGroup() {
        this(null);
    }

    public synchronized void register(ExecutorService executorService, Runnable task) {
        tasks.add(new KeyValue<>(executorService, task));
    }

    public void register(Runnable task) {
        if (defaultExecutor == null) {
            throw new IllegalStateException("No default executor specified");
        }
        register(defaultExecutor, task);
    }

    public synchronized FinishLatch execute() {

        final CountDownLatch finishLatch = new CountDownLatch(tasks.size());

        for (final KeyValue<ExecutorService, Runnable> task : tasks) {
            task.getKey().execute(() -> {
                try {
                    task.getValue().run();
                } finally {
                    finishLatch.countDown();
                }
            });
        }
        return new FinishLatch(finishLatch);
    }

    public static class FinishLatch {

        private final CountDownLatch finishLatch;

        private FinishLatch(CountDownLatch finishLatch) {
            this.finishLatch = finishLatch;
        }

        public boolean await(long waitTime, TimeUnit waitTimeUnit)
                throws InterruptedException {
            return finishLatch.await(waitTime, waitTimeUnit);
        }

        public void await() throws InterruptedException {
            finishLatch.await();
        }
    }

    public class KeyValue<K, V> {

        private K key;

        private V value;

        public KeyValue(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public KeyValue setKey(K key) {
            this.key = key;
            return this;
        }

        public V getValue() {
            return value;
        }

        public KeyValue setValue(V value) {
            this.value = value;
            return this;
        }
    }
}
