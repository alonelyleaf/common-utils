package com.alonelyleaf.prometheus.exporter.util;

import io.prometheus.client.Collector;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指标工具
 *
 * @author bijl
 * @date 2021/6/7 下午3:34
 */
public class MeterUtil {

    private static final ConcurrentHashMap<String, Collector> COLLECTOR_MAP = new ConcurrentHashMap<>();

    public static Counter getCounter(@NotBlank String name, @NotNull String[] labelList, @NotNull String help) {

        Collector collector = COLLECTOR_MAP.get(name);
        if (collector == null) {
            synchronized (MeterUtil.class) {
                collector = COLLECTOR_MAP.get(name);
                if (collector == null) {
                    Counter counter = Counter.build()
                        .name(name)
                        .labelNames(labelList)
                        .help(help)
                        .register();

                    collector = counter;

                    COLLECTOR_MAP.put(name, counter);
                }
            }
        }

        if (collector instanceof Counter) {
            return (Counter) collector;
        }

        return null;
    }

    public static Histogram getHistogram(@NotBlank String name, @NotNull String[] labelList, @NotNull String help) {

        Collector collector = COLLECTOR_MAP.get(name);
        if (collector == null) {
            synchronized (MeterUtil.class) {
                collector = COLLECTOR_MAP.get(name);
                if (collector == null) {
                    Histogram histogram = Histogram.build()
                        .name(name)
                        .labelNames(labelList)
                        .help(help)
                        .register();

                    collector = histogram;

                    COLLECTOR_MAP.put(name, histogram);
                }
            }
        }

        if (collector instanceof Histogram) {
            return (Histogram) collector;
        }

        return null;
    }
}
