package com.wk.demo.lock;

import java.util.concurrent.atomic.AtomicBoolean;

public class FakeLimitedResources {
    private final AtomicBoolean inUse = new AtomicBoolean(false);
    public void use(){
        if(!inUse.compareAndSet(false,true)){
            throw new IllegalStateException("needs to be use by one client at a time");
        }

        try {
            System.out.println("fakeLimiteResources in use");
            Thread.sleep((long) (3*Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            inUse.set(false);
        }
    }
}
