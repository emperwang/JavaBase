package com.wk.timers;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.concurrent.TimeUnit;

/**
 *  作用: 统计请求的速率和处理时间
 *  例如: 某接口在一定时间内的请求总数,平均处理时间
 */
public class TimeExample1 {

    private static final MetricRegistry registry = new MetricRegistry();
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
    // 实例化 Timer
    private static final Timer timer = registry.timer("request");
    public static void main(String[] args) {
        reporter.start(3, TimeUnit.SECONDS);

        while (true){
            handleRequest();
        }
    }

    private static void handleRequest(){
        Timer.Context time = timer.time();
        try{
            TimeUnit.MILLISECONDS.sleep(100);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            time.stop();
            System.out.println("time closed.");
        }
    }
}
