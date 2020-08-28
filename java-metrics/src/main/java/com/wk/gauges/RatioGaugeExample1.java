package com.wk.gauges;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 度量事件成功率的计算. 例如: 度量缓存命中率,接口调用率等.
 */
public class RatioGaugeExample1 {
    private static final MetricRegistry registry = new MetricRegistry();
    private static Meter totalMeter = registry.meter("totalcount");
    private static Meter succMeter = registry.meter("succCount");

    public static void main(String[] args) {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        reporter.start(3, TimeUnit.SECONDS);

        registry.gauge("succ-ratio", ()->new RatioGauge() {
            @Override
            protected Ratio getRatio() {
                // 第一个参数:分子, 第二个参数: 分母
                return Ratio.of(succMeter.getCount(), totalMeter.getCount());
            }
        });
        for(;;){
            processHandle();
        }
    }

    public static void processHandle(){
        totalMeter.mark();
        try{
            int x = 10/ ThreadLocalRandom.current().nextInt(10);
            TimeUnit.MILLISECONDS.sleep(100);
            succMeter.mark();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(e.getCause());
        }
    }
}
