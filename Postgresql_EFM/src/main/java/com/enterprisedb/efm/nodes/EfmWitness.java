package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exec.AgentLauncher;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.ShutdownNotificationService;
import com.enterprisedb.efm.utils.Utils;
import com.enterprisedb.efm.utils.VipMonitor;
import java.util.logging.Level;
import org.jgroups.Address;

public final class EfmWitness extends EfmNode {
    public EfmWitness() {
        ShutdownNotificationService.getInstance().setShutdownNotification(Notifications.WITNESS_EXITED);
        this.nodeTypeTime = System.currentTimeMillis();
    }

    public Object doHandle(Object payload) throws Exception {
        if ("node_status".equals(payload)) {
            long currentTime = System.currentTimeMillis();
            NodeStatus nodeStatus = new NodeStatus(EfmNode.NodeType.WITNESS.getName(), this.env.getBindingAddress(), true, false, currentTime - this.nodeTypeTime, getLastPromoted(), currentTime - getTimeLastPromoted(), this.channel.getAddress());
            LOGGER.log(Level.INFO, "responding to db status request with status: {0}", nodeStatus.marshal());
            return nodeStatus;
        }
        LOGGER.log(Level.INFO, "Witness received {0}", payload);
        return null;
    }

    boolean determineStartupAgentType() {
        throw new AssertionError("Cannot be called on witness node.");
    }

    EfmNode.NodeType getNodeType() {
        return EfmNode.NodeType.WITNESS;
    }

    boolean checkVipUse() {
        return true;
    }

    void promoteNode(String failedMaster) {
        throw new IllegalStateException("Cannot promote witness node.");
    }

    void stopMonitoringForManualPromotion() {
        throw new IllegalStateException("Witness node does not monitor a database.");
    }

    void switchToNewMaster(Address newMaster) {
        throw new IllegalStateException("Witness node does not monitor a database.");
    }

    void prepareForSwitchover(String recoveryConfText) {
        throw new IllegalStateException("Witness node cannot be reconfigured as a standby during swtichover.");
    }

    void cancelSwitchover() {
        throw new IllegalStateException("Witness node cannot be reconfigured as a standby during swtichover.");
    }

    void reconfigureAsStandby(Address newMaster) {
        throw new IllegalStateException("Witness node cannot be reconfigured as a standby during swtichover.");
    }

    public void run() {
        try {
            if (!doStartup()) {
                shutdown();
                return;
            }
            ShutdownNotificationService.getInstance().nodeHasStarted();
            if (!Utils.writePid()) {
                shutdown();
                return;
            }
            if (!checkControllerAuthFile()) {
                shutdown();
                return;
            }
            Notifications.WITNESS_STARTED.addSubjectParams(new String[] { this.env.getBindingAddress(), this.env.getClusterName() }).send();
            System.out.println("Witness is running.");
            if (this.env.isVipEnabled())
                VipMonitor.getInstance().startMonitoring(false);
            if (!AgentLauncher.signalStartToLauncher())
                shutdown();
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof SecurityException) {
                LOGGER.log(Level.SEVERE, "Exception starting service: {0}", cause.toString());
                System.err.println("Error starting service: " + cause.getMessage());
            } else {
                LOGGER.log(Level.SEVERE, "Exception starting service", e);
                System.err.println("Error starting service: " + Utils.getMsg(e));
            }
            if (this.channel != null)
                this.channel.close();
            shutdown();
        }
    }

    public void resumeMonitoring() throws Exception {
        throw new Exception("Resume command cannot be called on witness.");
    }

    public void shutdown() {
        commonShutdown();
        Utils.deletePidAndKey();
    }
}
