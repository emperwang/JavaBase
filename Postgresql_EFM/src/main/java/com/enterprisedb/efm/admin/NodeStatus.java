package com.enterprisedb.efm.admin;

import java.io.Serializable;
import org.jgroups.Address;

public class NodeStatus implements Serializable {
    private static final long serialVersionUID = -8114792528245728092L;

    private final String type;

    private final String ipAddr;

    private final boolean agentStatus;

    private final boolean dbStatus;

    private final String info;

    private final long timeAsNodeType;

    private final String lastPromoted;

    private final long timeSinceLastPromoted;

    private final Address address;

    public static final String NONE = " ";

    public NodeStatus(String nodeTypeName, String ipAddr, boolean agentStatus, boolean dbStatus, long timeAsNodeType, String lastPromoted, long timeSinceLastPromoted, Address address) {
        this(nodeTypeName, ipAddr, agentStatus, dbStatus, " ", timeAsNodeType, lastPromoted, timeSinceLastPromoted, address);
    }

    public NodeStatus(String nodeTypeName, String ipAddr, boolean agentStatus, boolean dbStatus, String info, long timeAsNodeType, String lastPromoted, long timeSinceLastPromoted, Address address) {
        this.type = nodeTypeName;
        this.ipAddr = ipAddr;
        this.agentStatus = agentStatus;
        this.dbStatus = dbStatus;
        this.info = info;
        this.timeAsNodeType = timeAsNodeType;
        this.lastPromoted = lastPromoted;
        this.timeSinceLastPromoted = timeSinceLastPromoted;
        this.address = address;
    }

    public String getType() {
        return this.type;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public boolean getAgentStatus() {
        return this.agentStatus;
    }

    public boolean getDbStatus() {
        return this.dbStatus;
    }

    public String getInfo() {
        return this.info;
    }

    public long getTimeAsNodeType() {
        return this.timeAsNodeType;
    }

    public String getLastPromoted() {
        return this.lastPromoted;
    }

    public long getTimeSinceLastPromoted() {
        return this.timeSinceLastPromoted;
    }

    public Address getAddress() {
        return this.address;
    }

    public String marshal() {
        return this.type + "%%" + this.ipAddr + "%%" + this.agentStatus + "%%" + this.dbStatus + "%%" + this.info;
    }
}
