package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public enum VipMonitor {
    INSTANCE;

    private final long periodMs;

    private final long delayMs = 60000L;

    private final transient Object lock;

    private boolean monitoring;

    private final Environment env;

    private transient Timer vipMonitor;

    private static final Logger LOGGER;

    VipMonitor() {
        this.env = Environment.getEnvironment();
        this.monitoring = false;
        this.lock = new Object();
        //this.delayMs = 60000L;
        this.periodMs = this.env.getJGroupsTotalTimeout() * 1000L;
    }

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    public static VipMonitor getInstance() {
        return INSTANCE;
    }

    public void startMonitoring(boolean master) {
        if (!this.env.isVipEnabled()) {
            LOGGER.info("Not starting VIP monitor since a VIP is not being used.");
            return;
        }
        synchronized (this.lock) {
            stopMonitoring();
            if (master) {
                LOGGER.info("Starting master vip monitor (to ensure this node is assigned the VIP)");
            } else {
                LOGGER.info("Starting non-master vip monitor (to ensure this node isn't assigned the VIP)");
            }
            this.vipMonitor = new Timer("vip_monitor_" + this.env.getClusterName(), true);
            this.vipMonitor.schedule(new VipTask(master, this), 60000L, this.periodMs);
            this.monitoring = true;
        }
    }

    public boolean isMonitoring() {
        synchronized (this.lock) {
            return this.monitoring;
        }
    }

    public void stopMonitoring() {
        synchronized (this.lock) {
            if (this.monitoring) {
                this.monitoring = false;
                LOGGER.info("Stopping vip monitor");
                if (this.vipMonitor != null)
                    this.vipMonitor.cancel();
            }
        }
    }

    public Object getLock() {
        return this.lock;
    }

    static class VipTask extends TimerTask {
        private final boolean master;

        private final VipMonitor monitor;

        public VipTask(boolean master, VipMonitor monitor) {
            this.master = master;
            this.monitor = monitor;
        }

        public void run() {
            synchronized (this.monitor.getLock()) {
                if (!this.monitor.isMonitoring())
                    return;
                VipMonitor.LOGGER.finest("Checking on vip. master=" + this.master);
                if (!this.master && ClusterUtils.isBroadcastingVip()) {
                    VipMonitor.LOGGER.warning(" ***  non-master is broadcasting vip!");
                    Notifications.VIP_ASSIGNED_TO_NON_MASTER.send();
                    ClusterUtils.releaseVip(true, false);
                } else if (this.master && !ClusterUtils.isBroadcastingVip()) {
                    VipMonitor.LOGGER.warning(" ***  master is not broadcasting vip!");
                    Notifications.VIP_NOT_ASSIGNED_TO_MASTER.send();
                    ClusterUtils.acquireVip();
                }
            }
        }
    }
}
