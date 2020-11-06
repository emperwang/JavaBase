package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.VipMonitor;
import java.sql.SQLException;
import java.util.logging.Level;

public final class EfmMaster extends EfmAgentDelegate {
    public EfmMaster(EfmAgent owner) {
        super(EfmNode.NodeType.MASTER, owner);
    }

    public Notifications getNowWatchingNotification() {
        return Notifications.MASTER_STARTED;
    }

    public boolean setupAfterJoiningCluster() {
        if (this.env.isVipEnabled()) {
            VipMonitor.getInstance().stopMonitoring();
            if (!assignVip()) {
                System.err.println("Could not assign VIP - please check logs for more detail. Exiting.");
                return false;
            }
            VipMonitor.getInstance().startMonitoring(true);
        }
        return true;
    }

    public void cleanupAfterDbFailure() {
        ClusterUtils.releaseVip((this.owner.state == EfmNode.State.PROMOTE), true);
        if (this.env.autoFailoverOn() || this.owner.state == EfmNode.State.PROMOTE) {
            writeRecoveryConf();
        } else {
            LOGGER.info("Not writing recovery.conf file to master.");
        }
    }

    public void checkRecoveryState() {
        try {
            if (this.owner.dbInRecovery(Level.FINER)) {
                Notifications.DATABASE_SHOULD_NOT_BE_IN_RECOVERY.addSubjectParams(new String[] { this.env.getBindingAddress() }).send();
                this.owner.stopMonitoringForUnknown();
            }
        } catch (SQLException e) {
            LOGGER.warning("Could not perform scheduled database recovery check: " + e.toString());
        }
    }

    boolean writeRecoveryConf() {
        ProcessResult result;
        String text = this.owner.getRecoveryConfText();
        if (text == null || text.isEmpty()) {
            LOGGER.log(Level.INFO, "Writing recovery.conf file to: {0}", this.env.getRecoveryConfLocation() + "/recovery.conf");
            result = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.WRITE_RECOVERY_CONF + " " + this.env.getClusterName() });
        } else {
            text = text.replace("\n", "\\n");
            LOGGER.log(Level.INFO, "Writing switchover recovery.conf file to: {0}", this.env.getRecoveryConfLocation() + "/recovery.conf");
            LOGGER.log(Level.FINE, "Text: " + text);
            result = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.WRITE_CUSTOM_RECOVERY_CONF + " " + this.env.getClusterName() + " " + text });
        }
        StringBuilder sb = new StringBuilder();
        result.addNiceOutput(sb);
        LOGGER.info(sb.toString());
        if (result.getExitValue() != 0) {
            Notifications.RECOVERY_CONF_ERROR.addSubjectParams(new String[] { this.env.getBindingAddress(), this.env.getClusterName() }).addBodyParams(new String[] { this.env.getBindingAddress(), sb.toString() }).send();
            return false;
        }
        return true;
    }
}
