package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.DBMonitor;
import com.enterprisedb.efm.EfmProps;
import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import com.enterprisedb.efm.exec.AgentLauncher;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.DBUtils;
import com.enterprisedb.efm.utils.MasterStandbyMonitor;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.RecoveryConfFile;
import com.enterprisedb.efm.utils.ShutdownNotificationService;
import com.enterprisedb.efm.utils.StandbyInfo;
import com.enterprisedb.efm.utils.Utils;
import com.enterprisedb.efm.utils.VipMonitor;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import org.jgroups.Address;
import org.jgroups.Channel;

public class EfmAgent extends EfmNode {
    private volatile DBMonitor monitor = null;

    private volatile boolean dbRunning = false;

    private EfmAgentDelegate delegate;

    private final ReentrantReadWriteLock delegateLock = new ReentrantReadWriteLock();

    private final ReentrantReadWriteLock.ReadLock delRead = this.delegateLock.readLock();

    private final ReentrantReadWriteLock.WriteLock delWrite = this.delegateLock.writeLock();

    private volatile String recoveryConfText;

    private final MasterStandbyMonitor msMonitor = new MasterStandbyMonitor(this);

    void setAgentDelegate(EfmAgentDelegate newDelegate) {
        this.delWrite.lock();
        try {
            this.nodeTypeTime = System.currentTimeMillis();
            if (this.delegate != null) {
                LOGGER.log(Level.INFO, "setAgentDelegate: I was {0}, will now be {1}.", new Object[] { this.delegate.getAgentTypeName(), newDelegate.getAgentTypeName() });
                this.delegate.replaced();
            } else {
                LOGGER.log(Level.FINE, "Setting delegate for first time to {0}", newDelegate);
            }
            EfmNode.NodeType newDelegateType = newDelegate.getType();
            ShutdownNotificationService sns = ShutdownNotificationService.getInstance();
            switch (newDelegateType) {
                case MASTER:
                    LOGGER.fine("Setting master shutdown notification.");
                    sns.setShutdownNotification(Notifications.MASTER_EXITED);
                    break;
                case STANDBY:
                    LOGGER.fine("Setting standby shutdown notification.");
                    sns.setShutdownNotification(Notifications.STANDBY_EXITED);
                    break;
                case PROMOTING:
                    LOGGER.fine("Setting promoting shutdown notification.");
                    sns.setShutdownNotification(Notifications.PROMOTING_EXITED);
                    break;
                case WITNESS:
                    LOGGER.fine("Setting witness shutdown notification.");
                    sns.setShutdownNotification(Notifications.WITNESS_EXITED);
                    break;
                case IDLE:
                    LOGGER.fine("Setting idle shutdown notification.");
                    sns.setShutdownNotification(Notifications.IDLE_EXITED);
                    break;
                default:
                    LOGGER.fine("Setting default shutdown notification.");
                    sns.setShutdownNotification(null);
                    break;
            }
            if (newDelegateType == EfmNode.NodeType.MASTER) {
                if (!RecoveryConfFile.getInstance().isMonitoring())
                    RecoveryConfFile.getInstance().startMonitoring();
            } else if (RecoveryConfFile.getInstance().isMonitoring()) {
                RecoveryConfFile.getInstance().stopMonitoring();
            }
            LOGGER.log(Level.INFO, "Setting agent to type {0}", newDelegate.getAgentTypeName());
            if (this.delegate != null && newDelegateType != this.delegate.getType())
                if (isCoordinator()) {
                    addToClusterStatus(newDelegateType, this.channel.getAddress());
                } else {
                    try {
                        ClusterUtils.sendMessageToCoordinator((Channel)this.channel, newDelegateType);
                    } catch (Exception e) {
                        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                        LOGGER.log(Level.SEVERE, "Could tell coordinator to update state.", e);
                    }
                }
            if (this.env.isVipEnabled()) {
                VipMonitor.getInstance().stopMonitoring();
                if (newDelegateType == EfmNode.NodeType.MASTER && this.delegate != null) {
                    VipMonitor.getInstance().startMonitoring(true);
                } else if (newDelegateType == EfmNode.NodeType.STANDBY) {
                    VipMonitor.getInstance().startMonitoring(false);
                }
            }
            this.delegate = newDelegate;
        } finally {
            this.delWrite.unlock();
        }
    }

    public void run() {
        try {
            if (!doStartup()) {
                shutdown();
                return;
            }
            this.delRead.lock();
            try {
                if (!this.delegate.setupAfterJoiningCluster()) {
                    shutdown();
                    return;
                }
            } finally {
                this.delRead.unlock();
            }
            ShutdownNotificationService.getInstance().nodeHasStarted();
            this.monitor = DBUtils.createMonitor(this.env.getBindingAddress());
            if (!Utils.writePid()) {
                shutdown();
                return;
            }
            if (!checkControllerAuthFile()) {
                shutdown();
                return;
            }
            if (getNodeType() != EfmNode.NodeType.IDLE)
                System.out.println("Now monitoring database.");
            this.msMonitor.start();
            if (!AgentLauncher.signalStartToLauncher()) {
                shutdown();
                return;
            }
            if (getNodeType() != EfmNode.NodeType.IDLE) {
                this.dbRunning = true;
                startMonitoring();
            } else {
                Notifications.IDLE_STARTED.addSubjectParams(new String[] { this.env.getBindingAddress(), this.env.getClusterName() }).send();
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof SecurityException) {
                String hint = "If other nodes are already running in the cluster, please verify that the address for this node is on the allowed node host list.";
                LOGGER.log(Level.SEVERE, "Exception starting service: {0}", cause.toString());
                LOGGER.log(Level.SEVERE, "If other nodes are already running in the cluster, please verify that the address for this node is on the allowed node host list.");
                System.err.println("There was an error starting service: " + cause.getMessage());
                System.err.println("If other nodes are already running in the cluster, please verify that the address for this node is on the allowed node host list.");
            } else {
                LOGGER.log(Level.SEVERE, "Exception starting service", e);
                System.err.println("Error starting service: " + Utils.getMsg(e));
            }
            shutdown();
        }
    }

    public Object doHandle(Object payload) throws Exception {
        if ("node_status".equals(payload)) {
            long currentTime = System.currentTimeMillis();
            NodeStatus nodeStatus = new NodeStatus(this.delegate.getAgentTypeName(), this.env.getBindingAddress(), true, this.dbRunning, currentTime - this.nodeTypeTime, getLastPromoted(), currentTime - getTimeLastPromoted(), this.channel.getAddress());
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.log(Level.INFO, "responding to db status request with status: {0}", nodeStatus.marshal());
            return nodeStatus;
        }
        return null;
    }

    public void checkRecoveryState() {
        this.delRead.lock();
        try {
            this.delegate.checkRecoveryState();
        } finally {
            this.delRead.unlock();
        }
    }

    void promoteNode(String failedMaster) {
        LOGGER.fine("promoteNode called.");
        this.delegate.promoteNode(failedMaster);
    }

    void stopMonitoringForManualPromotion() {
        LOGGER.info("Will stop monitoring database for manual promotion");
        this.state = EfmNode.State.PROMOTE;
        stopMonitoring();
    }

    void switchToNewMaster(Address newMaster) {
        String host, restartCommand;
        if (getNodeType() != EfmNode.NodeType.STANDBY) {
            LOGGER.severe("Non-standby agent cannot switch replication to use new master");
            return;
        }
        if (!this.env.autoReconfigure()) {
            LOGGER.log(Level.INFO, "The {0} property is set to false. Will not attempt to reconfigure standby database.", EfmProps.AUTO_STANDBY_RECONFIGURE.getPropName());
            Notifications.RECONFIGURE_OFF.addSubjectParams(new String[] { this.env.getClusterName() }).send();
            return;
        }
        synchronized (this.clusterStateLock) {
            host = ClusterUtils.getHost((Channel)this.channel, newMaster, this.clusterState);
        }
        if (host == null) {
            LOGGER.log(Level.SEVERE, "Cannot retrieve host address for {0}", newMaster);
            return;
        }
        if (!reconfigureRecoveryConf(host))
            return;
        LOGGER.log(Level.INFO, "Restarting database. Will stop monitoring during restart");
        this.state = EfmNode.State.RECONFIGURING;
        stopMonitoring();
        if (this.env.useDbService()) {
            restartCommand = this.env.getRootSudoCommand() + " " + this.env.getRootFunctionsScript() + " " + SudoFunctions.RESTART_DB_SERVICE.toString() + " " + this.env.getClusterName();
        } else {
            restartCommand = this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.RESTART_DB.toString() + " " + this.env.getClusterName();
        }
        ProcessResult result1 = ExecUtil.performExec(new String[] { restartCommand });
        if (result1.getExitValue() != 0) {
            StringBuilder problem = new StringBuilder();
            result1.addNiceOutput(problem);
            String problemString = problem.toString();
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Could not restart standby: " + problemString }).send();
            LOGGER.log(Level.WARNING, "Could not restart database. Will attempt to resume monitoring. Command: {0}. Result: {1}", new Object[] { restartCommand, problemString });
        }
        long startTime = System.currentTimeMillis();
        while (isDbRunning() && System.currentTimeMillis() < startTime + 60000L) {
            LOGGER.fine("Waiting for db monitor to stop before resuming monitoring after db restart.");
            try {
                TimeUnit.SECONDS.sleep(10L);
            } catch (InterruptedException e) {
                LOGGER.warning("Interrupted waiting for db monitor to stop");
            }
        }
        if (isDbRunning()) {
            String message = "DB monitor did not shut down as expected before database restart. Check the state of the agent.";
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "DB monitor did not shut down as expected before database restart. Check the state of the agent." }).send();
            LOGGER.severe("DB monitor did not shut down as expected before database restart. Check the state of the agent.");
        }
        LOGGER.log(Level.INFO, "Resuming monitoring.");
        try {
            resumeMonitoring();
        } catch (Exception e) {
            Notifications.RESTART_RESUME_TIMED_OUT.addSubjectParams(new String[] { this.env.getClusterName() }).send();
            LOGGER.log(Level.SEVERE, "Could not resume monitoring after database restart: {0}", e.toString());
        }
    }

    void prepareForSwitchover(String text) {
        LOGGER.log(Level.WARNING, "Will prepare for switchover with upcoming promotion.");
        this.recoveryConfText = text;
    }

    void cancelSwitchover() {
        this.recoveryConfText = null;
    }

    void fenceOffIsolatedMaster() {
        LOGGER.severe("Fencing off database on this node.");
        this.state = EfmNode.State.FENCING_ISOLATED;
        stopMonitoring();
    }

    public String getRecoveryConfText() {
        return this.recoveryConfText;
    }

    private void startMonitoring() {
        this.monitor.watchDB(this.delegate.getNowWatchingNotification());
        this.dbRunning = false;
        if (this.state == EfmNode.State.SHUTDOWN)
            return;
        if (this.state == EfmNode.State.UNKNOWN) {
            LOGGER.warning("Node will now become idle.");
            this.delWrite.lock();
            try {
                this.state = EfmNode.State.RUNNING;
                setAgentDelegate(new EfmZombie(this, false));
            } finally {
                this.delWrite.unlock();
            }
            return;
        }
        if (this.state == EfmNode.State.RECONFIGURING) {
            LOGGER.log(Level.INFO, "Agent has stopped watching the local database while it is being reconfigured.");
            return;
        }
        if (this.state == EfmNode.State.FENCING_ISOLATED) {
            this.delWrite.lock();
            try {
                if (this.delegate instanceof EfmMaster) {
                    LOGGER.fine("Releasing vip and writing recovery.conf.");
                    ClusterUtils.releaseVip(true, true);
                    ((EfmMaster)this.delegate).writeRecoveryConf();
                    setAgentDelegate(new EfmZombie(this, false));
                    executeMasterIsolatedScript();
                } else {
                    throw new AssertionError("Cannot fence off node with type " + this.delegate.getAgentTypeName());
                }
            } finally {
                this.delWrite.unlock();
            }
        }
        this.delWrite.lock();
        try {
            if (this.delegate.getType() == EfmNode.NodeType.MASTER && !this.state.equals(EfmNode.State.PROMOTE) && !this.state.equals(EfmNode.State.FENCING_ISOLATED) && otherNodesCanPingDB()) {
                Notifications.MASTER_CANNOT_PING_RUNNING_DB.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getBindingAddress() }).send();
                setAgentDelegate(new EfmZombie(this, false));
                return;
            }
            this.delegate.cleanupAfterDbFailure();
            if (this.state != EfmNode.State.PROMOTE && this.state != EfmNode.State.FENCING_ISOLATED) {
                try {
                    Notifications.DB_FAILURE.addSubjectParams(new String[] { this.delegate.getAgentTypeName(), this.env.getClusterName() }).addBodyParams(new String[] { this.env.getBindingAddress() }).send();
                    LOGGER.log(Level.INFO, "Telling coordinator that db has failed.");
                    if (isCoordinator()) {
                        handleDbFailure(this.channel.getAddress());
                    } else {
                        ClusterUtils.sendMessageToCoordinator((Channel)this.channel, "database_failure");
                    }
                } catch (Exception e) {
                    Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                    LOGGER.severe("Could not contact coordinator: " + e);
                }
                executePostDbFailScript();
                setAgentDelegate(new EfmZombie(this, true));
            } else if (this.state == EfmNode.State.FENCING_ISOLATED) {
                this.state = EfmNode.State.RUNNING;
            } else {
                StandbyInfo newBoss;
                if (this.state == EfmNode.State.PROMOTE && this.recoveryConfText != null && !this.recoveryConfText.isEmpty())
                    if (getNodeType() == EfmNode.NodeType.MASTER) {
                        if (!stopMasterDatabase()) {
                            LOGGER.severe("Could not stop master database before promoting standby. Switchover cannot continue.");
                            LOGGER.log(Level.WARNING, "Attempting to resume monitoring.");
                            if (this.monitor.checkOnce(false, this.env.getLocalDbTimeout())) {
                                removeRecoveryConf();
                                this.dbRunning = true;
                                (new Thread(new Runnable() {
                                    public void run() {
                                        EfmNode.LOGGER.info("Resuming monitoring.");
                                        EfmAgent.this.callResumedScript();
                                        EfmAgent.this.startMonitoring();
                                    }
                                },  "db_monitor_resumed")).start();
                            } else {
                                LOGGER.log(Level.WARNING, "Can no longer connect to database.");
                                this.delWrite.lock();
                                try {
                                    if (getNodeType() != EfmNode.NodeType.IDLE)
                                        setAgentDelegate(new EfmZombie(this, false));
                                } finally {
                                    this.delWrite.unlock();
                                }
                            }
                            return;
                        }
                    } else {
                        LOGGER.severe("A non-master node has stored recovery.conf information in preparation to be reconfigured as a standby after switchover. This is not a valid state -- the EFM agents may need to be restarted.");
                    }
                if (this.delegate.getType() != EfmNode.NodeType.IDLE)
                    setAgentDelegate(new EfmZombie(this, false));
                try {
                    newBoss = chooseStandbyForPromotion();
                } catch (Exception e) {
                    newBoss = null;
                }
                if (newBoss == null) {
                    LOGGER.log(Level.WARNING, "No standby nodes could be found to promote.");
                } else {
                    LOGGER.log(Level.WARNING, "Will tell standby at {0} to promote.", newBoss.getHost());
                    tellStandbyToPromote(newBoss.getAddress(), this.env.getBindingAddress());
                }
            }
            if (this.delegate.getType() != EfmNode.NodeType.IDLE)
                setAgentDelegate(new EfmZombie(this, false));
        } finally {
            this.delWrite.unlock();
            if (this.state == EfmNode.State.PROMOTE) {
                LOGGER.info("Setting state back to RUNNING instead of PROMOTE");
                this.state = EfmNode.State.RUNNING;
            }
        }
    }

    void stopMonitoringForUnknown() {
        this.state = EfmNode.State.UNKNOWN;
        stopMonitoring();
    }

    void stopMonitoring() {
        if (this.monitor != null)
            this.monitor.shutdown();
    }

    public void shutdown() {
        commonShutdown();
        this.msMonitor.stop();
        stopMonitoring();
        Utils.deletePidAndKey();
        LOGGER.info("Exiting.");
    }

    public synchronized void resumeMonitoring() throws Exception {
        boolean inRecovery;
        if (isDbRunning())
            throw new Exception("Database is already running and is being monitored.");
        try {
            LOGGER.info("Connecting to database and determining recovery status");
            inRecovery = this.monitor.isInRecovery();
        } catch (SQLException sqle) {
            LOGGER.log(Level.WARNING, "Cannot connect to database or determine recovery status:{0}. SQL state: {1}", new Object[] { sqle.toString(), sqle.getSQLState() });
            this.delWrite.lock();
            try {
                if (getNodeType() != EfmNode.NodeType.IDLE)
                    setAgentDelegate(new EfmZombie(this, false));
            } finally {
                this.delWrite.unlock();
            }
            throw sqle;
        }
        verifyServiceName();
        if (inRecovery) {
            if (this.state == EfmNode.State.RECONFIGURING) {
                LOGGER.log(Level.INFO, "Setting state back to running from 'reconfiguring' state.");
                this.state = EfmNode.State.RUNNING;
            } else {
                this.delWrite.lock();
                try {
                    setAgentDelegate(new EfmStandby(this));
                    if (!this.delegate.setupAfterJoiningCluster())
                        throw new Exception("Setup steps before monitoring database failed. See agent log for more information.");
                    if (!this.env.isPromotable())
                        setPriority(this.env.getBindingAddress(), 0);
                } finally {
                    this.delWrite.unlock();
                }
            }
        } else {
            synchronized (this.clusterStateLock) {
                if (this.clusterState.getMaster() != null)
                    throw new Exception(String.format("Database is not in recovery and there is already a master node in cluster: %s", new Object[] { this.clusterState.getMaster() }));
                if (this.clusterState.getPromoting() != null)
                    throw new Exception(String.format("Database is not in recovery and there is already a promoting node in cluster: %s", new Object[] { this.clusterState.getPromoting() }));
            }
            this.delWrite.lock();
            try {
                setAgentDelegate(new EfmMaster(this));
            } finally {
                this.delWrite.unlock();
            }
            this.delRead.lock();
            try {
                if (!this.delegate.setupAfterJoiningCluster())
                    throw new Exception("Setup steps failed, but the agent is monitoring the master database. See agent log and cluster status for more information. Manual intervention may be required.");
            } finally {
                this.delRead.unlock();
            }
        }
        this.dbRunning = true;
        (new Thread(new Runnable() {
            public void run() {
                EfmNode.LOGGER.info("Resuming monitoring.");
                EfmAgent.this.callResumedScript();
                EfmAgent.this.startMonitoring();
            }
        },  "db_monitor_resumed")).start();
    }

    public boolean isDbRunning() {
        return this.dbRunning;
    }

    boolean dbInRecovery(Level level) throws SQLException {
        return this.monitor.isInRecovery(level);
    }

    protected boolean checkVipUse() {
        if (this.delegate.getType() == EfmNode.NodeType.MASTER) {
            String vip = this.env.getVirtualIp();
            if (ClusterUtils.isBroadcastingVip()) {
                System.out.println("This node is currently broadcasting virtual IP " + vip);
            } else if (ClusterUtils.isVipReachable()) {
                System.err.println("Warning: virtualIp ( " + vip + " ) appears " + "to be assigned to another node on the network. Exiting");
                return false;
            }
        }
        return true;
    }

    boolean determineStartupAgentType() {
        boolean inRecovery;
        String host = this.env.getBindingAddress();
        try {
            inRecovery = DBUtils.createMonitor(host).isInRecovery();
        } catch (PasswordDecryptException pde) {
            Throwable throwable;
            PasswordDecryptException passwordDecryptException1 = pde;
            while (passwordDecryptException1.getCause() != null) {
                throwable = passwordDecryptException1.getCause();
                String msg = throwable.getMessage();
                if (msg == null || msg.isEmpty())
                    msg = throwable.toString();
                System.err.println("Could not decode database password. Check that it has been encrypted properly. Error: " + msg);
            }
            return false;
        } catch (SQLException sqle) {
            System.err.println("Cannot reach local database: " + sqle.toString());
            System.err.println("Will start in IDLE mode.");
            setAgentDelegate(new EfmZombie(this, false));
            return true;
        } catch (Exception e) {
            System.err.println("Could not determine if the node at " + host + " is master or standby: " + e.toString());
            return false;
        }
        try {
            verifyServiceName();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        if (inRecovery) {
            System.out.println("Continuing startup in standby mode.");
            setAgentDelegate(new EfmStandby(this));
            return this.env.readTriggerFileLocation();
        }
        System.out.println("Continuing startup in master mode.");
        setAgentDelegate(new EfmMaster(this));
        return true;
    }

    EfmNode.NodeType getNodeType() {
        this.delRead.lock();
        try {
            return this.delegate.getType();
        } finally {
            this.delRead.unlock();
        }
    }

    void reconfigureAsStandby(Address newMaster) {
        String host;
        if (getNodeType() != EfmNode.NodeType.IDLE || this.recoveryConfText == null || this.recoveryConfText.isEmpty()) {
            LOGGER.fine("Ignoring reconfigureAsStandby call");
            return;
        }
        this.recoveryConfText = null;
        synchronized (this.clusterStateLock) {
            host = ClusterUtils.getHost((Channel)this.channel, newMaster, this.clusterState);
        }
        if (host == null) {
            LOGGER.log(Level.SEVERE, "Cannot retrieve host address for {0}", newMaster);
            return;
        }
        if (!reconfigureRecoveryConf(host))
            return;
        LOGGER.info("Restarting database as standby.");
        if (!startDatabase())
            return;
        try {
            resumeMonitoring();
        } catch (Exception e) {
            Notifications.RESTART_RESUME_TIMED_OUT.addSubjectParams(new String[] { this.env.getClusterName() }).send();
            LOGGER.log(Level.SEVERE, "Could not resume monitoring after database start: {0}", e.toString());
        }
    }

    void notifyNodesOfNewMaster() {
        LOGGER.log(Level.INFO, "Informing all nodes of new master address so that they can reconfigure.");
        try {
            this.channel.send(null, "switch_to_new_master");
        } catch (Exception e) {
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
            LOGGER.log(Level.SEVERE, "Could not send message to all nodes: {0}", e.toString());
        }
    }

    void resumeReplay() throws SQLException {
        this.monitor.resumeReplay();
    }

    void broadcastLastPromoted(String lastPromoted) {
        setLastPromoted(lastPromoted);
        try {
            ClusterUtils.sendToNodes("last_promoted_" + lastPromoted, this.dispatcher);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not inform all nodes that master at {0} is being promoted. This could cause problems if a previous master rejoins only part of the cluster and does not see the new master. Error: {1}", new Object[] { lastPromoted, e });
        }
    }

    private void removeRecoveryConf() {
        LOGGER.log(Level.INFO, "Removing the recovery.conf file that was created.");
        ProcessResult result = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.REMOVE_RECOVERY_CONF + " " + this.env.getClusterName() });
        if (result.getExitValue() != 0) {
            LOGGER.log(Level.SEVERE, "Could not remove recovery.conf file.This should be removed before restarting the master database.");
            StringBuilder sb = new StringBuilder();
            result.addNiceOutput(sb);
            LOGGER.severe(sb.toString());
        }
    }

    private void verifyServiceName() throws Exception {
        if (!this.env.useDbService())
            return;
        String statusCommand = this.env.getRootSudoCommand() + " " + this.env.getRootFunctionsScript() + " " + SudoFunctions.DB_SERVICE_STATUS.toString() + " " + this.env.getClusterName();
        ProcessResult result = ExecUtil.performExec(true, new String[] { statusCommand });
        if (result.getExitValue() == 0)
            return;
        String message = String.format("The database is running, but a status call using the db.service.name property returned non-zero exit code %s. This probably means there is a problem with the db.service.name value. This must be fixed before the database can be monitored.", new Object[] { Integer.valueOf(result.getExitValue()) });
        LOGGER.warning(message);
        throw new Exception(message + " See status call output in log for more information.");
    }

    private void executePostDbFailScript() {
        String script = this.env.getPostDbFailScript();
        if (script == null)
            return;
        LOGGER.log(Level.INFO, "Attempting to run post-db-failure script {0}", script);
        ProcessResult result = ExecUtil.performExec(new String[] { script });
        int exitVal = result.getExitValue();
        if (exitVal != 0) {
            Notifications.POST_DB_FAIL_SCRIPT_FAILURE.addBodyParams(new String[] { script, Integer.toString(exitVal), result.toString() }).send();
        } else {
            Notifications.POST_DB_FAIL_SCRIPT_RUN.addBodyParams(new String[] { script, result.toString() }).send();
        }
    }

    private void callResumedScript() {
        String script = this.env.getResumedScript();
        if (script == null)
            return;
        LOGGER.log(Level.INFO, "Attempting to run agent resumed script {0}", script);
        ProcessResult result = ExecUtil.performExec(new String[] { script });
        int exitVal = result.getExitValue();
        if (exitVal != 0) {
            Notifications.RESUMED_SCRIPT_FAILURE.addBodyParams(new String[] { script, Integer.toString(exitVal), result.toString() }).send();
        } else {
            Notifications.RESUMED_SCRIPT_RUN.addBodyParams(new String[] { script, result.toString() }).send();
        }
    }

    private boolean stopMasterDatabase() {
        String stopCommand;
        if (this.env.useDbService()) {
            stopCommand = this.env.getRootSudoCommand() + " " + this.env.getRootFunctionsScript() + " " + SudoFunctions.STOP_DB_SERVICE.toString() + " " + this.env.getClusterName();
        } else {
            stopCommand = this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.STOP_DB.toString() + " " + this.env.getClusterName();
        }
        ProcessResult result = ExecUtil.performExec(new String[] { stopCommand });
        if (result.getExitValue() == 0)
            return true;
        StringBuilder problem = new StringBuilder();
        result.addNiceOutput(problem);
        String problemString = problem.toString();
        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Could not stop database: " + problemString }).send();
        LOGGER.log(Level.WARNING, "Could not stop database. Will not be able to continue switchover Command: {0}. Result: {1}", new Object[] { stopCommand, problemString });
        return false;
    }

    private boolean startDatabase() {
        String startCommand;
        if (this.env.useDbService()) {
            startCommand = this.env.getRootSudoCommand() + " " + this.env.getRootFunctionsScript() + " " + SudoFunctions.START_DB_SERVICE.toString() + " " + this.env.getClusterName();
        } else {
            startCommand = this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.START_DB.toString() + " " + this.env.getClusterName();
        }
        ProcessResult result = ExecUtil.performExec(new String[] { startCommand });
        if (result.getExitValue() == 0)
            return true;
        StringBuilder problem = new StringBuilder();
        result.addNiceOutput(problem);
        String problemString = problem.toString();
        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Could not start database: " + problemString }).send();
        LOGGER.log(Level.WARNING, "Could not start database after switchover Command: {0}. Result: {1}", new Object[] { startCommand, problemString });
        return false;
    }

    private boolean reconfigureRecoveryConf(String host) {
        LOGGER.log(Level.INFO, "Changing recovery.conf file to use new host {0}.", host);
        String reconfigureCommand = this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.CHANGE_MASTER.toString() + " " + this.env.getClusterName() + " " + host;
        ProcessResult result = ExecUtil.performExec(new String[] { reconfigureCommand });
        if (result.getExitValue() == 0)
            return true;
        StringBuilder problem = new StringBuilder();
        result.addNiceOutput(problem);
        String problemString = problem.toString();
        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Could not reconfigure standby: " + problemString }).send();
        LOGGER.log(Level.WARNING, "Could not change recovery.conf file. Command: {0}. Result: {1}", new Object[] { reconfigureCommand, problemString });
        return false;
    }

    private void executeMasterIsolatedScript() {
        String script = this.env.getMasterIsolatedScript();
        if (script == null)
            return;
        LOGGER.log(Level.INFO, "Attempting to run master isolated script {0}", script);
        ProcessResult result = ExecUtil.performExec(new String[] { script });
        int exitVal = result.getExitValue();
        if (exitVal != 0) {
            Notifications.MASTER_ISO_SCRIPT_FAILURE.addBodyParams(new String[] { script, Integer.toString(exitVal), result.toString() }).send();
        } else {
            Notifications.MASTER_ISO_SCRIPT_RUN.addBodyParams(new String[] { script, result.toString() }).send();
        }
    }

    private boolean otherNodesCanPingDB() {
        LOGGER.log(Level.WARNING, "Confirming that no other nodes can ping database before declaring database failure.");
        try {
            if (nodeStillUp(EfmNode.PingType.DB_PING, this.env.getBindingAddress())) {
                LOGGER.warning("Other node(s) able to reach database.");
                return true;
            }
            LOGGER.info("No other nodes able to reach database");
            return false;
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Thread interrupted trying to determine if any nodes can reach local database. Returning false.");
            return false;
        }
    }
}
