package com.wk.threadFactory;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicLong;

// com.google.common.util.concurrent.threadFactoryBuilder
public class ThreadFactoryBuilder {
    private String nameFormat = null;
    private Boolean daemon = null;
    private Integer priority = null;
    private UncaughtExceptionHandler uncaughtExceptionHandler = null;
    private ThreadFactory backingThreadFactory = null;

    public ThreadFactoryBuilder(){}

    public ThreadFactoryBuilder setNameFormat(String nameFormat){
        String unused = format(nameFormat,0);
        this.nameFormat = nameFormat;
        return this;
    }

    private static String format(String format, Object... args) {
        return String.format(Locale.ROOT,format, args);
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon){
        this.daemon = daemon;
        return this;
    }

    public ThreadFactoryBuilder setPriority(int priority){
        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY){

        }else{
            this.priority = priority;
        }
        return this;
    }

    public ThreadFactoryBuilder setUncaughtExceptionHandler(
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler){
        this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler);
        return this;
    }

    public ThreadFactoryBuilder setThreadFactory(ThreadFactory threadFactory){
        this.backingThreadFactory = checkNotNull(threadFactory);
        return this;
    }

    public ThreadFactory build(){
        return build(this);
    }

    private static ThreadFactory build(ThreadFactoryBuilder builder){
        final String nameFormat = builder.nameFormat;
        final Boolean daemon = builder.daemon;
        final Integer priority = builder.priority;
        final UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
        final ThreadFactory backingThreadFactory =
                (builder.backingThreadFactory != null ? builder.backingThreadFactory: Executors.defaultThreadFactory());
        final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;

        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = backingThreadFactory.newThread(r);
                if (nameFormat != null){
                    thread.setName(format(nameFormat,count.getAndIncrement()));
                }
                if (daemon != null){
                    thread.setDaemon(daemon);
                }
                if (priority != null){
                    thread.setPriority(priority);
                }
                if (uncaughtExceptionHandler != null){
                    thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
                }
                return thread;
            }
        };
    }

    private static <T> T checkNotNull(T reference){
        if (reference == null){
            throw new NullPointerException();
        }
        return reference;
    }
}
