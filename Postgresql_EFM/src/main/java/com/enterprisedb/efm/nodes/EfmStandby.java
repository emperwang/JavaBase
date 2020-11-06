package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.admin.EfmStatus;
import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import com.enterprisedb.efm.exec.WorkQueue;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.DBUtils;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.VipMonitor;
import java.sql.SQLException;
import java.util.logging.Level;
import org.jgroups.Channel;

public final class EfmStandby extends EfmAgentDelegate {
    public EfmStandby(EfmAgent owner) {
        super(EfmNode.NodeType.STANDBY, owner);
    }

    public Notifications getNowWatchingNotification() {
        return Notifications.STANDBY_STARTED;
    }

    public boolean setupAfterJoiningCluster() {
        return true;
    }

    public void cleanupAfterDbFailure() {}

    public void checkRecoveryState() {
        try {
            if (!this.owner.dbInRecovery(Level.FINER)) {
                LOGGER.log(Level.SEVERE, "Standby agent detected that local database is not in recovery.");
                Notifications.DATABASE_SHOULD_BE_IN_RECOVERY.addSubjectParams(new String[] { this.env.getBindingAddress() }).send();
                this.owner.stopMonitoringForUnknown();
            }
        } catch (SQLException e) {
            LOGGER.warning("Could not perform scheduled database recovery check: " + e.toString());
        }
    }

    void promoteNode(final String failedMaster) {
        WorkQueue.executeTask(new Runnable() {
            public void run() {
                EfmStandby.this.doPromoteNode(failedMaster);
            }
        });
    }

    public static String readRecoveryConf() {
        Environment env = Environment.INSTANCE;
        String command = env.getUserSudoCommand() + " " + env.getDBFunctionsScript() + " " + SudoFunctions.READ_RECOVERY_CONF + " " + env.getClusterName();
        ProcessResult result = ExecUtil.performExec(false, new String[] { command });
        if (result.getExitValue() == 0)
            return result.getStdOut();
        LOGGER.log(Level.SEVERE, "Could not read recovery.conf file: {0}", result.getErrorOut());
        return null;
    }

    private void doPromoteNode(String failedMaster) {
        LOGGER.fine("Entering doPromoteNode()");
        if (this.replaced) {
            LOGGER.warning("This delegate has been replaced. Returning.");
            return;
        }
        if (!ClusterUtils.pingHost(this.env.getPingServer())) {
            Notifications.PROMOTE_BUT_ORPHANED.addBodyParams(new String[] { this.env.getPingServer() }).send();
            return;
        }
        if (masterFoundInCluster()) {
            Notifications.PROMOTE_BUT_MASTER_REJOIN.send();
            DBUtils.resumeReplay((Channel)this.owner.channel, this.owner.clusterState.getStandbys(), this.owner.clusterState);
            return;
        }
        this.owner.broadcastLastPromoted(this.env.getBindingAddress());
        this.owner.setAgentDelegate(new EfmPromoting(this.owner, failedMaster));
        String fencingScript = this.env.getFencingScript();
        if (fencingScript != null && !executeFencingScript(fencingScript, failedMaster)) {
            this.owner.setAgentDelegate(new EfmStandby(this.owner));
            return;
        }
        if (writeTriggerFile()) {
            try {
                this.owner.resumeReplay();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Could not resume replay on node being promoted. Manual action may be required. " + e.toString());
                Notifications.RESUME_FAILED_PROMOTED_NODE.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
            }
            VipMonitor.getInstance().stopMonitoring();
            if (!assignVip())
                LOGGER.warning("Assign VIP failed, but FO complete.");
            Notifications.PROMOTION_STARTED.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getClusterName() }).send();
        } else {
            this.owner.setAgentDelegate(new EfmStandby(this.owner));
        }
    }

    private boolean masterFoundInCluster() {
        try {
            LOGGER.log(Level.INFO, "Checking to see if master has rejoined before promoting this standby.");
            EfmStatus hs = ClusterUtils.sendStatusRequestToNodes(this.owner.dispatcher, this.owner.clusterState);
            String masterType = EfmNode.NodeType.MASTER.getName();
            for (NodeStatus ns : hs.getNodeStatusSet()) {
                if (ns.getIpAddr().equals(this.env.getBindingAddress())) {
                    LOGGER.log(Level.INFO, "Ignoring own node status.");
                    continue;
                }
                if (masterType.equals(ns.getType())) {
                    LOGGER.log(Level.WARNING, "Found rejoined master at {0}. Will not promote this standby.", ns.getIpAddr());
                    return true;
                }
                LOGGER.log(Level.INFO, "Found node of type {0}", ns.getType());
            }
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not perform check for rejoined master in cluster. Cluster state unknown. Unexpected error: " + e);
            return false;
        }
    }

    boolean writeTriggerFile() {
        LOGGER.info("Writing trigger file as specified in recovery.conf.");
        ProcessResult result = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.WRITE_TRIGGER_FILE + " " + this.env.getClusterName() });
        StringBuilder sb = new StringBuilder();
        result.addNiceOutput(sb);
        LOGGER.info(sb.toString());
        if (result.getExitValue() != 0) {
            Notifications.TRIGGER_FAILED.addBodyParams(new String[] { sb.toString() }).send();
            LOGGER.severe("Error writing trigger file. Could not promote standby.");
            return false;
        }
        return true;
    }

    boolean executeFencingScript(String fencingScript, String failedMaster) {
        if (failedMaster == null || failedMaster.isEmpty())
            failedMaster = "";
        String[] scriptAndParams = fencingScript.split("\\s+");
        if (scriptAndParams.length > 1)
            for (int i = 1; i < scriptAndParams.length; i++) {
                scriptAndParams[i] = scriptAndParams[i].replace("%p", this.env.getBindingAddress());
                scriptAndParams[i] = scriptAndParams[i].replace("%f", failedMaster);
            }
        ProcessResult result = ExecUtil.performExec(scriptAndParams);
        int exitVal = result.getExitValue();
        if (exitVal != 0) {
            Notifications.FENCING_SCRIPT_ERROR.addBodyParams(new String[] { fencingScript, Integer.toString(exitVal), result.toString() }).send();
        } else {
            Notifications.FENCING_SCRIPT.addBodyParams(new String[] { fencingScript, result.toString() }).send();
        }
        return (exitVal == 0);
    }
}
