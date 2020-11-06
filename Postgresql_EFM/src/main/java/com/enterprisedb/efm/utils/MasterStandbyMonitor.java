package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.nodes.EfmAgent;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MasterStandbyMonitor {
    public static final Long DELAY_MINUTES = Long.valueOf(2L);

    private final EfmAgent agent;

    private final ScheduledThreadPoolExecutor executor;

    public MasterStandbyMonitor(EfmAgent agent) {
        this.agent = agent;
        this.executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread retVal = new Thread(r, "master_standby_checker");
                retVal.setDaemon(true);
                return retVal;
            }
        });
    }

    public void start() {
        Runnable toRun = new Runnable() {
            public void run() {
                MasterStandbyMonitor.this.agent.checkRecoveryState();
            }
        };
        this.executor.scheduleWithFixedDelay(toRun, DELAY_MINUTES.longValue(), DELAY_MINUTES.longValue(), TimeUnit.MINUTES);
    }

    public void stop() {
        this.executor.shutdownNow();
    }
}
