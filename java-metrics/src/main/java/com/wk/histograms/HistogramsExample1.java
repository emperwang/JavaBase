package com.wk.histograms;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 主要使用统计数据的分布情况,最大值,最小值,平均值,中位数,百分比
 * 例如: 需要统计某个页面的请求,接口方法请求的响应时间
 */
public class HistogramsExample1 {

    private static final MetricRegistry registry = new MetricRegistry();

    // 实例化一个 histograms
    private static final Histogram histogram = registry.histogram(MetricRegistry.name(HistogramsExample1.class, ""));
    public static void main(String[] args) throws InterruptedException {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(3, TimeUnit.SECONDS);
        Random random = new Random();
        while (true){
            processHandle(random.nextDouble());
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static void processHandle(Double b){
        // 在应用中,需要统计的位置调用update 更新值
        histogram.update((int)(b*100));
    }
}
