package com.wk.hystrix.demo1;

import com.netflix.hystrix.*;

public class MyCommand extends HystrixCommand<String> {

    private String group;
    private String thing;
    private MyService myService;
    public MyCommand(String group,String thing){
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group))
              .andCommandKey(HystrixCommandKey.Factory.asKey("mycommand"))
              .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("mycommandThreadPool"))
              .andCommandPropertiesDefaults(
                      HystrixCommandProperties.Setter()
                      .withCircuitBreakerRequestVolumeThreshold(5)
                      .withCircuitBreakerErrorThresholdPercentage(60)
                      //.withExecutionTimeoutInMilliseconds(2000)
                      .withExecutionTimeoutEnabled(false)
                      .withMetricsRollingStatisticalWindowInMilliseconds(1000)
              )
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)  // 设置线程池大小10
                )
        );
        this.group = group;
        this.thing = thing;
    }

    @Override
    protected String run() throws Exception {
        myService.doSomeThing("hystrix");
        return thing+" over";
    }

    @Override
    protected String getFallback() {
        return  thing + " Failure";
    }

    public void setMyService(MyService myService) {
        this.myService = myService;
    }
}
