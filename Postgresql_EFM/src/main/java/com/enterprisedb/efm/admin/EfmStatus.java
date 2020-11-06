package com.enterprisedb.efm.admin;

import com.enterprisedb.efm.ClusterState;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.LogManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EfmStatus {
    private final Environment env = Environment.getEnvironment();

    private static final Logger LOGGER = LogManager.getEfmLogger();

    public static final String NODE_STATUS = "node_status";

    public static final String LIST_IS_EMPTY = "(List is empty.)";

    private static final String STATUS_OUTPUT_TEMPLATE = "\t%-11s %-20s %-6s %-8s %s";

    public static final String NODE_SEP = "||";

    public static final String FIELD_SEP = "%%";

    private static final int EXPECTED_NUM_TOKENS = 5;

    private final Set<NodeStatus> nodeStatusSet = new HashSet<NodeStatus>();

    private String allowedNodeList;

    private String stdbyPriorityList;

    private String coordinator;

    private boolean allUp = true;

    private String error;

    public EfmStatus() {}

    public EfmStatus(String results) {
        unmarshal(results);
    }

    public void addNodeStatus(NodeStatus nodeStatus) {
        this.nodeStatusSet.add(nodeStatus);
    }

    public boolean isAllUp() {
        return this.allUp;
    }

    public List<String> getAllowedNodeList() {
        List<String> retVal = new ArrayList<String>();
        for (String node : this.allowedNodeList.split(" ")) {
            if (!node.trim().isEmpty())
                retVal.add(node);
        }
        return retVal;
    }

    public String getCoordinator() {
        return this.coordinator;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getStdbyPriorityList() {
        if (this.stdbyPriorityList.trim().equals("(List is empty.)"))
            return Collections.emptyList();
        List<String> retVal = new ArrayList<String>();
        for (String stdby : this.stdbyPriorityList.split(" ")) {
            if (!stdby.trim().isEmpty())
                retVal.add(stdby);
        }
        return retVal;
    }

    public void print() {
        System.out.println("Cluster Status: " + this.env.getClusterName());
        String vip = this.env.getVirtualIp();
        if (vip == null)
            vip = "";
        System.out.println("VIP: " + vip);
        if (!this.env.autoFailoverOn())
            System.out.println("Automatic failover is disabled.");
        int minStandbys = this.env.getMinStandbys();
        if (minStandbys > 0)
            System.out.println("Minimum number of standbys: " + minStandbys);
        System.out.println("\n" + String.format("\t%-11s %-20s %-6s %-8s %s", new Object[] { "Agent Type", "Address", "Agent", "DB", "Info" }));
        System.out.println("\t--------------------------------------------------------------");
        for (NodeStatus nodeStat : this.nodeStatusSet) {
            if (EfmNode.NodeType.WITNESS.getName().equals(nodeStat.getType())) {
                System.out.println(String.format("\t%-11s %-20s %-6s %-8s %s", new Object[] { nodeStat.getType(), nodeStat.getIpAddr(), nodeStat.getAgentStatus() ? "UP" : "DOWN", "N/A", nodeStat.getInfo() }));
                continue;
            }
            if (EfmNode.NodeType.IDLE.getName().equals(nodeStat.getType())) {
                System.out.println(String.format("\t%-11s %-20s %-6s %-8s %s", new Object[] { nodeStat.getType(), nodeStat.getIpAddr(), nodeStat.getAgentStatus() ? "UP" : "DOWN", "UNKNOWN", nodeStat.getInfo() }));
                continue;
            }
            System.out.println(String.format("\t%-11s %-20s %-6s %-8s %s", new Object[] { nodeStat.getType(), nodeStat.getIpAddr(), nodeStat.getAgentStatus() ? "UP" : "DOWN", nodeStat.getDbStatus() ? "UP" : "DOWN", nodeStat.getInfo() }));
        }
        System.out.print("\nAllowed node host list:\n\t");
        System.out.println(this.allowedNodeList);
        System.out.println("\nMembership coordinator: " + this.coordinator);
        System.out.print("\nStandby priority host list:\n\t");
        System.out.println(this.stdbyPriorityList);
    }

    public Set<NodeStatus> getNodeStatusSet() {
        return Collections.unmodifiableSet(this.nodeStatusSet);
    }

    public void setHostLists(ClusterState state, String coordinatorHost) {
        StringBuilder allowedBuilder = new StringBuilder();
        for (String host : state.getAllowed()) {
            if (allowedBuilder.length() != 0)
                allowedBuilder.append(" ");
            allowedBuilder.append(host);
        }
        this.allowedNodeList = allowedBuilder.toString();
        this.coordinator = coordinatorHost;
        StringBuilder foBuilder = new StringBuilder();
        for (String host : state.getFoPriority()) {
            if (foBuilder.length() != 0)
                foBuilder.append(" ");
            foBuilder.append(host);
        }
        this.stdbyPriorityList = foBuilder.toString();
    }

    public String marshal() {
        StringBuilder results = new StringBuilder();
        for (NodeStatus nodeStatus : this.nodeStatusSet) {
            if (results.length() != 0)
                results.append("||");
            results.append(nodeStatus.marshal());
        }
        results.append("||");
        if (this.allowedNodeList == null || this.allowedNodeList.isEmpty()) {
            results.append("(List is empty.)");
        } else {
            results.append(this.allowedNodeList);
        }
        results.append("%%");
        results.append(this.coordinator);
        results.append("%%");
        if (this.stdbyPriorityList == null || this.stdbyPriorityList.isEmpty()) {
            results.append("(List is empty.)");
        } else {
            results.append(this.stdbyPriorityList);
        }
        return results.toString();
    }

    private void unmarshal(String results) {
        StringTokenizer nodes = new StringTokenizer(results, "||");
        while (nodes.hasMoreElements()) {
            String node = nodes.nextToken();
            StringTokenizer fields = new StringTokenizer(node, "%%");
            int tokens = fields.countTokens();
            if (tokens == 5) {
                String nodeTypeName = fields.nextToken();
                String ipAddr = fields.nextToken();
                boolean agentStatus = Boolean.parseBoolean(fields.nextToken());
                boolean dbStatus = Boolean.parseBoolean(fields.nextToken());
                String info = fields.nextToken();
                if (!agentStatus || (!dbStatus && !nodeTypeName.equals(EfmNode.NodeType.WITNESS.getName())))
                    this.allUp = false;
                addNodeStatus(new NodeStatus(nodeTypeName, ipAddr, agentStatus, dbStatus, info, 0L, null, 0L, null));
                continue;
            }
            if (tokens == 3) {
                this.allowedNodeList = fields.nextToken();
                this.coordinator = fields.nextToken();
                this.stdbyPriorityList = fields.nextToken();
                if (this.stdbyPriorityList == null)
                    this.stdbyPriorityList = "";
                continue;
            }
            LOGGER.log(Level.SEVERE, "Error parsing node status - incorrect number of tokens in: {0}", node);
            this.allUp = false;
        }
    }
}
