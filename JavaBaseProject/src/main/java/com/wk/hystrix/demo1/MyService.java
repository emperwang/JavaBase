package com.wk.hystrix.demo1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyService {

    public void doSomeThing(String thing){
        log.info("doSoneThing -> "+thing + " ->begin");
        String threadName = Thread.currentThread().getName();
        log.info(threadName +" :doSomeThing -> " + thing + " ->doing");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("doSomeThing -> end");
    }
}
