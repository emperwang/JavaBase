package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.exec.WorkQueue;
import com.enterprisedb.efm.nodes.EfmNode;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClusterStateVerifier {
    public static final Long WAIT_SECONDS = Long.valueOf(100L);

    private static final Logger LOGGER = LogManager.getEfmLogger();

    private final EfmNode node;

    private final transient Object timerLock = new Object();

    private transient TimerThread timerThread;

    public ClusterStateVerifier(EfmNode node) {
        this.node = node;
    }

    public void scheduleVerification() {
        synchronized (this.timerLock) {
            stopTimerThread();
            LOGGER.log(Level.FINE, "starting state verifier thread");
            this.timerThread = new TimerThread(this.node);
            this.timerThread.start();
        }
    }

    public void shutdown() {
        stopTimerThread();
    }

    private void stopTimerThread() {
        synchronized (this.timerLock) {
            if (this.timerThread != null) {
                LOGGER.log(Level.FINE, "stopping state verifier thread");
                this.timerThread.interrupt();
            }
        }
    }

    private static final class TimerThread extends Thread {
        private static final Logger LOGGER = LogManager.getEfmLogger();

        private final EfmNode node;

        private TimerThread(EfmNode node) {
            this.node = node;
            setName("StateVerifierThread");
            setDaemon(true);
        }

        public void run() {
            LOGGER.log(Level.INFO, "Cluster state verifier thread started.");
            try {
                TimeUnit.SECONDS.sleep(ClusterStateVerifier.WAIT_SECONDS.longValue());
                LOGGER.finest("***** timer thread submitting node.verifyClusterState");
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        ClusterStateVerifier.TimerThread.LOGGER.finest("***** timer thread calling node.verifyClusterState");
                        ClusterStateVerifier.TimerThread.this.node.verifyClusterState();
                        ClusterStateVerifier.TimerThread.LOGGER.finest("***** node.verifyClusterState done");
                    }
                });
            } catch (InterruptedException e) {
                LOGGER.log(Level.FINE, "verifier thread interrupted");
            }
            LOGGER.log(Level.INFO, "verifier thread exiting");
        }
    }
}
