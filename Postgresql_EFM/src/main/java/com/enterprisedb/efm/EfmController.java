package com.enterprisedb.efm;
import com.enterprisedb.efm.admin.AdminServer;
import com.enterprisedb.efm.admin.EfmStatus;
import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import com.enterprisedb.efm.main.SubCommand;
import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.DBUtils;
import com.enterprisedb.efm.utils.LogManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
/**
 * @author: wk
 * @Date: 2020/11/6 16:40
 * @Description
 */
public class EfmController {
    private boolean forcePromote;

    private boolean performingSwitchover = false;

    private final Environment env = Environment.getEnvironment();

    private static final String STATUS_OUTPUT_TEMPLATE = "\t%-11s %-20s %-16s %s";

    private static final String IDLE_STATUS_OUTPUT_TEMPLATE = "\t%-20s %-16s %s";

    public boolean run(SubCommand command) {
        switch (command) {
            case CLUSTER_STATUS:
                return reportStatus();
            case CLUSTER_STATUS_JSON:
                return reportStatusJson();
            case PROMOTE:
                this.forcePromote = false;
                return doPromote();
            case FORCE_PROMOTE:
                this.forcePromote = true;
                return doPromote();
            case RESUME:
                return doResume();
            case STOP:
                return doStop();
            case STOP_CLUSTER:
                return doStopCluster();
        }
        System.err.println("Unknown command: " + command.getValue());
        return false;
    }

    public boolean switchover() {
        this.performingSwitchover = true;
        if (!checkMembersAndPromoteStatus())
            return false;
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.SWITCHOVER, new String[0]);
            if (response.equals("F_T_W")) {
                System.out.println("Promote/switchover command accepted by local agent. Proceeding with promotion and will reconfigure original master. Run the 'cluster-status' command for information about the new cluster state.");
                return true;
            }
            System.err.println("Received error from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send promote/switchover command.");
            return false;
        }
    }

    public boolean runAddNode(String ipPort, String priority) {
        try {
            String response;
            if (priority == null) {
                response = getAgentResponse(AdminServer.AdminCommand.ADD_NODE, new String[] { ipPort });
            } else {
                response = getAgentResponse(AdminServer.AdminCommand.ADD_NODE, new String[] { ipPort, priority });
            }
            if (response.equals("F_T_W")) {
                System.out.println("add-node signal sent to local agent.");
                return true;
            }
            System.err.println("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send add-node command.");
            return false;
        }
    }

    public boolean runRemoveNode(String ip) {
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.REMOVE_NODE, new String[] { ip });
            if (response.equals("F_T_W")) {
                System.out.println("remove-node signal sent to local agent.");
                return true;
            }
            System.err.println("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send remove command.");
            return false;
        }
    }

    public boolean runAllowNode(String ip) {
        try {
            if (InetAddress.getByName("0.0.0.0").equals(InetAddress.getByName(ip))) {
                System.err.println("Can not add address equivalent to 0.0.0.0");
                return false;
            }
        } catch (UnknownHostException unknownHostException) {}
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.ALLOW_NODE, new String[] { ip });
            if (response.equals("F_T_W")) {
                System.out.println("allow-node signal sent to local agent.");
                return true;
            }
            System.err.println("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send allow command.");
            return false;
        }
    }

    public boolean runDisallowNode(String ip) {
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.DISALLOW_NODE, new String[] { ip });
            if (response.equals("F_T_W")) {
                System.out.println("disallow-node signal sent to local agent.");
                return true;
            }
            System.err.println("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send remove command.");
            return false;
        }
    }

    public boolean runSetPriority(String ip, String priority) {
        EfmStatus status;
        try {
            status = new EfmStatus(getAgentResponse(AdminServer.AdminCommand.STATUS, new String[0]));
        } catch (IOException e) {
            System.err.println("Error contacting local agent - unable to proceed.");
            return false;
        }
        String s = EfmNode.NodeType.STANDBY.getName();
        boolean addressFound = false;
        List<String> addresses = new ArrayList<String>();
        for (NodeStatus ns : status.getNodeStatusSet()) {
            if (s.equals(ns.getType())) {
                if (ip.equals(ns.getIpAddr())) {
                    addressFound = true;
                    break;
                }
                addresses.add(ns.getIpAddr());
            }
        }
        if (!addressFound) {
            System.err.println(String.format("Address %s not found in list of current standby addresses: %s", new Object[] { ip, addresses }));
            return false;
        }
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.SET_PRIORITY, new String[] { ip, priority });
            if (response.equals("F_T_W")) {
                System.out.println("set-priority signal sent to local agent.");
                return true;
            }
            System.err.println("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send add-node command.");
            return false;
        }
    }

    private boolean doStop() {
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.STOP, new String[0]);
            if (response.equals("F_T_W")) {
                LogManager.tsOut("Stop signal sent to local agent.");
                return true;
            }
            LogManager.tsErr("Received unknown response from local agent: " + response);
            return false;
        } catch (IOException e) {
            LogManager.tsErr("Error contacting local agent to send stop command.");
            return false;
        }
    }

    private boolean doResume() {
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.RESUME, new String[0]);
            if (response.equals("F_T_W")) {
                System.out.println("Resume command successful on local agent.");
                return true;
            }
            System.err.println("Received error from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send resume command.");
            return false;
        }
    }

    private boolean doStopCluster() {
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.STOP_CLUSTER, new String[0]);
            try {
                System.out.println(String.format("Stop cluster command sent to %s nodes.", new Object[] { Integer.valueOf(Integer.parseInt(response)) }));
                return true;
            } catch (NumberFormatException nfe) {
                System.err.println("Received unknown response from local agent: " + response);
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send stop-cluster command.");
            return false;
        }
    }

    private boolean doPromote() {
        LogManager.getEfmLogger().setLevel(Level.OFF);
        if (!checkMembersAndPromoteStatus())
            return false;
        try {
            String response = getAgentResponse(AdminServer.AdminCommand.PROMOTE, new String[0]);
            if (response.equals("F_T_W")) {
                System.out.println("Promote command accepted by local agent. Proceeding with promotion. Run the 'cluster-status' command for information about the new cluster state.");
                return true;
            }
            System.err.println("Received error from local agent: " + response);
            return false;
        } catch (IOException e) {
            System.err.println("Error contacting local agent to send promote command.");
            return false;
        }
    }

    private boolean reportStatus() {
        try {
            EfmStatus status = new EfmStatus(getAgentResponse(AdminServer.AdminCommand.STATUS, new String[0]));
            status.print();
            boolean promoteStatus = reportPromoteStatus(status, null);
            return (status.isAllUp() && promoteStatus);
        } catch (UnknownHostException e) {
            System.out.println("Error contacting local agent - unable to report status.");
            return false;
        } catch (IOException e) {
            System.out.println("Error contacting local agent - unable to report status.");
            return false;
        }
    }

    private boolean reportStatusJson() {
        try {
            EfmStatus status = new EfmStatus(getAgentResponse(AdminServer.AdminCommand.STATUS, new String[0]));
            Map<String, Map<String, String>> nodesMap = new LinkedHashMap<String, Map<String, String>>();
            for (NodeStatus ns : status.getNodeStatusSet()) {
                Map<String, String> statusmap = new LinkedHashMap<String, String>();
                statusmap.put("type", ns.getType());
                if (ns.getAgentStatus()) {
                    statusmap.put("agent", "UP");
                } else {
                    statusmap.put("agent", "DOWN");
                }
                if (ns.getType().equals(EfmNode.NodeType.IDLE.getName())) {
                    statusmap.put("db", "UNKNOWN");
                } else if (ns.getType().equals(EfmNode.NodeType.WITNESS.getName())) {
                    statusmap.put("db", "N/A");
                } else if (ns.getDbStatus()) {
                    statusmap.put("db", "UP");
                } else {
                    statusmap.put("db", "DOWN");
                }
                statusmap.put("info", ns.getInfo());
                nodesMap.put(ns.getIpAddr(), statusmap);
            }
            Map<String, Object> mainMap = new LinkedHashMap<String, Object>();
            mainMap.put("nodes", nodesMap);
            mainMap.put("allowednodes", status.getAllowedNodeList());
            mainMap.put("membershipcoordinator", status.getCoordinator());
            mainMap.put("failoverpriority", status.getStdbyPriorityList());
            String vip = this.env.getVirtualIp();
            if (vip == null)
                vip = "";
            mainMap.put("VIP", vip);
            mainMap.put("minimumstandbys", Integer.valueOf(this.env.getMinStandbys()));
            boolean promoteStatus = reportPromoteStatus(status, mainMap);
            System.out.println(JSONValue.toJSONString(mainMap));
            return (status.isAllUp() && promoteStatus);
        } catch (UnknownHostException e) {
            JSONArray errors = new JSONArray();
            errors.add("Error contacting local agent - unable to report status.");
            JSONObject json = new JSONObject();
            json.put("errors", errors);
            System.out.println(json);
            return false;
        } catch (IOException e) {
            JSONArray errors = new JSONArray();
            errors.add("Error contacting local agent - unable to report status.");
            JSONObject json = new JSONObject();
            json.put("errors", errors);
            System.out.println(json);
            return false;
        }
    }

    private boolean reportPromoteStatus(EfmStatus status, Map<String, Object> outputMap) {
        LogManager.getEfmLogger().setLevel(Level.OFF);
        Set<String> ips = new HashSet<String>();
        Set<String> idleIps = new HashSet<String>();
        boolean foundXlogLocations = false;
        String idleType = EfmNode.NodeType.IDLE.getName();
        String witnessType = EfmNode.NodeType.WITNESS.getName();
        for (NodeStatus nodeStatus : status.getNodeStatusSet()) {
            if (witnessType.equals(nodeStatus.getType()))
                continue;
            if (idleType.equals(nodeStatus.getType()))
                idleIps.add(nodeStatus.getIpAddr());
            ips.add(nodeStatus.getIpAddr());
        }
        SingleNodeInfo master = null;
        Set<SingleNodeInfo> standbys = new HashSet<SingleNodeInfo>();
        Set<SingleNodeInfo> idles = new HashSet<SingleNodeInfo>();
        List<String> errList = null;
        Map<String, Map<String, String>> nodesMap = null;
        if (outputMap != null) {
            errList = new LinkedList<String>();
            outputMap.put("messages", errList);
            nodesMap = (Map<String, Map<String, String>>)outputMap.get("nodes");
        }
        try {
            for (String ip : ips) {
                String nodeInfo = "";
                boolean inRecovery = false;
                boolean running = false;
                String location = null;
                try {
                    DBMonitor monitor = DBUtils.createMonitor(ip);
                    inRecovery = monitor.isInRecovery();
                    location = monitor.getXlogLocation(!inRecovery);
                    running = true;
                } catch (SQLException sqle) {
                    if (sqle.getMessage() != null) {
                        nodeInfo = sqle.getMessage();
                    } else {
                        nodeInfo = sqle.toString();
                    }
                }
                if (idleIps.contains(ip)) {
                    idles.add(new SingleNodeInfo(ip, inRecovery, running, location, nodeInfo));
                    continue;
                }
                SingleNodeInfo sni = new SingleNodeInfo(ip, inRecovery, running, location, nodeInfo);
                if (location != null && !location.isEmpty())
                    foundXlogLocations = true;
                if (running && !inRecovery) {
                    if (master != null) {
                        String message = "\nCannot determine promotestatus. One master exists at " + master.getIp() + ". Found a second master at " + ip + ".";
                        if (errList != null) {
                            errList.add(message);
                        } else {
                            System.err.println("\n" + message);
                        }
                        return false;
                    }
                    master = sni;
                    continue;
                }
                standbys.add(sni);
            }
        } catch (PasswordDecryptException pde) {
            String message = "Could not decrypt user password to check databases: " + pde.getCause();
            if (errList != null) {
                errList.add(message);
            } else {
                System.err.println("\t" + message);
            }
            return false;
        } catch (Exception e) {
            String message = "Unexpected problem checking xlog locations: " + e.toString();
            if (errList != null) {
                errList.add(message);
            } else {
                System.err.println(message);
            }
            return false;
        }
        if (outputMap == null)
            System.out.println("\nPromote Status:\n");
        boolean retVal = true;
        if (foundXlogLocations) {
            if (nodesMap == null) {
                System.out.println(String.format("\t%-11s %-20s %-16s %s", new Object[] { "DB Type", "Address", "XLog Loc", "Info" }));
                System.out.println("\t--------------------------------------------------------------");
                if (master != null)
                    printXlogStatus(master);
                for (SingleNodeInfo standby : standbys)
                    printXlogStatus(standby);
            } else {
                if (master != null) {
                    Map<String, String> nodeMap = nodesMap.get(master.getIp());
                    if (master.getXlogLocation() == null) {
                        nodeMap.put("xlog", "UNKNOWN");
                    } else {
                        nodeMap.put("xlog", master.getXlogLocation());
                    }
                    nodeMap.put("xloginfo", master.getInfo());
                }
                for (SingleNodeInfo standby : standbys) {
                    Map<String, String> nodeMap = nodesMap.get(standby.getIp());
                    if (standby.getXlogLocation() == null) {
                        nodeMap.put("xlog", "UNKNOWN");
                    } else {
                        nodeMap.put("xlog", standby.getXlogLocation());
                    }
                    nodeMap.put("xloginfo", standby.getInfo());
                }
            }
            if (outputMap == null)
                System.out.print("\n\t");
            if (master == null) {
                String message = "No master database was found.";
                if (errList != null) {
                    errList.add("No master database was found.");
                } else {
                    System.out.println("No master database was found.");
                }
                retVal = false;
            } else if (standbys.isEmpty()) {
                String message = "No standby databases were found.";
                if (errList != null) {
                    errList.add("No standby databases were found.");
                } else {
                    System.out.println("No standby databases were found.");
                }
                retVal = false;
            } else {
                for (SingleNodeInfo sni : standbys) {
                    if (!master.getXlogLocation().equals(sni.getXlogLocation())) {
                        String message = "One or more standby databases are not in sync with the master database.";
                        if (errList != null) {
                            errList.add("One or more standby databases are not in sync with the master database.");
                        } else {
                            System.err.println("One or more standby databases are not in sync with the master database.");
                        }
                        retVal = false;
                        break;
                    }
                }
            }
            if (retVal && outputMap == null)
                System.out.println("Standby database(s) in sync with master. It is safe to promote.");
        } else if (!idles.isEmpty()) {
            String message = "Did not find XLog location for any non-idle nodes.";
            if (errList != null) {
                errList.add("Did not find XLog location for any non-idle nodes.");
            } else {
                System.err.println("Did not find XLog location for any non-idle nodes.");
            }
            retVal = false;
        } else {
            String message = "Did not find XLog location for any nodes.";
            if (errList != null) {
                errList.add("Did not find XLog location for any nodes.");
            } else {
                System.err.println("Did not find XLog location for any nodes.");
            }
            retVal = false;
        }
        if (!idles.isEmpty())
            if (nodesMap == null) {
                System.out.println("\nIdle Node Status (idle nodes ignored in XLog location comparisons):\n");
                System.out.println(String.format("\t%-20s %-16s %s", new Object[] { "Address", "XLog Loc", "Info" }));
                System.out.println("\t--------------------------------------------------------------");
                for (SingleNodeInfo idle : idles)
                    printIdleXlogStatus(idle);
            } else {
                for (SingleNodeInfo idle : idles) {
                    Map<String, String> nodeMap = nodesMap.get(idle.getIp());
                    if (idle.getXlogLocation() == null) {
                        nodeMap.put("xlog", "UNKNOWN");
                        nodeMap.put("xloginfo", idle.getInfo());
                        continue;
                    }
                    nodeMap.put("xlog", idle.getXlogLocation());
                    if (idle.isInRecovery()) {
                        nodeMap.put("xloginfo", "DB is in recovery.");
                        continue;
                    }
                    nodeMap.put("xloginfo", "DB is not in recovery.");
                }
            }
        return retVal;
    }

    private void printXlogStatus(SingleNodeInfo info) {
        if (info.getXlogLocation() == null) {
            System.out.println(String.format("\t%-11s %-20s %-16s %s", new Object[] { "Unknown", info.getIp(), "UNKNOWN", info.getInfo() }));
        } else {
            String type = info.isInRecovery() ? "Standby" : "Master";
            System.out.println(String.format("\t%-11s %-20s %-16s %s", new Object[] { type, info.getIp(), info.getXlogLocation(), "" }));
        }
    }

    private void printIdleXlogStatus(SingleNodeInfo info) {
        if (info.getXlogLocation() == null) {
            System.out.println(String.format("\t%-20s %-16s %s", new Object[] { info.getIp(), "UNKNOWN", info.getInfo() }));
        } else {
            String recInfo;
            if (info.isInRecovery()) {
                recInfo = "DB is in recovery.";
            } else {
                recInfo = "DB is not in recovery.";
            }
            System.out.println(String.format("\t%-20s %-16s %s", new Object[] { info.getIp(), info.getXlogLocation(), recInfo }));
        }
    }

    private boolean checkMembersAndPromoteStatus() {
        EfmStatus status;
        try {
            status = new EfmStatus(getAgentResponse(AdminServer.AdminCommand.STATUS, new String[0]));
        } catch (IOException e) {
            System.err.println("Error contacting local agent - unable to proceed.");
            return false;
        }
        if (status.getStdbyPriorityList().isEmpty()) {
            System.err.println("The standby priority host list must contain at least one standby that can be promoted. Run the cluster-status command for more information.");
            return false;
        }
        int numTotalStandbys = 0;
        boolean promotingExists = false;
        boolean masterFound = false;
        for (NodeStatus ns : status.getNodeStatusSet()) {
            if (EfmNode.NodeType.STANDBY.getName().equals(ns.getType())) {
                numTotalStandbys++;
                continue;
            }
            if (EfmNode.NodeType.PROMOTING.getName().equals(ns.getType())) {
                promotingExists = true;
                continue;
            }
            if (EfmNode.NodeType.MASTER.getName().equals(ns.getType()))
                masterFound = true;
        }
        if (numTotalStandbys <= this.env.getMinStandbys()) {
            System.err.println("There are not enough standbys to promote. Run the cluster-status command for more information.");
            return false;
        }
        if (promotingExists) {
            System.err.println("A node is currently being promoted. Cannot promote a new node while a promotion is happening.");
            return false;
        }
        if (this.performingSwitchover && !masterFound) {
            System.err.println("No master database was found in the cluster. Cannot proceed with promotion and switchover.");
            return false;
        }
        return checkXlogLocations(status);
    }

    private boolean checkOkWithoutMaster() {
        String prompt;
        if (this.env.isVipEnabled() && ClusterUtils.isVipReachable()) {
            if (this.forcePromote) {
                System.err.println("The master agent is not present but the VIP is in use.");
                return false;
            }
            prompt = "The master agent is not present but the VIP is in use. Before proceeding, you should ensure that the VIP is not in use by a node other than the standby node. Are you sure you want to continue? [y/N]: ";
        } else {
            if (this.forcePromote) {
                System.err.println("The master agent is not present. Continuing with promotion.");
                return true;
            }
            prompt = "The master agent is not present. Are you sure you want to continue? [y/N]: ";
        }
        String response = System.console().readLine(prompt, new Object[0]).trim();
        while (!"y".equalsIgnoreCase(response) && !"n".equalsIgnoreCase(response) && !response.isEmpty()) {
            System.err.println("Please enter y or n.");
            response = System.console().readLine(prompt, new Object[0]).trim();
        }
        return "y".equalsIgnoreCase(response);
    }

    private boolean checkXlogLocations(EfmStatus status) {
        boolean promptRequired = false;
        String masterXlogLocation = null;
        boolean masterPresent = false;
        Map<String, String> standbyLocations = new HashMap<String, String>();
        for (NodeStatus ns : status.getNodeStatusSet()) {
            if (EfmNode.NodeType.MASTER.getName().equals(ns.getType())) {
                if (masterXlogLocation != null) {
                    System.err.println("Found more than one master node. Please check cluster status.");
                    return false;
                }
                try {
                    DBMonitor monitor = DBUtils.createMonitor(ns.getIpAddr());
                    masterXlogLocation = monitor.getXlogLocation(true);
                    masterPresent = true;
                } catch (PasswordDecryptException e) {
                    System.err.println("Could not decrypt password to check database status: " + e);
                    return false;
                } catch (SQLException e) {
                    System.err.println("Could not determine transaction log location for master node: " + e);
                    promptRequired = true;
                }
                continue;
            }
            if (EfmNode.NodeType.STANDBY.getName().equals(ns.getType()))
                try {
                    DBMonitor monitor = DBUtils.createMonitor(ns.getIpAddr());
                    standbyLocations.put(ns.getIpAddr(), monitor.getXlogLocation(false));
                } catch (PasswordDecryptException e) {
                    System.err.println("Could not decrypt password to check database status: " + e);
                    return false;
                } catch (SQLException e) {
                    System.err.println("Could not determine transaction log location for standby node " + ns.getIpAddr() + ": " + e);
                    promptRequired = true;
                }
        }
        if (!masterPresent && !checkOkWithoutMaster())
            return false;
        String targetLocation = null;
        if (masterXlogLocation != null) {
            targetLocation = masterXlogLocation;
        } else {
            for (String anyLocation : standbyLocations.values()) {
                if (anyLocation != null) {
                    targetLocation = anyLocation;
                    break;
                }
            }
        }
        if (targetLocation == null) {
            System.err.println("No transaction logs could be found to check if databases are synchronized.");
            promptRequired = true;
        } else {
            for (String location : standbyLocations.values()) {
                if (!targetLocation.equals(location)) {
                    System.err.println("Not all of the database transaction logs match. Run the 'cluster-status' command for more information.");
                    promptRequired = true;
                    break;
                }
            }
        }
        if (promptRequired) {
            if (this.performingSwitchover) {
                System.err.println("Cannot proceed with promotion and switchover if the master database would lose information when being reconfigured.");
                return false;
            }
            if (!this.forcePromote) {
                String prompt = "Would you like to attempt to promote the standby anyway [y/N]: ";
                String response = System.console().readLine(prompt, new Object[0]).trim();
                while (!"y".equalsIgnoreCase(response) && !"n".equalsIgnoreCase(response) && !response.isEmpty()) {
                    System.err.println("Please enter y or n.");
                    response = System.console().readLine(prompt, new Object[0]).trim();
                }
                if ("y".equalsIgnoreCase(response)) {
                    prompt = "Forcing this promotion may result in data loss - are you sure you want to continue? This is the last prompt. [y/N]: ";
                    response = System.console().readLine(prompt, new Object[0]).trim();
                    while (!"y".equalsIgnoreCase(response) && !"n".equalsIgnoreCase(response) && !response.isEmpty()) {
                        System.err.println("Please enter y or n.");
                        response = System.console().readLine(prompt, new Object[0]).trim();
                    }
                    if ("y".equalsIgnoreCase(response))
                        return true;
                }
                System.err.println("Promotion cancelled");
                return false;
            }
        }
        return true;
    }

    private String getAgentResponse(AdminServer.AdminCommand ac, String... params) throws IOException {
        Socket socket = new Socket("127.0.0.1", this.env.getAdminPort());
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        try {
            out.println(ac.name());
            String fromServer = in.readLine();
            while (fromServer != null) {
                if ("_param_needed_".equals(fromServer)) {
                    if (params.length == 0)
                        return "parameter missing from request";
                    StringBuilder sb = new StringBuilder();
                    for (String param : params) {
                        if (sb.length() != 0)
                            sb.append("|");
                        sb.append(param);
                    }
                    out.println(sb.toString());
                } else if ("_authorization_needed_".equals(fromServer)) {
                    String authText = getAuthText();
                    if (authText == null)
                        return "Could not read controller authorization text.";
                    out.println(getAuthText());
                } else {
                    if ("F_T_W".equals(fromServer))
                        return fromServer;
                    response.append(fromServer);
                }
                fromServer = in.readLine();
            }
            return response.toString();
        } finally {
            in.close();
            out.close();
            socket.close();
        }
    }

    private String getAuthText() {
        BufferedReader reader = null;
        try {
            FileInputStream stream = new FileInputStream(this.env.getAuthFileLocation());
            InputStreamReader sReader = new InputStreamReader(stream, "UTF-8");
            reader = new BufferedReader(sReader);
            String line = reader.readLine();
            if (line == null)
                return null;
            return line.trim();
        } catch (IOException e) {
            System.err.println("Could not read authorization text.");
            return null;
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

    private static final class SingleNodeInfo {
        final String ip;

        final boolean running;

        final boolean inRecovery;

        final String xlogLocation;

        final String info;

        private SingleNodeInfo(String ip, boolean inRecovery, boolean running, String xlogLocation, String info) {
            this.ip = ip;
            this.inRecovery = inRecovery;
            this.running = running;
            this.xlogLocation = xlogLocation;
            this.info = info;
        }

        public String getIp() {
            return this.ip;
        }

        public boolean isRunning() {
            return this.running;
        }

        public boolean isInRecovery() {
            return this.inRecovery;
        }

        public String getXlogLocation() {
            return this.xlogLocation;
        }

        public String getInfo() {
            return this.info;
        }
    }
}
