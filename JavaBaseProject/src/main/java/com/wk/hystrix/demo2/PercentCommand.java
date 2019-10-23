package com.wk.hystrix.demo2;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesCommandDefault;

public class PercentCommand extends HystrixCommand<String> {

    private String group;
    private PercentService service;
    private int seed;

    public PercentCommand(String group,int seed){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group))
            .andCommandPropertiesDefaults(HystrixPropertiesCommandDefault.Setter()
            .withCircuitBreakerErrorThresholdPercentage(50)
                    .withExecutionTimeoutInMilliseconds(2000)
                    .withCircuitBreakerRequestVolumeThreshold(5)
                    .withCircuitBreakerSleepWindowInMilliseconds(30000) // 30秒放部分流量过去
                    .withMetricsRollingPercentileBucketSize(10)
                    .withMetricsRollingPercentileWindowBuckets(5000)) );
        this.group = group;
        this.seed = seed;
    }

    @Override
    protected String run() throws Exception {
        return service.ex(seed);
    }

    public void setService(PercentService service){
        this.service = service;
    }

    @Override
    protected String getFallback() {
        return seed +"  failed";
    }
}
