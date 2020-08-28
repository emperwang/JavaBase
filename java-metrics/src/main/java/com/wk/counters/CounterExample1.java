package com.wk.counters;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * 作用: counter是gague的一个特例,维护一个计数器,可以通过inc()和dec()方法对计数器做修改.使用
 * 步骤与gauge基本类似,在MetricRegistry中提供了静态方法可以直接实例化一个counter.可以用来度量生产者和消费者之间的关系
 */
public class CounterExample1 {

    private static final MetricRegistry registry = new MetricRegistry();

    private static final Counter counter=registry.counter(MetricRegistry.name(CounterExample1.class,""));

    private static Queue<String> queue = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        reporter.start(3,TimeUnit.SECONDS);

        new Thread(new Runnable() {
            @Override
            public void run() {
                production("ss");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                consume();
            }
        }).start();
        Thread.currentThread().join();
    }

    public static void production(String s){
        for(int i=0; i<100;i++){
            counter.inc();
            queue.offer(s);
        }
    }

    public static void consume(){
        while (queue.size() != 0){
            queue.poll();
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.dec();
        }
    }
}


















