package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.ClusterState;
import com.enterprisedb.efm.DBMonitor;
import com.enterprisedb.efm.EfmProps;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.admin.AdminServer;
import com.enterprisedb.efm.admin.EfmStatus;
import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exceptions.NoStandbysException;
import com.enterprisedb.efm.exceptions.NotEnoughStandbysException;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import com.enterprisedb.efm.exec.WorkQueue;
import com.enterprisedb.efm.main.SubCommand;
import com.enterprisedb.efm.utils.ClusterSizeUpdater;
import com.enterprisedb.efm.utils.ClusterStateVerifier;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.DBUtils;
import com.enterprisedb.efm.utils.LicenseManager;
import com.enterprisedb.efm.utils.LockFile;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.StandbyInfo;
import com.enterprisedb.efm.utils.SuccessHolder;
import com.enterprisedb.efm.utils.VipMonitor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.jgroups.MembershipListener;
import org.jgroups.Message;
import org.jgroups.MessageListener;
import org.jgroups.Receiver;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

public abstract class EfmNode extends ReceiverAdapter implements RequestHandler {
    public enum NodeType {
        MASTER("Master"),
        PROMOTING("Promoting"),
        STANDBY("Standby"),
        WITNESS("Witness"),
        IDLE("Idle");

        private final String name;

        NodeType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    enum State {
        STARTING, RUNNING, PROMOTE, FENCING_ISOLATED, RECONFIGURING, SHUTDOWN, UNKNOWN;
    }

    enum PingType {
        DB_PING, VIP_PING;
    }

    volatile State state = State.STARTING;

    volatile boolean isolated = false;

    public static final String GET_PROPS = "get_properties";

    public static final String DATABASE_FAILURE = "database_failure";

    public static final String PREPARE_STOP = "prepare_to_stop";

    public static final String FENCE_DB = "fence_database";

    public static final String STOP_MONITORING_FOR_M_PROMOTION = "stop_monitoring_for_manual_promotion";

    public static final String LOAD_SHARED_STATE = "load_shared_state";

    public static final String SWITCH_TO_NEW_MASTER = "switch_to_new_master";

    public static final String GET_RECOVERY_CONF_TEXT = "get_recovery_conf_text";

    public static final String IDLE_DROP_VIP = "idle_node_drop_vip";

    public static final String PING_NEW_DB = "ping_new_db_";

    public static final String PROMOTE_SELF = "promote_self_";

    public static final String RECOVERY_CONF_TEXT = "recovery_conf_text_";

    public static final String CLEAR_RECOVERY_CONF_TEXT = "clear_recovery_conf_text";

    public static final String LAST_PROMOTED = "last_promoted_";

    public static final int REPLACEMENT_MASTER_RETRIES = 5;

    public static final long RECHECK_WAIT_SECONDS = 10L;

    ClusterState clusterState = new ClusterState();

    final Object clusterStateLock = new Object();

    boolean clusterStateInitialized = false;

    volatile JChannel channel;

    volatile MessageDispatcher dispatcher;

    final Environment env = Environment.getEnvironment();

    static final Logger LOGGER = LogManager.getEfmLogger();

    AdminServer adminServer;

    volatile long nodeTypeTime;

    private String lastPromoted;

    private long timeLastPromoted;

    private final Object lastPromotedLock = new Object();

    private volatile String controllerAuthText;

    private volatile float stableSize = 0.0F;

    private final ClusterStateVerifier stateVerifier = new ClusterStateVerifier(this);

    private Set<Address> suspected = new HashSet<Address>();

    private static final String PING_TARGET_SEP = "\\|";

    public void receive(Message msg) {
        final Object msgObj = msg.getObject();
        if (msgObj instanceof String) {
            String msgString = ((String)msgObj).trim();
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Received message: {0}", msgString);
            if (SubCommand.STOP_CLUSTER.getValue().equalsIgnoreCase(msgString)) {
                LOGGER.info("Received stop command.");
                shutdown();
            } else if ("load_shared_state".equals(msgString)) {
                if (isCoordinator()) {
                    LOGGER.fine("Coordinator ignoring 'load shared state' msg.");
                } else {
                    LOGGER.finest("**** submitting task to load shared state");
                    WorkQueue.executeTask(new Runnable() {
                        public void run() {
                            EfmNode.LOGGER.info("Reloading shared state per coordinator command");
                            try {
                                EfmNode.this.channel.getState(null, 3000L * EfmNode.this.env.getRemoteDbTimeout());
                            } catch (Exception e) {
                                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                                EfmNode.LOGGER.severe("Could not load shared state: " + e.toString());
                            }
                        }
                    });
                }
            } else if (msgString.startsWith("ping_new_db_")) {
                final String host = msgString.substring("ping_new_db_".length());
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.checkNewDBPing(host);
                    }
                });
            } else if (msgString.startsWith(SubCommand.ADD_NODE.name())) {
                String[] msgParts = msgString.split("\\|");
                final String address = msgParts[1];
                final int priority = Integer.parseInt(msgParts[2]);
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "received call from {0} to add node with address {1} and priority {2}", new Object[] { msg.getSrc(), address, Integer.valueOf(priority) });
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.addNode(address, priority);
                    }
                });
            } else if (msgString.startsWith(SubCommand.REMOVE_NODE.name())) {
                String[] msgParts = msgString.split("\\|");
                final String address = msgParts[1];
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "received call from {0} to remove node with address {1}", new Object[] { msg.getSrc(), address });
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.removeNode(address);
                    }
                });
            } else if (msgString.startsWith(SubCommand.ALLOW_NODE.name())) {
                String[] msgParts = msgString.split("\\|");
                final String address = msgParts[1];
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "received call from {0} to allow node with address {1}", new Object[] { msg.getSrc(), address });
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.allowNode(address);
                    }
                });
            } else if (msgString.startsWith(SubCommand.SET_PRIORITY.name())) {
                String[] msgParts = msgString.split("\\|");
                final String address = msgParts[1];
                final int priority = Integer.parseInt(msgParts[2]);
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "received call from {0} to set priority with address {1} and priority {2}", new Object[] { msg.getSrc(), address, Integer.valueOf(priority) });
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.setPriority(address, priority);
                    }
                });
            } else if (msgString.startsWith(SubCommand.DISALLOW_NODE.name())) {
                String[] msgParts = msgString.split("\\|");
                final String address = msgParts[1];
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "received call from {0} to disallow node with address {1}", new Object[] { msg.getSrc(), address });
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.disallowNode(address);
                    }
                });
            } else if ("database_failure".equals(msgString)) {
                final Address source = msg.getSrc();
                LOGGER.finest("**** submitting task to handle db failure");
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.handleDbFailure(source);
                    }
                });
            } else if ("fence_database".equals(msgString)) {
                final EfmNode thisNode = this;
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        if (thisNode instanceof EfmAgent) {
                            ((EfmAgent)thisNode).fenceOffIsolatedMaster();
                        } else {
                            EfmNode.LOGGER.warning("Ignoring call to fence off this node");
                        }
                    }
                });
            } else if ("stop_monitoring_for_manual_promotion".equals(msgString)) {
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.this.stopMonitoringForManualPromotion();
                    }
                });
            } else if ("idle_node_drop_vip".equals(msgString)) {
                if (getNodeType() == NodeType.IDLE) {
                    if (ClusterUtils.isBroadcastingVip()) {
                        LOGGER.info("IDLE node dropping VIP");
                        ClusterUtils.releaseVip(true, true);
                    } else {
                        LOGGER.fine("IDLE node ignoring request to drop VIP");
                    }
                } else {
                    LOGGER.fine("Non-IDLE node ignoring request to drop VIP");
                }
            } else if ("switch_to_new_master".equals(msgString)) {
                if (getNodeType() == NodeType.STANDBY) {
                    final Address source = msg.getSrc();
                    LOGGER.finest("**** submitting task to switch to new master");
                    WorkQueue.executeTask(new Runnable() {
                        public void run() {
                            EfmNode.this.switchToNewMaster(source);
                        }
                    });
                } else if (getNodeType() == NodeType.IDLE) {
                    final Address source = msg.getSrc();
                    WorkQueue.executeTask(new Runnable() {
                        public void run() {
                            EfmNode.this.reconfigureAsStandby(source);
                        }
                    });
                } else {
                    LOGGER.log(Level.INFO, "Message to reconfigure to use new master ignored by non-standby/idle agent.");
                }
            } else if (msgString.startsWith("recovery_conf_text_")) {
                String recoveryConfText = msgString.substring("recovery_conf_text_".length());
                prepareForSwitchover(recoveryConfText);
            } else if ("clear_recovery_conf_text".equals(msgString)) {
                cancelSwitchover();
            }
        } else if (msgObj instanceof NodeType) {
            final Address source = msg.getSrc();
            WorkQueue.executeTask(new Runnable() {
                public void run() {
                    EfmNode.this.addToClusterStatus((EfmNode.NodeType)msgObj, source);
                }
            });
        } else {
            LOGGER.log(Level.WARNING, "Received unknown object: {0} from {1}", new Object[] { msgObj, ip(msg.getSrc()) });
        }
    }

    public Object handle(Message msg) throws Exception {
        Object payload = msg.getObject();
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "EfmNode.handle: {0} from {1}", new Object[] { payload, msg.getSrc() });
        if ("prepare_to_stop".equals(payload)) {
            LOGGER.info("Preparing to stop.");
            this.state = State.SHUTDOWN;
            return Boolean.valueOf(true);
        }
        if ("get_properties".equals(payload))
            return Environment.getEnvironment().getProperties();
        if ("get_recovery_conf_text".equals(payload)) {
            LOGGER.info("Node is being asked for recovery.conf text.");
            return EfmStandby.readRecoveryConf();
        }
        if (PingType.VIP_PING.name().equals(payload)) {
            boolean retVal = ClusterUtils.isVipReachable();
            LOGGER.log(Level.INFO, "Handle called for VIP check. Returning {0}.", Boolean.valueOf(retVal));
            return Boolean.valueOf(retVal);
        }
        if (payload instanceof String) {
            String payString = (String)payload;
            if (payString.startsWith(PingType.DB_PING.name())) {
                DBMonitor dbm;
                String host = payString.split("\\|")[1];
                try {
                    dbm = DBUtils.createMonitor(host);
                } catch (PasswordDecryptException e) {
                    LOGGER.severe(e.toString());
                    return Boolean.valueOf(false);
                }
                boolean retVal = dbm.checkOnce(false, this.env.getRemoteDbTimeout());
                LOGGER.log(Level.INFO, "Handle called for DB check. Returning {0}.", Boolean.valueOf(retVal));
                return Boolean.valueOf(retVal);
            }
            if (payString.startsWith("last_promoted_")) {
                setLastPromoted(payString.substring("last_promoted_".length()));
                LOGGER.log(Level.INFO, "Last promoted address set to {0}", this.lastPromoted);
                return Boolean.valueOf(true);
            }
            if (payString.startsWith("promote_self_")) {
                String failedMaster = payString.substring("promote_self_".length());
                promoteNode(failedMaster);
                return Boolean.valueOf(true);
            }
        }
        return doHandle(payload);
    }

    public void suspect(Address mbr) {
        LOGGER.log(Level.WARNING, "Address suspect: {0} at {1}", new Object[] { mbr, ip(mbr) });
        LOGGER.finest("suspect before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("suspect has lock");
            this.suspected.add(mbr);
        }
    }

    public void viewAccepted(final View view) {
        if (this.state == State.SHUTDOWN) {
            LOGGER.info("Ignoring view change during shutdown.");
            return;
        }
        Address localAddr = this.channel.getAddress();
        LOGGER.log(Level.INFO, "View changed. I am {0} Total jgroups nodes: {1}", new Object[] { localAddr, Integer.valueOf(view.size()) });
        WorkQueue.executeTask(new Runnable() {
            public void run() {
                EfmNode.this.handleViewChange(view);
            }
        });
    }

    public void getState(OutputStream output) throws Exception {
        LOGGER.fine("getState called");
        LOGGER.finest("getState before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("getState has lock");
            if (LOGGER.isLoggable(Level.FINER))
                LOGGER.log(Level.FINER, "state: {0}", this.clusterState);
            Util.objectToStream(this.clusterState, new DataOutputStream(output));
        }
    }

    public void setState(InputStream input) throws Exception {
        LOGGER.finest("setState before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("setState has lock");
            this.clusterState = (ClusterState)Util.objectFromStream(new DataInputStream(input));
            LOGGER.log(Level.INFO, "setState called: {0}", this.clusterState);
            if (!this.clusterStateInitialized) {
                this.clusterStateInitialized = true;
                updateNodesFile(this.channel.getView().getMembers());
            }
        }
    }

    public boolean isCoordinator(View view) {
        Address coordinator = view.getMembers().get(0);
        boolean retVal = coordinator.equals(this.channel.getAddress());
        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.log(Level.FINER, "isCoordinator: {0}", Boolean.valueOf(retVal));
        return retVal;
    }

    public boolean isCoordinator() {
        return isCoordinator(this.channel.getView());
    }

    public void shutdownCluster() {
        WorkQueue.executeTask(new Runnable() {
            public void run() {
                EfmNode.LOGGER.log(Level.INFO, "Sending message to {0} nodes to prepare for shutdown.", Integer.valueOf(EfmNode.this.getCurrentNodeCount()));
                try {
                    if (ClusterUtils.sendToNodes("prepare_to_stop", EfmNode.this.dispatcher)) {
                        EfmNode.LOGGER.info("Preparing to stop.");
                        EfmNode.this.state = EfmNode.State.SHUTDOWN;
                        EfmNode.this.channel.send(new Message(null, SubCommand.STOP_CLUSTER.getValue()));
                    }
                } catch (Exception e) {
                    Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                    EfmNode.LOGGER.log(Level.SEVERE, "There was an unexpected problem sending cluster shutdown signal: {0}", e.toString());
                }
            }
        });
    }

    public void verifyClusterState() {
        if (!isCoordinator()) {
            LOGGER.info("Node is no longer coordinator. Returning from call to verify cluster state.");
            return;
        }
        Set<NodeStatus> masterOrPromotings = new HashSet<NodeStatus>();
        Set<Address> standbys = new HashSet<Address>();
        Set<Address> witnesses = new HashSet<Address>();
        Set<Address> idles = new HashSet<Address>();
        boolean needUpdate = false;
        LOGGER.finest("verifyClusterState before lock");
        synchronized (this.clusterStateLock) {
            EfmStatus status;
            LOGGER.finest("verifyClusterState has lock");
            try {
                LOGGER.log(Level.FINE, "Verifying cluster state");
                status = ClusterUtils.sendStatusRequestToNodes(this.dispatcher, this.clusterState);
                String error = status.getError();
                if (error != null && !error.trim().isEmpty()) {
                    LOGGER.warning("There was an error obtaining cluster status from other nodes. Will wait and attempt again.");
                    this.stateVerifier.scheduleVerification();
                    return;
                }
            } catch (Exception e) {
                LOGGER.warning("Could not obtain cluster status from other nodes. Will wait and attempt again.");
                this.stateVerifier.scheduleVerification();
                return;
            }
            for (NodeStatus ns : status.getNodeStatusSet()) {
                String type = ns.getType();
                if (NodeType.MASTER.getName().equals(type)) {
                    masterOrPromotings.add(ns);
                    continue;
                }
                if (NodeType.PROMOTING.getName().equals(type)) {
                    masterOrPromotings.add(ns);
                    continue;
                }
                if (NodeType.STANDBY.getName().equals(type)) {
                    standbys.add(ns.getAddress());
                    continue;
                }
                if (NodeType.WITNESS.getName().equals(type)) {
                    witnesses.add(ns.getAddress());
                    continue;
                }
                if (NodeType.IDLE.getName().equals(type)) {
                    idles.add(ns.getAddress());
                    continue;
                }
                String msg = "Unrecognized node type: " + type;
                LOGGER.severe(msg);
                throw new AssertionError(msg);
            }
            if (masterOrPromotings.isEmpty()) {
                if (this.clusterState.getMaster() != null) {
                    LOGGER.log(Level.SEVERE, "No master node was found in cluster, but master {0} exists in internal cluster state. Removing from internal state.", this.clusterState.getMaster());
                    this.clusterState.setMaster(null);
                    needUpdate = true;
                }
                if (this.clusterState.getPromoting() != null) {
                    LOGGER.log(Level.SEVERE, "No promoting node was found in cluster, but promoting {0} exists in internal cluster state. Removing from internal state.", this.clusterState.getPromoting());
                    this.clusterState.setPromoting(null);
                    needUpdate = true;
                }
            } else if (masterOrPromotings.size() == 1) {
                NodeStatus current = masterOrPromotings.iterator().next();
                String currentMPType = current.getType();
                Address currentAddress = current.getAddress();
                if (this.clusterState.getMaster() == null && this.clusterState.getPromoting() == null) {
                    LOGGER.log(Level.SEVERE, "Unknown node of type {0} at {1} found in cluster.", new Object[] { currentMPType, current.getIpAddr() });
                    if (NodeType.MASTER.getName().equals(currentMPType)) {
                        this.clusterState.setMaster(current.getAddress());
                    } else if (NodeType.PROMOTING.getName().equals(currentMPType)) {
                        this.clusterState.setPromoting(current.getAddress());
                    }
                    needUpdate = true;
                } else if (NodeType.MASTER.getName().equals(currentMPType)) {
                    if (this.clusterState.getPromoting() != null) {
                        LOGGER.log(Level.SEVERE, "Master node {0} found in cluster, but promoting node in internal state {1} was not.", new Object[] { current.getIpAddr(), this.clusterState.getPromoting() });
                        this.clusterState.setPromoting(null);
                        needUpdate = true;
                    }
                    if (!currentAddress.equals(this.clusterState.getMaster())) {
                        LOGGER.log(Level.SEVERE, "Master node {0} found in cluster does not match {1} from internal state.", new Object[] { current.getIpAddr(), this.clusterState.getMaster() });
                        this.clusterState.setMaster(currentAddress);
                        needUpdate = true;
                    }
                } else if (NodeType.PROMOTING.getName().equals(currentMPType)) {
                    if (this.clusterState.getMaster() != null) {
                        LOGGER.log(Level.SEVERE, "Promoting node {0} found in cluster, but master node in internal state {1} was not.", new Object[] { current.getIpAddr(), this.clusterState.getMaster() });
                        this.clusterState.setMaster(null);
                        needUpdate = true;
                    }
                    if (!currentAddress.equals(this.clusterState.getPromoting())) {
                        LOGGER.log(Level.SEVERE, "Promoting node {0} found in cluster does not match {1} from internal state.", new Object[] { current.getIpAddr(), this.clusterState.getPromoting() });
                        this.clusterState.setPromoting(currentAddress);
                        needUpdate = true;
                    }
                }
            } else if (masterOrPromotings.size() > 1) {
                handleMultipleMasterOrPromoting(masterOrPromotings);
                needUpdate = true;
            }
            if (!compareToStatus(this.clusterState.getStandbys(), standbys, NodeType.STANDBY))
                needUpdate = true;
            if (!compareToStatus(this.clusterState.getWitnesses(), witnesses, NodeType.WITNESS))
                needUpdate = true;
            if (!compareToStatus(this.clusterState.getIdleNodes(), idles, NodeType.IDLE))
                needUpdate = true;
        }
        if (needUpdate) {
            Notifications.CLUSTER_STATE_MISMATCH.addSubjectParams(new String[] { this.env.getClusterName() }).send();
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        }
    }

    public void doManualPromotion(EfmStatus status) {
        if (status == null)
            status = getStatusBeforePromotion();
        if (status == null)
            return;
        String currentMasterHost = null;
        for (NodeStatus ns : status.getNodeStatusSet()) {
            if (NodeType.MASTER.getName().equals(ns.getType()))
                currentMasterHost = ns.getIpAddr();
        }
        LOGGER.log(Level.INFO, "Starting manual promotion.");
        try {
            Address mstrAddr = this.clusterState.getMaster();
            if (mstrAddr == null && currentMasterHost != null) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { String.format("A master node Address was not found in the cluster state, but the node at %s is reporting that it is a master agent.", new Object[] { currentMasterHost }) }).send();
                LOGGER.log(Level.SEVERE, "A master node Address was not found in the cluster state, but the node at {0} is reporting that it is a master agent. To be safe, will treat that node as a master agent and have it stop monitoring its database. Locating node.", currentMasterHost);
                LOGGER.finest("doManualPromotion unknown master agent found before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("doManualPromotion unknown master agent found has lock");
                    for (Address target : this.channel.getView().getMembers()) {
                        if (currentMasterHost.equals(ClusterUtils.getHost((Channel)this.channel, target, this.clusterState))) {
                            mstrAddr = target;
                            break;
                        }
                    }
                }
            }
            if (mstrAddr != null) {
                if (mstrAddr.equals(this.channel.getAddress())) {
                    stopMonitoringForManualPromotion();
                } else {
                    LOGGER.log(Level.WARNING, "Telling master node {0} at {1} to stop monitoring database.", new Object[] { mstrAddr, ip(mstrAddr) });
                    this.channel.send(mstrAddr, "stop_monitoring_for_manual_promotion");
                }
                return;
            }
            LOGGER.log(Level.WARNING, "Did not find a master node Address in the cluster.");
            StandbyInfo aboutToBeMaster = chooseStandbyForPromotion();
            if (aboutToBeMaster == null) {
                LOGGER.log(Level.SEVERE, "No standby nodes could be found.");
                return;
            }
            LOGGER.log(Level.WARNING, "Will tell standby at {0} to promote.", aboutToBeMaster.getHost());
            this.channel.send(null, "idle_node_drop_vip");
            tellStandbyToPromote(aboutToBeMaster.getAddress(), "");
        } catch (NoStandbysException nse) {
            LOGGER.severe("Could not find an available standby for failover.");
            Notifications.PROMOTE_BUT_NO_STANDBY.addBodyParams(new String[] { this.env.getClusterName() }).send();
            tellMasterSwitchoverCanceled();
        } catch (NotEnoughStandbysException nese) {
            LOGGER.severe("There are not enough standbys for failover.");
            Notifications.PROMOTE_BUT_NOT_ENOUGH_STANDBYS.addBodyParams(new String[] { this.env.getClusterName() }).send();
            tellMasterSwitchoverCanceled();
        } catch (Exception e) {
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
            LOGGER.log(Level.SEVERE, "There was an unexpected problem sending promote signal: {0}", e.toString());
            tellMasterSwitchoverCanceled();
        }
    }

    public void doSwitchover() {
        EfmStatus status = getStatusBeforePromotion();
        if (status == null)
            return;
        String recoveryConfText = null;
        if (getNodeType() == NodeType.STANDBY) {
            recoveryConfText = EfmStandby.readRecoveryConf();
        } else {
            Address standbyAddr;
            LOGGER.finest("doSwitchover1 before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("doSwitchover1 has lock");
                Address[] standbyAddrs = this.clusterState.getStandbys();
                if (standbyAddrs.length > 0) {
                    standbyAddr = standbyAddrs[0];
                } else {
                    LOGGER.log(Level.SEVERE, "Cannot find any standby nodes in cluster state.");
                    return;
                }
            }
            RspList<String> rspList = ClusterUtils.getObjectFromNode(standbyAddr, this.dispatcher, "get_recovery_conf_text");
            if (rspList.isEmpty()) {
                LOGGER.log(Level.SEVERE, "Could not receive recovery conf text from {0}.", standbyAddr);
            } else {
                recoveryConfText = (String)rspList.getFirst();
            }
        }
        if (recoveryConfText == null) {
            LOGGER.log(Level.SEVERE, "Could not read the recovery.conf text to send to the original master node. Cannot proceed with switchover.");
            return;
        }
        if (getNodeType() == NodeType.MASTER) {
            prepareForSwitchover(recoveryConfText);
        } else {
            Address masterAddr;
            LOGGER.finest("doSwitchover2 before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("doSwitchover2 has lock");
                masterAddr = this.clusterState.getMaster();
            }
            if (masterAddr == null) {
                LOGGER.log(Level.SEVERE, "Cannot find master in cluster state.");
                return;
            }
            try {
                this.channel.send(masterAddr, "recovery_conf_text_" + recoveryConfText);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot send recovery.conf information to master: {0}", e.toString());
                return;
            }
        }
        doManualPromotion(status);
    }

    public int getCurrentNodeCount() {
        return this.channel.getView().getMembers().size();
    }

    public String checkClusterStatus() {
        try {
            LOGGER.log(Level.INFO, "Sending status message to all nodes: {0}", "node_status");
            LOGGER.finest("checkClusterStatus before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("checkClusterStatus has lock");
                // 执行命令
                return ClusterUtils.sendStatusRequestToNodes(this.dispatcher, this.clusterState).marshal();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error sending status request", e);
            return null;
        }
    }

    public void addNode(String address, int priority) {
        if (isCoordinator()) {
            synchronized (this.clusterStateLock) {
                this.clusterState.addAllowedNode(address);
                if (priority != 0) {
                    this.clusterState.setNodePriority(address, priority);
                } else {
                    for (Address addr : this.clusterState.getStandbys()) {
                        String targetHost = ClusterUtils.getHost((Channel)this.channel, addr, this.clusterState);
                        if (address.equals(targetHost)) {
                            LOGGER.log(Level.FINE, "Standby at {0} already in cluster. Adding to end of priority list.", address);
                            this.clusterState.setNodePriority(address, (this.clusterState.getFoPriority()).length + 1);
                            break;
                        }
                    }
                }
            }
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        } else {
            String payload = SubCommand.ADD_NODE.name() + "|" + address + "|" + priority;
            try {
                LOGGER.log(Level.FINE, "Sending add-node information to coordinator");
                ClusterUtils.sendMessageToCoordinator((Channel)this.channel, payload);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not send add-node message to coordinator: " + e.toString());
            }
        }
    }

    public void allowNode(String address) {
        if (isCoordinator()) {
            synchronized (this.clusterStateLock) {
                this.clusterState.addAllowedNode(address);
            }
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        } else {
            String payload = SubCommand.ALLOW_NODE.name() + "|" + address;
            try {
                LOGGER.log(Level.FINE, "Sending allow-node information to coordinator");
                ClusterUtils.sendMessageToCoordinator((Channel)this.channel, payload);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not send allow-node message to coordinator: " + e.toString());
            }
        }
    }

    public void removeNode(String address) {
        if (isCoordinator()) {
            synchronized (this.clusterStateLock) {
                this.clusterState.removeAllowedNode(address);
            }
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        } else {
            String payload = SubCommand.REMOVE_NODE.name() + "|" + address;
            try {
                LOGGER.log(Level.FINE, "Sending remove-node information to coordinator");
                ClusterUtils.sendMessageToCoordinator((Channel)this.channel, payload);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not send remove-node message to coordinator: " + e.toString());
            }
        }
    }

    public void disallowNode(String address) {
        if (isCoordinator()) {
            synchronized (this.clusterStateLock) {
                this.clusterState.removeAllowedNode(address);
            }
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        } else {
            String payload = SubCommand.DISALLOW_NODE.name() + "|" + address;
            try {
                LOGGER.log(Level.FINE, "Sending disallow-node information to coordinator");
                ClusterUtils.sendMessageToCoordinator((Channel)this.channel, payload);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not send remove-node message to coordinator: " + e.toString());
            }
        }
    }

    public void setPriority(String address, int priority) {
        if (isCoordinator()) {
            synchronized (this.clusterStateLock) {
                this.clusterState.setNodePriority(address, priority);
            }
            try {
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + e.toString());
            }
        } else {
            String payload = SubCommand.SET_PRIORITY.name() + "|" + address + "|" + priority;
            try {
                LOGGER.log(Level.FINE, "Sending set-priority information to coordinator");
                ClusterUtils.sendMessageToCoordinator((Channel)this.channel, payload);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not send set-priority message to coordinator: " + e.toString());
            }
        }
    }

    public boolean authenticateHCText(String target) {
        if (this.controllerAuthText == null)
            throw new IllegalStateException("Controller auth text has not been set.");
        return this.controllerAuthText.equals(target);
    }

    void handleDbFailure(Address address) {
        String host;
        boolean isMasterOrPromoting;
        LOGGER.fine("Entering handleDbFailure.");
        LOGGER.finest("handleDbFailure before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("handleDbFailure has lock");
            host = ClusterUtils.getHost((Channel)this.channel, address, this.clusterState);
            isMasterOrPromoting = (address.equals(this.clusterState.getMaster()) || address.equals(this.clusterState.getPromoting()));
        }
        addToClusterStatus(NodeType.IDLE, address);
        if (isMasterOrPromoting) {
            LOGGER.log(Level.WARNING, "Node {0} at host {1} has declared database failure. Proceeding with promotion checks.", new Object[] { address, host });
            attemptPromotion(host);
        } else {
            LOGGER.log(Level.WARNING, "Node {0} at host {1} has declared database failure.", new Object[] { address, host });
            try {
                if (nodeStillUp(PingType.DB_PING, host)) {
                    Notifications.STANDBY_AGENT_CANNOT_REACH_DB.addSubjectParams(new String[] { this.env.getClusterName() }).send();
                    return;
                }
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, "Could not determine whether or not the database at host {0} is running", host);
            }
            Notifications.STANDBY_DB_FAILED.addSubjectParams(new String[] { this.env.getClusterName() }).send();
        }
    }

    void commonShutdown() {
        LOGGER.info("Starting shutdown.");
        this.state = State.SHUTDOWN;
        int tasks = WorkQueue.shutdown().size();
        if (tasks > 0)
            LOGGER.log(Level.WARNING, "{0} tasks not run in queue during shutdown.", Integer.valueOf(tasks));
        this.stateVerifier.shutdown();
        ClusterSizeUpdater.shutdown();
        LockFile.getInstance().shutdownLockMonitor();
        LicenseManager.getLicenseManager().shutdown();
        VipMonitor.getInstance().stopMonitoring();
        if (this.adminServer != null)
            this.adminServer.shutdown();
        if (this.channel != null)
            this.channel.close();
        if (this.dispatcher != null)
            this.dispatcher.stop();
    }

    boolean doStartup() throws Exception {
        if (this.env.isWitness()) {
            System.out.println("Starting witness.");
        } else {
            System.out.println("Starting agent.");
            if (!determineStartupAgentType())
                return false;
        }
        if (!this.env.isVipEnabled()) {
            System.err.println("Warning: virtualIp property is not set, starting EFM with VIP support disabled.");
        } else if (!checkVipUse()) {
            return false;
        }
        if (!this.env.autoFailoverOn())
            System.out.println("Warning: auto.failover property is set to false. Notifications regarding failover will be sent, but failover will not be performed by EFM if needed.");
        if (!ClusterUtils.pingHost(this.env.getPingServer())) {
            System.err.println("Could not reach the address " + this.env.getPingServer() + ". Please check the value of your " + EfmProps.EFM_PING_SERVER.getPropName() + " property.");
            return false;
        }
        // server端
        this.adminServer = AdminServer.getAdminServer();
        // server端启动
        if (!this.adminServer.start(this)) {
            System.err.println("Error starting admin server. See log for details: " + this.env.getLogFileLocation());
            return false;
        }
        LOGGER.log(Level.FINE, "Checking if port {0} is already in use.", Integer.valueOf(this.env.getBindingPort()));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.env.getBindingPort());
        } catch (BindException be) {
            System.err.println("Port " + this.env.getBindingPort() + " is already" + "in use. Please choose another binding address for this node.");
            return false;
        } finally {
            if (serverSocket != null)
                serverSocket.close();
        }
        if (!LicenseManager.getLicenseManager().monitorLicense(this)) {
            System.err.println("You must have a valid license to run EFM. Exiting.");
            return false;
        }
        LOGGER.info("Starting");
        // 创建channel
        this.channel = this.env.createJChannel(this);
        // 设置接收者
        this.channel.setReceiver((Receiver)this);
        // 创建发送者
        this.dispatcher = new MessageDispatcher((Channel)this.channel, (MessageListener)this, (MembershipListener)this, this);
        // 连接集群
        this.channel.connect(this.env.getClusterName(), null, 10000L);
        if (isCoordinator()) {
            Address me = this.channel.getAddress();
            synchronized (this.clusterStateLock) {
                this.clusterState.addAllowedNode(this.env.getBindingAddress());
                switch (getNodeType()) {
                    case DB_PING:
                        this.clusterState.setMaster(me);
                        break;
                    case VIP_PING:
                        this.clusterState.addStandby(me, (Channel)this.channel);
                        break;
                    case null:
                        this.clusterState.addIdleNode(me, (Channel)this.channel);
                        break;
                    case null:
                        this.clusterState.addWitness(me);
                        break;
                    default:
                        throw new AssertionError("Unknown node type: " + getNodeType());
                }
            }
            LOGGER.info("This node is coordinator. Not checking properties.");
            if (this.env.autoAllowHosts()) {
                LOGGER.log(Level.INFO, "Adding hosts from .nodes file to allowed host list.");
                synchronized (this.clusterStateLock) {
                    for (String host : this.env.getInitialNodeAddresses())
                        this.clusterState.addAllowedNode(host);
                }
            }
            synchronized (this.clusterStateLock) {
                this.clusterStateInitialized = true;
                updateNodesFile(this.channel.getView().getMembers());
            }
        } else {
            Address existingMaster, existingPromoting;
            synchronized (this.clusterStateLock) {
                existingMaster = this.clusterState.getMaster();
                existingPromoting = this.clusterState.getPromoting();
            }
            if (getNodeType() == NodeType.MASTER) {
                if (existingMaster != null) {
                    System.err.println("There is already a master node in this cluster: " + existingMaster + " at " + ip(existingMaster) + ". Starting shutdown process.");
                    shutdown();
                    return false;
                }
                if (existingPromoting != null) {
                    System.err.println("There is already a promoting node in this cluster: " + existingPromoting + " at " + ip(existingPromoting) + ". Starting shutdown process.");
                    shutdown();
                    return false;
                }
            }
            if (!comparePropsWithCoordinator()) {
                shutdown();
                return false;
            }
            if (!canPingAllDBs())
                return false;
            if (getNodeType() == NodeType.IDLE && (existingMaster != null || existingPromoting != null)) {
                System.out.println("There is already a master/promoting node in the cluster. Verifying that the local database has a recovery.conf file.");
                ProcessResult result = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.RECOVERY_CONF_EXISTS + " " + this.env.getClusterName() });
                if (result.getExitValue() != 0) {
                    ProcessResult writeResult = ExecUtil.performExec(new String[] { this.env.getUserSudoCommand() + " " + this.env.getDBFunctionsScript() + " " + SudoFunctions.WRITE_RECOVERY_CONF + " " + this.env.getClusterName() });
                    if (writeResult.getExitValue() != 0) {
                        System.err.println("There was an error making sure that this node has a recovery.conf file and cannot be started as a second master.");
                        return false;
                    }
                }
            }
            this.state = State.RUNNING;
            ClusterUtils.sendMessageToCoordinator((Channel)this.channel, getNodeType());
        }
        if (getNodeType() == NodeType.STANDBY && !this.env.isPromotable()) {
            System.out.println("Removing this standby's address from the failover priority list.");
            setPriority(this.env.getBindingAddress(), 0);
        }
        return true;
    }

    public String[] getAllowedIPs() {
        LOGGER.finest("getAllowedIPs before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("getAllowedIPs has lock");
            return this.clusterState.getAllowed();
        }
    }

    public void updateStableSize() {
        LOGGER.finest("updateStableSize before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("updateStableSize has lock");
            this.stableSize = this.channel.getView().size();
            LOGGER.log(Level.INFO, "stable cluster size now {0}", Float.valueOf(this.stableSize));
        }
    }

    void addToClusterStatus(NodeType type, Address node) {
        NodeType removedType = this.clusterState.removeAddress(node, (Channel)this.channel);
        LOGGER.finest("addToClusterStatus before lock");
        synchronized (this.clusterStateLock) {
            Address promoting;
            LOGGER.finest("addToClusterStatus has lock");
            if (this.clusterState.containsNodeAtType(node, type))
                return;
            if (removedType != null && LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Removed {0} of type {1} from cluster state. Adding it back in as type {2}.", new Object[] { node, removedType, type });
            switch (type) {
                case DB_PING:
                    promoting = this.clusterState.getPromoting();
                    if (promoting != null) {
                        LOGGER.log(Level.INFO, "Replacing promoting node {0} with master {1}.", new Object[] { promoting, node });
                        this.clusterState.setPromoting(null);
                    }
                    this.clusterState.setMaster(node);
                    break;
                case VIP_PING:
                    this.clusterState.addStandby(node, (Channel)this.channel);
                    break;
                case null:
                    this.clusterState.addWitness(node);
                    break;
                case null:
                    this.clusterState.addIdleNode(node, (Channel)this.channel);
                    break;
                case null:
                    this.clusterState.setPromoting(node);
                    break;
                default:
                    LOGGER.severe("Unknown node type to add: " + type);
                    break;
            }
        }
        try {
            this.channel.send(null, "load_shared_state");
        } catch (Exception e) {
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
            LOGGER.severe("Could not tell nodes to update status: " + e.toString());
        }
        boolean needToCheck = false;
        if (removedType == null && type != NodeType.WITNESS && type != NodeType.IDLE) {
            LOGGER.info("A new database node has joined the cluster. All nodes will verify that they can connect to the database.");
            needToCheck = true;
        } else if (removedType == NodeType.IDLE && type != NodeType.IDLE) {
            LOGGER.info("An existing agent is no longer IDLE. All nodes will verify that they can connect to the database.");
            needToCheck = true;
        }
        if (needToCheck) {
            final String host = ClusterUtils.getHost((Channel)this.channel, node, this.clusterState);
            WorkQueue.executeTask(new Runnable() {
                public void run() {
                    try {
                        EfmNode.this.channel.send(null, "ping_new_db_" + host);
                    } catch (Exception e) {
                        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                        EfmNode.LOGGER.severe("Could not tell nodes to ping new database: " + e.toString());
                    }
                }
            });
        }
    }

    StandbyInfo chooseStandbyForPromotion() throws NoStandbysException, NotEnoughStandbysException {
        Address[] allStandbys;
        String[] foPriority;
        LOGGER.finest("chooseStandbyForPromotion before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("chooseStandbyForPromotion has lock");
            allStandbys = this.clusterState.getStandbys();
            foPriority = this.clusterState.getFoPriority();
        }
        LOGGER.log(Level.FINE, "Creating list of standbys to consider for promotion.");
        List<StandbyInfo> promotableStandbys = new ArrayList<StandbyInfo>();
        try {
            for (Address address : allStandbys) {
                String host;
                LOGGER.finest("chooseStandbyForPromotion2 before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("chooseStandbyForPromotion2 has lock");
                    host = ClusterUtils.getHost((Channel)this.channel, address, this.clusterState);
                }
                if (host == null) {
                    LOGGER.log(Level.SEVERE, "Could not find host address for {0}. Will not consider this node for promotion.", address);
                } else if (!hostInPriorityList(host, foPriority)) {
                    LOGGER.log(Level.FINE, "Skipping standby {0} because not in priority list.", host);
                } else {
                    StandbyInfo sInfo = new StandbyInfo(address, host);
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, "Adding standby info: {0}", sInfo);
                    promotableStandbys.add(sInfo);
                }
            }
        } catch (PasswordDecryptException pde) {
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { pde.toString() }).send();
            LOGGER.log(Level.SEVERE, "Can not decrypt db password to contact standby databases: {0}", (Throwable)pde);
            return null;
        }
        int standbysSize = promotableStandbys.size();
        if (standbysSize == 0) {
            LOGGER.log(Level.SEVERE, "There are no standbys in cluster state to promote.");
            throw new NoStandbysException();
        }
        if (this.env.getMinStandbys() >= allStandbys.length) {
            LOGGER.log(Level.SEVERE, "There are not enough standbys in cluster state to promote.");
            throw new NotEnoughStandbysException();
        }
        if (standbysSize == 1) {
            LOGGER.fine("Returning only standby.");
            return promotableStandbys.get(0);
        }
        return DBUtils.chooseFailoverNode(promotableStandbys, foPriority);
    }

    private void handleMultipleMasterOrPromoting(Set<NodeStatus> nodes) {
        boolean promotingFound = false;
        for (NodeStatus node : nodes) {
            if (NodeType.PROMOTING.getName().equals(node.getType())) {
                promotingFound = true;
                break;
            }
        }
        if (promotingFound) {
            if (this.clusterState.getMaster() != null) {
                LOGGER.log(Level.SEVERE, "Unexpected promoting node found. Removing master {0} from internal cluster state.", this.clusterState.getMaster());
                this.clusterState.setMaster(null);
            }
            for (Iterator<NodeStatus> iter = nodes.iterator(); iter.hasNext(); ) {
                final NodeStatus ns = iter.next();
                if (NodeType.MASTER.getName().equals(ns.getType())) {
                    iter.remove();
                    WorkQueue.executeTask(new Runnable() {
                        public void run() {
                            EfmNode.LOGGER.log(Level.SEVERE, "Because promoting node(s) found, telling node {0} to fence self off", ns.getIpAddr());
                            try {
                                EfmNode.this.channel.send(ns.getAddress(), "fence_database");
                            } catch (Exception e) {
                                EfmNode.LOGGER.log(Level.SEVERE, "Could not send fence message to node: {0}", e.toString());
                            }
                        }
                    });
                }
            }
            if (nodes.size() == 1) {
                final NodeStatus ns = nodes.iterator().next();
                LOGGER.log(Level.WARNING, "Setting only promoting node in the cluster state. {0} at {1}.", new Object[] { ns.getAddress(), ns.getIpAddr() });
                this.clusterState.setMaster(null);
                this.clusterState.setPromoting(ns.getAddress());
                return;
            }
        }
        NodeStatus nodeToKeep = null;
        long shortestTime = System.currentTimeMillis();
        for (NodeStatus ns : nodes) {
            if (ns.getTimeAsNodeType() < shortestTime) {
                nodeToKeep = ns;
                shortestTime = ns.getTimeAsNodeType();
            }
        }
        for (NodeStatus ns : nodes) {
            if (ns.equals(nodeToKeep))
                continue;
            final NodeStatus fenceOff = ns;
            if (nodeToKeep == null)
                throw new AssertionError("Cannot have null node picked to remain. (0)");
            final String nodeToKeepAddr = nodeToKeep.getIpAddr();
            WorkQueue.executeTask(new Runnable() {
                public void run() {
                    EfmNode.LOGGER.log(Level.SEVERE, "Telling node {0} to fence itself off because there is a newer master/promoting node {1}", new Object[] { this.val$fenceOff.getIpAddr(), this.val$nodeToKeepAddr });
                    try {
                        EfmNode.this.channel.send(fenceOff.getAddress(), "fence_database");
                    } catch (Exception e) {
                        EfmNode.LOGGER.log(Level.SEVERE, "Could not send fence message to node: {0}", e.toString());
                    }
                }
            });
        }
        this.clusterState.setMaster(null);
        this.clusterState.setPromoting(null);
        if (nodeToKeep == null)
            throw new AssertionError("Cannot have null node picked to remain. (1)");
        if (NodeType.MASTER.getName().equals(nodeToKeep.getType())) {
            this.clusterState.setMaster(nodeToKeep.getAddress());
        } else {
            this.clusterState.setPromoting(nodeToKeep.getAddress());
        }
    }

    private boolean compareToStatus(Address[] csAddresses, Set<Address> addrs, NodeType type) {
        boolean needUpdate = false;
        if (csAddresses.length != addrs.size()) {
            needUpdate = true;
        } else {
            for (Address a : csAddresses) {
                if (!addrs.contains(a))
                    needUpdate = true;
            }
        }
        if (needUpdate) {
            LOGGER.log(Level.SEVERE, "Nodes of type {0} were discovered in the cluster that do not match the internal state. Internal state: {1}, found in cluster: {2}.", new Object[] { type.getName(), Arrays.toString((Object[])csAddresses), addrs });
            switch (type) {
                case VIP_PING:
                    this.clusterState.clearStandbys();
                    for (Address addr : addrs)
                        this.clusterState.addStandby(addr, (Channel)this.channel);
                    break;
                case null:
                    this.clusterState.clearWitnesses();
                    for (Address addr : addrs)
                        this.clusterState.addWitness(addr);
                    break;
                case null:
                    this.clusterState.clearIdles();
                    for (Address addr : addrs)
                        this.clusterState.addIdleNode(addr, (Channel)this.channel);
                    break;
                default:
                    throw new AssertionError("Method cannot be used to compare nodes of type " + type.getName());
            }
        }
        return !needUpdate;
    }

    private boolean hostInPriorityList(String host, String[] foPriority) {
        for (String target : foPriority) {
            if (host.equals(target))
                return true;
        }
        return false;
    }

    private EfmStatus getStatusBeforePromotion() {
        EfmStatus retVal;
        LOGGER.finest("getStatusBeforePromotion before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("getStatusBeforePromotion has lock");
            try {
                retVal = ClusterUtils.sendStatusRequestToNodes(this.dispatcher, this.clusterState);
            } catch (Exception e) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                LOGGER.severe("Could not check current nodes to see if any are currently in promoting state. Could not continue. Error: " + e.toString());
                return null;
            }
        }
        for (NodeStatus ns : retVal.getNodeStatusSet()) {
            if (NodeType.PROMOTING.getName().equals(ns.getType())) {
                Notifications.PROMOTE_BUT_ALREADY_PROMOTING.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { ns.getIpAddr() }).send();
                LOGGER.severe("There is already a node undergoing promotion. Cannot promote another standby and create more than one master node in the cluster.");
                return null;
            }
        }
        return retVal;
    }

    private void checkNewDBPing(String host) {
        try {
            DBMonitor monitor = DBUtils.createMonitor(host);
            // pingdb,即执行一次 select version()
            if (!monitor.checkOnce(true, true)) {
                LOGGER.log(Level.SEVERE, "Unable to ping database of new or resumed node at {0}. Failover may not function properly until this is fixed.", host);
                Notifications.PING_NEW_DB_FAIL.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { host }).send();
            }
        } catch (PasswordDecryptException e) {
            e.printStackTrace();
        }
    }

    String getLastPromoted() {
        synchronized (this.lastPromotedLock) {
            return this.lastPromoted;
        }
    }

    void setLastPromoted(String lastPromoted) {
        synchronized (this.lastPromotedLock) {
            this.lastPromoted = lastPromoted;
            this.timeLastPromoted = System.currentTimeMillis();
        }
        LOGGER.log(Level.INFO, "Last promoted address set to {0}", lastPromoted);
    }

    long getTimeLastPromoted() {
        synchronized (this.lastPromotedLock) {
            return this.timeLastPromoted;
        }
    }

    private void updateStableClusterSize(View view, List<Address> members) {
        int size = view.size();
        if (size == this.stableSize)
            return;
        LOGGER.log(Level.INFO, "Cluster size now {0}, was {1}.", new Object[] { Integer.valueOf(size), Float.valueOf(this.stableSize) });
        if (size > this.stableSize) {
            LOGGER.log(Level.FINE, "Increasing stable cluster size to {0}.", Integer.valueOf(size));
            ClusterSizeUpdater.stopTimer();
            this.stableSize = size;
        } else {
            Set<Address> addrs = this.clusterState.getNonMasterAddresses();
            addrs.add(this.clusterState.getMaster());
            boolean shouldStartTimer = false;
            for (Address addr : addrs) {
                if (members.contains(addr))
                    continue;
                if (this.suspected.contains(addr)) {
                    shouldStartTimer = true;
                    if (nodeHasDbRunning(addr)) {
                        LOGGER.warning("Database can still be reached at missing node. Updating stable cluster size to avoid false isolation case.");
                        this.stableSize--;
                    }
                }
            }
            if (shouldStartTimer) {
                LOGGER.info("At least one node was suspect before leaving. Starting timer to update known cluster size.");
                ClusterSizeUpdater.startTimer(this);
            } else {
                LOGGER.log(Level.INFO, "Decreasing stable cluster size to {0}", Integer.valueOf(size));
                ClusterSizeUpdater.stopTimer();
                this.stableSize = size;
            }
        }
    }

    private boolean nodeHasDbRunning(Address address) {
        boolean addressHasDb = false;
        if (address.equals(this.clusterState.getMaster())) {
            addressHasDb = true;
        } else {
            for (Address s : this.clusterState.getStandbys()) {
                if (address.equals(s)) {
                    addressHasDb = true;
                    break;
                }
            }
        }
        String addrString = ClusterUtils.getHost((Channel)this.channel, address, this.clusterState);
        try {
            return (addressHasDb && nodeStillUp(PingType.DB_PING, addrString));
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Could not determine whether {0} was still running or not based on database ping.", addrString);
            return false;
        }
    }

    private boolean handleMissingNonMasterNodes(List<Address> members) {
        boolean retVal = false;
        StringBuilder sb = new StringBuilder();
        LOGGER.finest("handleMissingNonMasterNodes before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("handleMissingNonMasterNodes has lock");
            for (Address addr : this.clusterState.getNonMasterAddresses()) {
                final String host;
                if (members.contains(addr))
                    continue;
                retVal = true;
                NodeType type = this.clusterState.removeAddress(addr, (Channel)this.channel);
                sb.append(type.getName()).append(":").append(addr).append(" ");
                if (!this.suspected.contains(addr))
                    continue;
                LOGGER.finest("handleMissingNonMasterNodes (internal) before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("handleMissingNonMasterNodes (internal) has lock");
                    host = ClusterUtils.getHost((Channel)this.channel, addr, this.clusterState);
                }
                if (type == NodeType.STANDBY) {
                    LOGGER.finest("**** submitting task to check standby failure");
                    WorkQueue.executeTask(new Runnable() {
                        public void run() {
                            EfmNode.LOGGER.finest("**** running task to check standby failure");
                            try {
                                if (EfmNode.this.nodeStillUp(EfmNode.PingType.DB_PING, host)) {
                                    Notifications.NO_STANDBY_BUT_DB_UP.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { this.val$host }).send();
                                } else {
                                    Notifications.NO_STANDBY.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { this.val$host }).send();
                                }
                            } catch (InterruptedException e) {
                                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                                EfmNode.LOGGER.warning(e.toString());
                            }
                        }
                    });
                    continue;
                }
                if (type == NodeType.WITNESS) {
                    Notifications.WITNESS_FAIL.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { host }).send();
                    continue;
                }
                Notifications.IDLE_FAIL.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { host }).send();
            }
        }
        String missingList = sb.toString().trim();
        if (!missingList.isEmpty())
            LOGGER.log(Level.WARNING, "Missing nodes: {0}", missingList);
        return retVal;
    }

    private boolean comparePropsWithCoordinator() {
        Address coordinator = this.channel.getView().getMembers().get(0);
        System.out.println("Checking shared properties with " + coordinator + " at " + ip(coordinator));
        LOGGER.log(Level.INFO, "Checking shared properties with {0} at {1}.", new Object[] { coordinator, ip(coordinator) });
        RspList<Properties> rspList = ClusterUtils.getObjectFromNode(coordinator, this.dispatcher, "get_properties");
        if (rspList.isEmpty()) {
            LOGGER.severe("Received no response in rsp list");
            return false;
        }
        Properties localProps = this.env.getProperties();
        Properties remoteProps = (Properties)rspList.getFirst();
        if (remoteProps == null) {
            System.err.println("Could not retrieve properties from " + ip(coordinator));
            return false;
        }
        boolean propsMatch = true;
        for (EfmProps prop : EfmProps.values()) {
            if (prop.isShared()) {
                String localProp = localProps.getProperty(prop.getPropName());
                String remoteProp = remoteProps.getProperty(prop.getPropName());
                if (localProp == null)
                    localProp = "";
                if (remoteProp == null)
                    remoteProp = "";
                if (prop == EfmProps.EFM_VIRT_IP) {
                    try {
                        InetAddress localAddr = InetAddress.getByName(localProp);
                        InetAddress remoteAddr = InetAddress.getByName(remoteProp);
                        if (!localAddr.equals(remoteAddr)) {
                            String message = String.format("Address '%s' for property '%s' does not match value '%s'.", new Object[] { localProp, prop.getPropName(), remoteProp });
                            System.err.println(message);
                            LOGGER.severe(message);
                            propsMatch = false;
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                } else if (!localProp.trim().equals(remoteProp.trim())) {
                    String message = String.format("Value '%s' for property '%s' must match value '%s'.", new Object[] { localProp, prop.getPropName(), remoteProp });
                    System.err.println(message);
                    LOGGER.severe(message);
                    propsMatch = false;
                }
            }
        }
        return propsMatch;
    }

    public boolean checkControllerAuthFile() {
        File authFile = new File(this.env.getAuthFileLocation());
        if (!authFile.exists()) {
            System.err.println("Could not find file " + authFile.getName() + " used to authorize requests from the 'efm' command.");
            return false;
        }
        String checkCommand = this.env.getRootFunctionsScript() + " " + SudoFunctions.CHECK_AUTH.toString() + " " + this.env.getClusterName();
        ProcessResult checkResult = ExecUtil.performExec(false, new String[] { checkCommand });
        if (checkResult.getExitValue() != 0) {
            System.err.println(authFile.getName() + " must be owned by efm in group efm with 640 permissions.");
            String error = checkResult.getErrorOut();
            if (error != null && !error.isEmpty())
                System.err.println("Error: " + error);
            return false;
        }
        if (authFile.canRead()) {
            BufferedReader reader = null;
            try {
                FileInputStream stream = new FileInputStream(authFile);
                InputStreamReader sReader = new InputStreamReader(stream, "UTF-8");
                reader = new BufferedReader(sReader);
                String line = reader.readLine();
                if (line == null) {
                    System.err.println("No text in auth file");
                    this.controllerAuthText = "";
                    return false;
                }
                this.controllerAuthText = line.trim();
                return true;
            } catch (IOException e) {
                System.err.println("Could not read authorization text: " + e.toString());
                return false;
            } finally {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        String err = e.getMessage();
                        if (err == null || err.isEmpty())
                            err = e.toString();
                        System.err.println("Problem closing auth file reader: " + err);
                    }
            }
        }
        System.err.println("Could not read controller auth file.");
        return false;
    }

    private boolean canPingAllDBs() {
        List<String> couldNotConnect;
        List<Address> dbAddresses = new ArrayList<Address>();
        List<Address> nonDbAddresses = new ArrayList<Address>();
        LOGGER.finest("canPingAllDBs before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("canPingAllDBs has lock");
            if (this.clusterState.getMaster() != null)
                dbAddresses.add(this.clusterState.getMaster());
            Collections.addAll(dbAddresses, this.clusterState.getStandbys());
            Collections.addAll(nonDbAddresses, this.clusterState.getWitnesses());
            Collections.addAll(nonDbAddresses, this.clusterState.getIdleNodes());
        }
        if (dbAddresses.isEmpty()) {
            System.out.println("No other databases exist to test connections.");
            return true;
        }
        LOGGER.info("Testing remote database connections.");
        System.out.println("Testing remote database connections.");
        Address me = this.channel.getAddress();
        List<String> dbs = new ArrayList<String>();
        for (Address addr : dbAddresses) {
            String host;
            if (addr.equals(me))
                continue;
            LOGGER.finest("canPingAllDBs gethost1 before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("canPingAllDBs gethost1 has lock");
                host = ClusterUtils.getHost((Channel)this.channel, addr, this.clusterState);
            }
            if (host != null) {
                dbs.add(host);
                continue;
            }
            String msg = "Could not determine IP address for member " + addr + ". Make sure all node addresses are present in the cluster's .nodes file.";
            LOGGER.warning(msg);
            System.err.println(msg);
            return false;
        }
        for (Address addr : nonDbAddresses) {
            if (addr.equals(me))
                continue;
            LOGGER.finest("canPingAllDBs gethost2 before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("canPingAllDBs gethost2 has lock");
                if (ClusterUtils.getHost((Channel)this.channel, addr, this.clusterState) == null) {
                    String msg = "Could not determine IP address for member " + addr + ". Make sure all node addresses are present in the cluster's .nodes file.";
                    LOGGER.warning(msg);
                    System.err.println(msg);
                    return false;
                }
            }
        }
        try {
            // ping所有的db
            couldNotConnect = DBUtils.checkConnections(dbs);
        } catch (PasswordDecryptException e) {
            System.err.println("Unexpected exception trying to verify database connections: " + e.toString());
            return false;
        }
        if (!couldNotConnect.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String db : couldNotConnect)
                sb.append(" ").append(db);
            System.err.println("Could not connect to the following databases:" + sb.toString());
            return false;
        }
        return true;
    }

    private void handleViewChange(View view) {
        boolean coordinatorWasSuspect;
        Address originalMaster;
        List<Address> members = view.getMembers();
        int viewSize = view.size();
        Address me = this.channel.getAddress();
        Address coordinator = members.get(0);
        boolean isMaster = getNodeType().equals(NodeType.MASTER);
        boolean isCoordinator = isCoordinator(view);
        if (isCoordinator)
            this.stateVerifier.scheduleVerification();
        LOGGER.finest("handleViewChange before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("handleViewChange has lock");
            coordinatorWasSuspect = this.suspected.contains(coordinator);
            if (LOGGER.isLoggable(Level.FINE)) {
                String host;
                LOGGER.finest("handleViewChange gethost before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("handleViewChange gethost has lock");
                    host = ClusterUtils.getHost((Channel)this.channel, coordinator, this.clusterState);
                }
                LOGGER.log(Level.FINE, "Current coordinator: {0} at {1}. Address suspect? {2}", new Object[] { coordinator, host, Boolean.valueOf(coordinatorWasSuspect) });
                StringBuilder builder = new StringBuilder();
                builder.append("\nview:");
                for (Address a : members)
                    builder.append("\t").append(a).append("\n");
                LOGGER.fine(builder.toString());
            }
            if (this.clusterStateInitialized)
                updateNodesFile(members);
            boolean bool = false;
            for (Address address : members) {
                if (this.suspected.remove(address)) {
                    LOGGER.log(Level.INFO, "Node was suspect, but has returned to the view: {0}", address);
                    if (isMaster)
                        bool = true;
                }
            }
            if (bool) {
                LOGGER.finest("***** submitting checkForReplacementMaster");
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        EfmNode.LOGGER.finest("***** starting checkForReplacementMaster");
                        EfmNode.this.checkForReplacementMaster(5);
                        EfmNode.LOGGER.finest("***** ended checkForReplacementMaster");
                    }
                });
            }
            originalMaster = this.clusterState.getMaster();
            if (originalMaster == null) {
                originalMaster = this.clusterState.getPromoting();
                if (originalMaster != null)
                    LOGGER.log(Level.INFO, "No master in cluster; checking for loss of promoting node: {0}", originalMaster);
            }
        }
        updateStableClusterSize(view, members);
        boolean canReachWKA = ClusterUtils.pingHost(this.env.getPingServer());
        if (this.isolated && canReachWKA) {
            this.isolated = false;
            Notifications.NODE_NOT_ISOLATED.addSubjectParams(new String[] { this.env.getClusterName() }).send();
        } else if (!this.isolated && !canReachWKA) {
            this.isolated = true;
            StringBuilder sb = new StringBuilder();
            if (viewSize == 1) {
                sb.append("[none]");
            } else {
                LOGGER.finest("handleViewChange2 before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("handleViewChange2 has lock");
                    for (Address member : members) {
                        if (sb.length() != 0)
                            sb.append(" ");
                        String host = ClusterUtils.getHost((Channel)this.channel, member, this.clusterState);
                        if (host == null) {
                            sb.append(member);
                            continue;
                        }
                        sb.append(host);
                    }
                }
            }
            String sbString = sb.toString();
            LOGGER.log(Level.WARNING, "Cannot ping WKA. Current members are {0}", sbString);
            Notifications.NODE_ISOLATED.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { sbString }).send();
        }
        if (!isCoordinator) {
            if (coordinatorWasSuspect)
                WorkQueue.executeTask(new Runnable() {
                    public void run() {
                        try {
                            EfmNode.LOGGER.log(Level.INFO, "Current coordinator was in suspect list. Sending current node type in case coordinator lost information while not connected.");
                            ClusterUtils.sendMessageToCoordinator((Channel)EfmNode.this.channel, EfmNode.this.getNodeType());
                        } catch (Exception e) {
                            String msg = "Could not update cluster state with this node's role: " + e.toString();
                            EfmNode.LOGGER.log(Level.SEVERE, msg);
                            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.this$0.env.getClusterName() }).addBodyParams(new String[] { msg }).send();
                        }
                    }
                });
            LOGGER.fine("Non-coordinator returning from view change.");
            return;
        }
        boolean clusterStateChanged = false;
        if (handleMissingNonMasterNodes(members))
            clusterStateChanged = true;
        if (originalMaster != null)
            if (members.contains(originalMaster)) {
                if (viewSize < this.stableSize / 2.0F || (viewSize == 1 && !canReachWKA)) {
                    String masterHost;
                    LOGGER.severe("The master/promoting node has been disconnected from most of the nodes in the cluster. Will fence off in case the cluster majority will be  promoting a new master.");
                    LOGGER.finest("handleViewChange3 before lock");
                    synchronized (this.clusterStateLock) {
                        LOGGER.finest("handleViewChange3 has lock");
                        masterHost = ClusterUtils.getHost((Channel)this.channel, originalMaster, this.clusterState);
                    }
                    if (masterHost == null)
                        masterHost = originalMaster.toString();
                    Notifications.MASTER_ISOLATED_FROM_MAJORITY.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { masterHost }).send();
                    if (originalMaster.equals(me)) {
                        masterIsolated();
                    } else {
                        try {
                            this.channel.send(originalMaster, "fence_database");
                        } catch (Exception e) {
                            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { e.toString() }).send();
                            LOGGER.log(Level.SEVERE, "There was an unexpected problem sending 'fence database' signal to master node: {0}", e.toString());
                        }
                    }
                } else if (viewSize < 3 && this.stableSize > 2.0F) {
                    Notifications.FEWER_THAN_THREE_NODES.addSubjectParams(new String[] { this.env.getClusterName() }).send();
                }
            } else {
                boolean masterFailed;
                clusterStateChanged = true;
                LOGGER.finest("handleViewChange4 before lock");
                synchronized (this.clusterStateLock) {
                    LOGGER.finest("handleViewChange4 has lock");
                    this.clusterState.setMaster(null);
                    this.clusterState.setPromoting(null);
                    masterFailed = this.suspected.contains(originalMaster);
                }
                if (masterFailed) {
                    LOGGER.severe("Master node has disappeared.");
                    if (viewSize > this.stableSize / 2.0F) {
                        LOGGER.fine("***** view size is majority. submitting promote task");
                        final Address origMasterRef = originalMaster;
                        WorkQueue.executeTask(new Runnable() {
                            public void run() {
                                String host;
                                EfmNode.LOGGER.finest("**** running promote task");
                                EfmNode.LOGGER.finest("handleViewChange->promote task before lock");
                                synchronized (EfmNode.this.clusterStateLock) {
                                    EfmNode.LOGGER.finest("handleViewChange->promote task has lock");
                                    host = ClusterUtils.getHost((Channel)EfmNode.this.channel, origMasterRef, EfmNode.this.clusterState);
                                }
                                EfmNode.LOGGER.finest("**** have our host " + host);
                                EfmNode.this.attemptPromotion(host);
                                EfmNode.LOGGER.finest("**** attempt promotion exited");
                            }
                        });
                    } else {
                        StringBuilder sb = new StringBuilder();
                        LOGGER.finest("handleViewChange5 before lock");
                        synchronized (this.clusterStateLock) {
                            LOGGER.finest("handleViewChange5 has lock");
                            for (Address member : view.getMembers()) {
                                String address = ClusterUtils.getHost((Channel)this.channel, member, this.clusterState);
                                if (address == null)
                                    address = member.toString();
                                if (sb.length() > 0)
                                    sb.append(", ");
                                sb.append(address);
                            }
                        }
                        Notifications.CLUSTER_SUBSET_NO_MASTER.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getClusterName(), sb.toString() }).send();
                    }
                } else {
                    LOGGER.info("Master agent has shut down.");
                    Notifications.COORD_SEES_MASTER_EXITED.addSubjectParams(new String[] { this.env.getClusterName() }).send();
                }
            }
        if (clusterStateChanged)
            try {
                LOGGER.finer("Tell all to load shared state.");
                this.channel.send(null, "load_shared_state");
            } catch (Exception e) {
                Exception exception1;
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { exception1.toString() }).send();
                LOGGER.severe("Could not tell nodes to update status: " + exception1.toString());
            }
    }

    private void checkForReplacementMaster(int retries) {
        LOGGER.finest("checkForReplacementMaster before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("checkForReplacementMaster has lock");
            LOGGER.log(Level.FINE, "checkForReplacementMaster called with retries={0}", Integer.valueOf(retries));
            if (!NodeType.MASTER.equals(getNodeType())) {
                LOGGER.log(Level.SEVERE, "Node of type {0} should not be checking for replacement master nodes.", getNodeType().getName());
                return;
            }
            try {
                EfmStatus hs = ClusterUtils.sendStatusRequestToNodes(this.dispatcher, this.clusterState);
                long myTime = System.currentTimeMillis() - this.nodeTypeTime;
                int otherMastersFound = 0;
                boolean thisMasterReplaced = false;
                boolean broadcastLatestLPInfo = false;
                for (NodeStatus ns : hs.getNodeStatusSet()) {
                    if (ns.getIpAddr().equals(this.env.getBindingAddress())) {
                        LOGGER.log(Level.FINE, "Ignoring own node status when looking for replacement master.");
                        continue;
                    }
                    if (NodeType.PROMOTING.getName().equals(ns.getType())) {
                        LOGGER.log(Level.SEVERE, "Found node that is promoting state at {0}", ns.getIpAddr());
                        otherMastersFound++;
                        setLastPromoted(ns.getLastPromoted());
                        continue;
                    }
                    if (NodeType.MASTER.getName().equals(ns.getType())) {
                        long otherMasterTime = ns.getTimeAsNodeType();
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE, "Other master uptime: {0}, this master uptime: {1}", new Object[] { Long.valueOf(otherMasterTime), Long.valueOf(myTime) });
                        if (otherMasterTime < myTime) {
                            LOGGER.log(Level.SEVERE, "Found newer master agent at {0}", ns.getIpAddr());
                            otherMastersFound++;
                            setLastPromoted(ns.getLastPromoted());
                            continue;
                        }
                        LOGGER.log(Level.INFO, "Found older master agent at {0}", ns.getIpAddr());
                        broadcastLatestLPInfo = true;
                        continue;
                    }
                    String lp = ns.getLastPromoted();
                    long tslp = ns.getTimeSinceLastPromoted();
                    if (lp == null || lp.isEmpty()) {
                        LOGGER.log(Level.FINE, "After rejoin, found node of type {0} with no last-promoted info.", ns.getType());
                        if (this.lastPromoted != null && !this.lastPromoted.isEmpty())
                            broadcastLatestLPInfo = true;
                        continue;
                    }
                    LOGGER.log(Level.FINE, "After rejoin, found node of type {0}. Last promoted info from node: {1}. Time since that promotion (ms) {2}", new Object[] { ns.getType(), lp, Long.valueOf(tslp) });
                    if (tslp < myTime && !this.env.getBindingAddress().equals(lp)) {
                        LOGGER.log(Level.SEVERE, "Node at {0} reporting that this node has been replaced as master by {1}.", new Object[] { ns.getIpAddr(), lp });
                        thisMasterReplaced = true;
                        setLastPromoted(lp);
                        continue;
                    }
                    broadcastLatestLPInfo = true;
                }
                if (thisMasterReplaced || otherMastersFound > 0) {
                    Notifications.MASTER_ISOLATED_FROM_MAJORITY_REJOIN.addSubjectParams(new String[] { this.env.getClusterName() }).send();
                    masterIsolated();
                    if (otherMastersFound > 1)
                        LOGGER.log(Level.SEVERE, "This master agent has detected more than one other master. Cluster could be unstable.");
                } else {
                    int currentViewSize = this.channel.getView().size();
                    int responses = hs.getNodeStatusSet().size();
                    if (responses != currentViewSize) {
                        LOGGER.log(Level.SEVERE, "Agent could not retrieve status from all nodes. Expected {0} responses, received {1}.", new Object[] { Integer.valueOf(currentViewSize), Integer.valueOf(responses) });
                        if (retries > 1) {
                            LOGGER.log(Level.SEVERE, "Will wait {0} seconds and check again.", Long.valueOf(10L));
                            final int newRetries = retries - 1;
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    try {
                                        TimeUnit.SECONDS.sleep(10L);
                                        EfmNode.this.checkForReplacementMaster(newRetries);
                                    } catch (InterruptedException e1) {
                                        EfmNode.LOGGER.log(Level.WARNING, "Interrupted waiting.");
                                    }
                                }
                            };
                            Thread recheckThread = new Thread(runnable, "master_recheck_thread");
                            recheckThread.setDaemon(true);
                            recheckThread.start();
                        }
                    } else {
                        if (broadcastLatestLPInfo)
                            ((EfmAgent)this).broadcastLastPromoted(this.env.getBindingAddress());
                        if (LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE, "Expected {0} responses in master check, received {1}.", new Object[] { Integer.valueOf(currentViewSize), Integer.valueOf(responses) });
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Could not perform check for new master in cluster. Cluster state unknown. Unexpected error: " + e);
                if (retries > 1) {
                    final int newRetries = retries - 1;
                    Runnable runnable = new Runnable() {
                        public void run() {
                            try {
                                TimeUnit.SECONDS.sleep(10L);
                                EfmNode.this.checkForReplacementMaster(newRetries);
                            } catch (InterruptedException e1) {
                                EfmNode.LOGGER.log(Level.WARNING, "Interrupted waiting to try master replacement check again.");
                            }
                        }
                    };
                    Thread recheckThread = new Thread(runnable, "master_recheck_thread");
                    recheckThread.setDaemon(true);
                    recheckThread.start();
                }
            }
        }
    }

    private void masterIsolated() {
        if (this instanceof EfmAgent) {
            ((EfmAgent)this).fenceOffIsolatedMaster();
        } else {
            throw new AssertionError("This is not a master node but has the Address of cluster master.");
        }
    }

    private void updateNodesFile(List<Address> addresses) {
        LOGGER.log(Level.FINE, "Updating .nodes file.");
        StringBuilder sb = new StringBuilder();
        Address me = this.channel.getAddress();
        LOGGER.finest("updateNodesFile before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("updateNodesFile has lock");
            for (Address addr : addresses) {
                if (addr.equals(me))
                    continue;
                if (sb.length() != 0)
                    sb.append(" ");
                try {
                    sb.append(ClusterUtils.getHostAndPort((Channel)this.channel, addr, this.clusterState));
                } catch (IllegalStateException npe) {
                    LOGGER.log(Level.SEVERE, "Cannot get IP address for {0}", addr);
                    LOGGER.log(Level.FINE, "Current state: {0}", this.state);
                    Notifications.NODE_IP_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getBindingAddress() }).send();
                    if (this.state == State.STARTING) {
                        System.err.println("Shutting down due to group communication error.");
                        shutdown();
                    }
                }
            }
        }
        LOGGER.log(Level.INFO, "New .nodes file information: {0}", sb);
        String command = this.env.getRootSudoCommand() + " " + this.env.getRootFunctionsScript() + " " + SudoFunctions.WRITE_NODES.toString() + " " + this.env.getClusterName() + " " + sb.toString();
        ProcessResult result = ExecUtil.performExec(true, new String[] { command });
        if (result.getExitValue() != 0) {
            StringBuilder problem = new StringBuilder();
            result.addNiceOutput(problem);
            String problemString = problem.toString();
            Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Could not write new .nodes file information: " + problemString }).send();
            LOGGER.log(Level.WARNING, "Could not write .nodes file content. Command: {0}. Result: {1}", new Object[] { command, problemString });
        }
    }

    private void attemptPromotion(String master) {
        StandbyInfo targetStandby;
        if (!this.env.autoFailoverOn()) {
            LOGGER.warning("Auto failover is turned off. Not proceeding with failover.");
            Notifications.FAILOVER_DISABLED.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getClusterName() }).send();
            return;
        }
        try {
            if (nodeStillUp(PingType.DB_PING, master)) {
                Notifications.PROMOTE_BUT_MASTER_UP.addBodyParams(new String[] { master }).send();
                return;
            }
        } catch (InterruptedException e) {
            Notifications.PROMOTE_BUT_MASTER_UNKNOWN.addBodyParams(new String[] { master }).send();
            return;
        }
        if (this.env.isVipEnabled())
            try {
                if (nodeStillUp(PingType.VIP_PING, null)) {
                    Notifications.PROMOTE_BUT_VIP_ASSIGNED.addBodyParams(new String[] { this.env.getVirtualIp() }).send();
                    return;
                }
            } catch (InterruptedException e) {
                LOGGER.severe("Node was interrupted while trying to verify VIP.");
                return;
            }
        try {
            targetStandby = chooseStandbyForPromotion();
        } catch (NoStandbysException nse) {
            LOGGER.severe("Could not find an available standby for failover.");
            Notifications.PROMOTE_BUT_NO_STANDBY.addBodyParams(new String[] { this.env.getClusterName() }).send();
            return;
        } catch (NotEnoughStandbysException nese) {
            LOGGER.severe("There are not enough standbys for failover.");
            Notifications.PROMOTE_BUT_NOT_ENOUGH_STANDBYS.addBodyParams(new String[] { this.env.getClusterName() }).send();
            return;
        }
        if (targetStandby == null) {
            LOGGER.severe("There was an error finding an available standby.");
            return;
        }
        try {
            tellStandbyToPromote(targetStandby.getAddress(), master);
        } catch (IllegalStateException ise) {
            LOGGER.log(Level.SEVERE, "Cannot promote node {0}. Message: {1}", new Object[] { this.env.getBindingAddress(), ise.getMessage() });
        }
    }

    void tellStandbyToPromote(Address targetStandby, String failedMaster) {
        if (targetStandby.equals(this.channel.getAddress())) {
            promoteNode(failedMaster);
        } else {
            if (failedMaster == null)
                failedMaster = "";
            RspList<Boolean> rspList = ClusterUtils.getObjectFromNode(targetStandby, this.dispatcher, "promote_self_" + failedMaster);
            if (rspList.isEmpty()) {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Received no response from standby that should promote." }).send();
                LOGGER.severe("Received no response");
                return;
            }
            Rsp<Boolean> rsp = rspList.get(targetStandby);
            Throwable oops = rsp.getException();
            if (oops != null) {
                if (oops instanceof RuntimeException)
                    throw (RuntimeException)oops;
                throw new RuntimeException(oops);
            }
            Boolean respBoolean = (Boolean)rsp.getValue();
            if (respBoolean != null && respBoolean.booleanValue()) {
                LOGGER.log(Level.INFO, "Node {0} will attempt promotion.", ip(targetStandby));
            } else {
                Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { "Node " + ip(targetStandby) + " has signalled that it will not attempt promotion." }).send();
                LOGGER.log(Level.WARNING, "Node {0} will not attempt promotion.", ip(targetStandby));
            }
        }
    }

    private void tellMasterSwitchoverCanceled() {
        if (getNodeType() == NodeType.MASTER) {
            cancelSwitchover();
        } else {
            Address masterAddr;
            LOGGER.finest("tellMasterSwitchoverCanceled before lock");
            synchronized (this.clusterStateLock) {
                LOGGER.finest("tellMasterSwitchoverCanceled has lock");
                masterAddr = this.clusterState.getMaster();
            }
            if (masterAddr == null)
                LOGGER.log(Level.SEVERE, "Cannot find master in cluster state.");
            try {
                this.channel.send(masterAddr, "clear_recovery_conf_text");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cannot send 'clear switchover flag' to master: {0}", e.toString());
            }
        }
    }

    boolean nodeStillUp(PingType pingType, String target) throws InterruptedException {
        if (pingType != PingType.VIP_PING && target == null)
            throw new IllegalArgumentException("Cannot pass in null host for ping type: " + pingType);
        long timeout = Environment.getEnvironment().getRemoteDbTimeout();
        LOGGER.log(Level.INFO, "Checking to see if node is responsive. Check type: {0}", pingType);
        SuccessHolder holder = new SuccessHolder();
        CountDownLatch doneLatch = new CountDownLatch(1);
        List<Thread> pingThreads = createPingThreads(holder, doneLatch, pingType, target);
        for (Thread thread : pingThreads)
            thread.start();
        try {
            return (doneLatch.await(timeout, TimeUnit.SECONDS) && holder.isSuccess());
        } finally {
            for (Thread thread : pingThreads) {
                LOGGER.log(Level.INFO, "Cleaning thread {0} in case still running.", thread.getName());
                thread.interrupt();
            }
        }
    }

    String ip(Address address) {
        LOGGER.finest("ip before lock");
        synchronized (this.clusterStateLock) {
            LOGGER.finest("ip has lock");
            String retVal = ClusterUtils.getHost((Channel)this.channel, address, this.clusterState);
            if (retVal != null)
                return retVal;
        }
        return address.toString();
    }

    private List<Thread> createPingThreads(SuccessHolder sh, final CountDownLatch mainLatch, PingType pingType, String target) {
        List<Address> nodes = this.channel.getView().getMembers();
        Address me = this.channel.getAddress();
        int pingThreads = nodes.size();
        final CountDownLatch threadDoneLatch = new CountDownLatch(pingThreads);
        List<Thread> retVal = new ArrayList<Thread>();
        int counter = 0;
        for (Address node : nodes) {
            if (node.equals(me)) {
                switch (pingType) {
                    case DB_PING:
                        retVal.add(new Thread(createDbCheck(mainLatch, threadDoneLatch, sh, target), "db_ping_local"));
                        continue;
                    case VIP_PING:
                        retVal.add(new Thread(createVipCheck(mainLatch, threadDoneLatch, sh), "vip_ping_local"));
                        continue;
                }
                throw new AssertionError("Unknown: " + pingType);
            }
            retVal.add(new Thread(createRemoteNodePingCheck(mainLatch, threadDoneLatch, sh, target, node, pingType), "rem_ping_" + counter++));
        }
        retVal.add(new Thread(new Runnable() {
            public void run() {
                EfmNode.LOGGER.fine("waiting for threadDoneLatch");
                try {
                    threadDoneLatch.await();
                } catch (InterruptedException e) {
                    EfmNode.LOGGER.log(Level.FINE, "{0} interrupted.", Thread.currentThread().getName());
                }
                EfmNode.LOGGER.fine("all threads done. decrementing main latch");
                mainLatch.countDown();
            }
        },"ping_monitor"));
        if (retVal.size() != pingThreads + 1)
            throw new AssertionError("Wrong number of threads created for pinging target.");
        return retVal;
    }

    private Runnable createDbCheck(final CountDownLatch mainLatch, final CountDownLatch threadDoneLatch, final SuccessHolder sh, final String target) {
        return new Runnable() {
            public void run() {
                try {
                    DBMonitor dbm;
                    if (target == null) {
                        EfmNode.LOGGER.warning("Cannot check remote DB with null address.");
                        return;
                    }
                    try {
                        dbm = DBUtils.createMonitor(target);
                    } catch (PasswordDecryptException e) {
                        EfmNode.LOGGER.severe(e.toString());
                        return;
                    }
                    if (dbm.checkOnce(false, true)) {
                        sh.success();
                        mainLatch.countDown();
                    }
                } finally {
                    threadDoneLatch.countDown();
                }
            }
        };
    }

    private Runnable createVipCheck(final CountDownLatch mainLatch, final CountDownLatch threadDoneLatch, final SuccessHolder sh) {
        return new Runnable() {
            public void run() {
                try {
                    if (ClusterUtils.isVipReachable()) {
                        sh.success();
                        mainLatch.countDown();
                    }
                } finally {
                    threadDoneLatch.countDown();
                }
            }
        };
    }

    private Runnable createRemoteNodePingCheck(final CountDownLatch mainLatch, final CountDownLatch threadDoneLatch, final SuccessHolder sh, final String target, final Address node, final PingType pingType) {
        return new Runnable() {
            public void run() {
                try {
                    String checkType;
                    EfmNode.LOGGER.log(Level.INFO, "Telling node {0} to ping {1}", new Object[] { this.val$node, this.val$pingType });
                    switch (pingType) {
                        case DB_PING:
                            checkType = EfmNode.PingType.DB_PING.name() + "\\|" + target;
                            break;
                        case VIP_PING:
                            checkType = EfmNode.PingType.VIP_PING.name();
                            break;
                        default:
                            EfmNode.LOGGER.log(Level.SEVERE, "Unknown ping type: {0}", pingType);
                            return;
                    }
                    RspList<Boolean> rspList = ClusterUtils.getObjectFromNode(node, EfmNode.this.dispatcher, checkType);
                    if (rspList.isEmpty()) {
                        EfmNode.LOGGER.severe("Received no response");
                        return;
                    }
                    Boolean respBoolean = (Boolean)rspList.getFirst();
                    if (respBoolean != null && respBoolean.booleanValue()) {
                        sh.success();
                        mainLatch.countDown();
                    }
                } finally {
                    threadDoneLatch.countDown();
                }
            }
        };
    }

    public abstract void shutdown();

    public abstract void resumeMonitoring() throws Exception;

    abstract boolean checkVipUse();

    abstract boolean determineStartupAgentType();

    abstract NodeType getNodeType();

    abstract Object doHandle(Object paramObject) throws Exception;

    abstract void promoteNode(String paramString);

    abstract void stopMonitoringForManualPromotion();

    abstract void switchToNewMaster(Address paramAddress);

    abstract void prepareForSwitchover(String paramString);

    abstract void cancelSwitchover();

    abstract void reconfigureAsStandby(Address paramAddress);
}
