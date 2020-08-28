package com.wk.meters;


import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Meter是一种只能自增的计数器,通次用来度量一系列事件发生的概率.
 * 它提供了平均速率,以及指数平滑平均速率,以及采样后的1分钟,5分钟, 15分钟这样
 */
public class MetricsExample1 {
    // 创建注册表
    private final static MetricRegistry registry = new MetricRegistry();
    // 创建tps 测量表
    private final static Meter requestMeter = registry.meter("tps");
    // 创建异常测量表
    private final static Meter errMeter = registry.meter("err_request");

    public static void main(String[] args) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.MINUTES)
                .convertDurationsTo(TimeUnit.MINUTES)
                .build();
        // 每10s 将数据打印在控制台上
        reporter.start(10, TimeUnit.SECONDS);
        for(;;){    // 模拟调用请求
            getAsk();
            randomSleep();
        }
    }

    public static void getAsk(){
        try {
            requestMeter.mark();
            randomSleep();
            int x = 10 / ThreadLocalRandom.current().nextInt(6);
        } catch (Exception e) {
            //e.printStackTrace();
            errMeter.mark();
        }
    }

    public static void randomSleep(){
        try{
            TimeUnit.SECONDS.toSeconds(ThreadLocalRandom.current().nextInt(5));
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
