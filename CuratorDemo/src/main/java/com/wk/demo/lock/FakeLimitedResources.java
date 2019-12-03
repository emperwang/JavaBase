package com.wk.demo.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
@Slf4j
public class FakeLimitedResources {
    private final AtomicBoolean inUse = new AtomicBoolean(false);
    private int count = 0;
    public void use(){
        if(!inUse.compareAndSet(false,true)){
            throw new IllegalStateException("needs to be use by one client at a time");
        }
        try {
            log.info("************************************fakeLimiteResources in use*****************************");
            log.info("value : {}",inUse.toString());
            log.info("************************************fakeLimiteResources in use*****************************");
            Thread.sleep((long) (3*Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            inUse.set(false);
            count++;
            log.info("************************************fakeLimiteResources finally **********count = {}",count);
        }
    }
}
