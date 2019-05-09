package com.wk.thread;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(0);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(0);
    private final String namePrefix;

    public SimpleThreadFactory(ThreadGroup group, String namePrefix) {
        this.group = group;
        this.namePrefix =  getPrefix(namePrefix) +"-"+poolNumber.incrementAndGet()+"-";
    }

    private String getPrefix(String prefix){
        if(StringUtils.isNotBlank(prefix)){
            return prefix;
        }
        return this.getClass().getSimpleName();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(group, r, namePrefix + threadNumber.incrementAndGet(), 0);
        if(thread.isDaemon()){
            thread.setDaemon(false);
        }
        return thread;
    }
}
