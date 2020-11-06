package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.nodes.EfmNode;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ClusterSizeUpdater {
    INSTANCE;

    private transient TimerThread timerThread;

    private final transient Object timerLock;

    private static final Logger LOGGER;

    ClusterSizeUpdater() {
        this.timerLock = new Object();
    }

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    public static void startTimer(EfmNode node) {
        INSTANCE.doStartTimer(node);
    }

    public static void stopTimer() {
        INSTANCE.stopTimerThread();
    }

    public static void shutdown() {
        stopTimer();
    }

    private void doStartTimer(EfmNode node) {
        synchronized (this.timerLock) {
            stopTimerThread();
            LOGGER.log(Level.FINE, "starting timer for node " + node);
            this.timerThread = new TimerThread(node);
            this.timerThread.start();
        }
    }

    private void stopTimerThread() {
        synchronized (this.timerLock) {
            if (this.timerThread != null) {
                LOGGER.log(Level.FINE, "Stopping updater thread.");
                this.timerThread.interrupt();
            }
        }
    }

    private static class TimerThread extends Thread {
        private static final Logger LOGGER = LogManager.getEfmLogger();

        private final EfmNode node;

        private final int seconds;

        public TimerThread(EfmNode node) {
            this.node = node;
            setName("ClusterSizeUpdaterThread");
            setDaemon(true);
            this.seconds = Environment.getEnvironment().getUpdaterSeconds();
        }

        public void run() {
            LOGGER.log(Level.INFO, "updater thread started");
            try {
                TimeUnit.SECONDS.sleep(this.seconds);
                LOGGER.finest("*** cluster updater calling node.updateStableSize");
                this.node.updateStableSize();
                LOGGER.finest("*** cluster updater: node.updateStableSize exited");
            } catch (InterruptedException e) {
                LOGGER.log(Level.INFO, "updater thread interrupted");
            }
            LOGGER.log(Level.FINE, "updater thread exiting");
        }
    }
}
