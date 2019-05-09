package com.wk.scheduling;

import com.wk.thread.ThreadUtil;
import com.wk.thread.pool.ThreadPoolUtil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  周期执行任务
 */
public class PeriodRunner {

    private ScheduledExecutorService executorService;
    /**
     * 第一次延迟多久再执行
     */
    private long initialDelay = 0;
    /**
     * 周期执行的间隔
     */
    private long period = 5;
    public PeriodRunner(long period){
        this(0,period);
    }

    public PeriodRunner(long initialDelay, long period) {
        this(initialDelay,period, ThreadUtil.currentGroup());
    }

    public PeriodRunner(long initialDelay, long period, ThreadGroup group) {
        this(initialDelay,period,group,"PeriodRunner");
    }

    /**
     *
     * @param initialDelay  第一次执行前需延迟的时间
     * @param period  每次任务的间隔时间
     * @param group  组名
     * @param prefix 线程名前缀
     */
    public PeriodRunner(long initialDelay, long period, ThreadGroup group, String prefix) {
        setInitialDelay(initialDelay);
        setPeriod(period);
        setExecutorService(ThreadPoolUtil.scheduledPool(ThreadUtil.createFactory(group,prefix)));
    }

    /**
     *  固定的时间去执行任务
     * @param task
     */
    public void schedule(Runnable task){
        executorService.scheduleAtFixedRate(task,initialDelay,period, TimeUnit.SECONDS);
    }

    /**
     *  指定任务的开始时间
     * @param task
     * @param time
     */
    public void schedule(Runnable task,long time){
        this.setInitialDelay(time - System.currentTimeMillis());
        schedule(task);
    }

    /**
     * 通知任务
     */
    public void stop(){
        executorService.shutdown();
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ScheduledExecutorService executorService) {
        this.executorService = executorService;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
        this.initialDelay = initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
