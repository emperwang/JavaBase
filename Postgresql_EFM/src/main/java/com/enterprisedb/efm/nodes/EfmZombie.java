package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EfmZombie extends EfmAgentDelegate {
    public EfmZombie(final EfmAgent owner, boolean checkAutoResume) {
        super(EfmNode.NodeType.IDLE, owner);
        final long period = Environment.INSTANCE.getAutoResumePeriod();
        final Logger logger = LogManager.getEfmLogger();
        if (checkAutoResume)
            if (period > 0L) {
                Thread resumeThread = new Thread(new Runnable() {
                    public void run() {
                        Notifications.AUTO_RESUME_STARTED.addSubjectParams(new String[] { owner.env.getClusterName() }).addBodyParams(new String[] { String.valueOf(period) }).send();
                        //Notifications.AUTO_RESUME_STARTED.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { String.valueOf(this.val$period) }).send();
                        logger.log(Level.INFO, "Starting check to see if agent can resume. Will check every {0} seconds.", Long.valueOf(period));
                        while (true) {
                            if (EfmZombie.this.replaced) {
                                logger.log(Level.INFO, "Node no longer in IDLE state. Resume check thread will now exit.");
                                return;
                            }
                            try {
                                TimeUnit.SECONDS.sleep(period);
                            } catch (InterruptedException e) {
                                logger.log(Level.WARNING, "Agent interrupted while checking to see if database is running again.");
                                return;
                            }
                            try {
                                owner.resumeMonitoring();
                                break;
                            } catch (Exception e) {
                                logger.log(Level.INFO, "Could not resume monitoring: {0}", e.toString());
                            }
                        }
                    }
                });
                resumeThread.setName("resume_check");
                resumeThread.setDaemon(true);
                resumeThread.start();
            } else {
                logger.log(Level.INFO, "The auto resume period set to {0}, so agent will stay IDLE until stopped or resumed manually.", Long.valueOf(period));
            }
    }

    public Notifications getNowWatchingNotification() {
        throw new IllegalStateException("Idle agent cannot be used to monitor database.");
    }

    public boolean setupAfterJoiningCluster() {
        return true;
    }

    public void cleanupAfterDbFailure() {}

    public void checkRecoveryState() {
        LOGGER.finer("IDLE node ignoring timer to check db recovery state.");
    }
}
