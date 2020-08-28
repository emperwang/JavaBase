package com.wk.gauges;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Gauge 是一个最简单的计量,一般用来统计瞬时状态的数据信息
 *  例如: 某一时刻的集合中的大小
 */
public class GaugeExample1 {
    // 定义度量中心
    private static final MetricRegistry registry = new MetricRegistry();

    // 定义队列
    private static Queue<Integer> queue = new LinkedBlockingDeque<>();

    public static void main(String[] args) throws InterruptedException {
        // 将信息展示到控制台上
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(3, TimeUnit.SECONDS);
        Gauge<Integer> gauge = new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return queue.size();
            }
        };

        // 将定义的gague 注册到注册中心
        registry.register(MetricRegistry.name(GaugeExample1.class,"queue-size"), gauge);
        // 模拟queue队列中的数据
        for (int i =0 ; i < 1000; i++){
            queue.add(i);
            TimeUnit.MILLISECONDS.sleep(100);
        }
        Thread.currentThread().join();
    }
}
