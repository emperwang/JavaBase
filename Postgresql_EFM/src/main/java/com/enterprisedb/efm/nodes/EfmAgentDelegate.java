package com.enterprisedb.efm.nodes;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import java.util.logging.Logger;

public abstract class EfmAgentDelegate {
    final Environment env = Environment.getEnvironment();

    static final Logger LOGGER = LogManager.getEfmLogger();

    final EfmNode.NodeType type;

    final EfmAgent owner;

    volatile boolean replaced = false;

    public EfmAgentDelegate(EfmNode.NodeType type, EfmAgent owner) {
        this.type = type;
        this.owner = owner;
    }

    public String getAgentTypeName() {
        return this.type.getName();
    }

    public abstract Notifications getNowWatchingNotification();

    public EfmNode.NodeType getType() {
        return this.type;
    }

    public void replaced() {
        this.replaced = true;
    }

    public abstract boolean setupAfterJoiningCluster();

    public abstract void cleanupAfterDbFailure();

    public abstract void checkRecoveryState();

    void promoteNode(String failedMaster) {
        throw new IllegalStateException("Cannot promote node type: " + getType());
    }

    boolean assignVip() {
        return ClusterUtils.acquireVip();
    }
}
