package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EfmPromoting extends EfmAgentDelegate {
    public EfmPromoting(final EfmAgent owner, final String failedMaster) {
        super(EfmNode.NodeType.PROMOTING, owner);
        Thread recoveryMonitorThread = new Thread(new Runnable() {
            public void run() {
                long monitorPeriod = EfmPromoting.this.env.getRecoveryCheckPeriod();
                Logger logger = LogManager.getEfmLogger();
                logger.log(Level.INFO, "Will monitor database until it is no longer in recovery. At that time, will switch to master node.");
                while (true) {
                    logger.log(Level.FINE, "recovery monitor thread starting loop. replaced: {0}", Boolean.valueOf(EfmPromoting.this.replaced));
                    if (EfmPromoting.this.replaced) {
                        logger.log(Level.INFO, "Node no longer in promoting state. The recovery check thread will now exit.");
                        return;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(monitorPeriod);
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, "Agent interrupted monitoring database to determine when it is no longer in recovery.");
                        return;
                    }
                    try {
                        boolean inRecovery = owner.dbInRecovery(Level.FINE);
                        if (logger.isLoggable(Level.FINE))
                            logger.log(Level.FINE, "In recovery result: {0}", Boolean.valueOf(inRecovery));
                        if (!inRecovery) {
                            //Notifications.FAILOVER_COMPLETE.addBodyParams(new String[] { this.this$0.env.getClusterName() }).addSubjectParams(new String[] { this.this$0.env.getClusterName() }).send();
                            Notifications.FAILOVER_COMPLETE.addBodyParams(new String[] { owner.env.getClusterName() }).
                                    addSubjectParams(new String[] { owner.env.getClusterName() }).send();
                            String script = EfmPromoting.this.env.getPostPromotionScript();
                            if (script != null) {
                                logger.log(Level.INFO, "Attempting to run post-promotion script {0}", script);
                                EfmPromoting.this.executePostPromoteScript(script, failedMaster);
                            }
                            owner.notifyNodesOfNewMaster();
                            owner.setAgentDelegate(new EfmMaster(owner));
                            return;
                        }
                    } catch (SQLException sqle) {
                        EfmAgentDelegate.LOGGER.log(Level.WARNING, "Could not determine if database is in recovery or not: {0}", sqle.toString());
                    }
                }
            }
        },"promoting_delegate");
        recoveryMonitorThread.setDaemon(true);
        recoveryMonitorThread.start();
    }

    public Notifications getNowWatchingNotification() {
        throw new IllegalStateException("Promoting agent cannot be used to monitor database.");
    }

    public boolean setupAfterJoiningCluster() {
        return false;
    }

    public void cleanupAfterDbFailure() {}

    public void checkRecoveryState() {
        LOGGER.finer("Ignoring call to check for proper recovery state because we are already checking during promotion.");
    }

    private void executePostPromoteScript(String script, String failedMaster) {
        if (failedMaster == null || failedMaster.isEmpty())
            failedMaster = "";
        String[] scriptAndParams = script.split("\\s+");
        if (scriptAndParams.length > 1)
            for (int i = 1; i < scriptAndParams.length; i++) {
                scriptAndParams[i] = scriptAndParams[i].replace("%p", this.env.getBindingAddress());
                scriptAndParams[i] = scriptAndParams[i].replace("%f", failedMaster);
            }
        ProcessResult result = ExecUtil.performExec(scriptAndParams);
        int exitVal = result.getExitValue();
        if (exitVal != 0) {
            Notifications.POST_PROMOTION_SCRIPT_FAILURE.addBodyParams(new String[] { script, Integer.toString(exitVal), result.toString() }).send();
        } else {
            Notifications.POST_PROMOTION_SCRIPT_RUN.addBodyParams(new String[] { script, result.toString() }).send();
        }
    }
}
