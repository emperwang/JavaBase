package com.enterprisedb.efm.admin;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.LogManager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author: wk
 * @Date: 2020/11/6 16:43
 * @Description
 */
public class AdminServerThread extends Thread{
    private static final Logger LOGGER = LogManager.getEfmLogger();

    private final Socket socket;

    private final EfmNode parentNode;

    private boolean hangup;

    private boolean expectingParam;

    private boolean requiresAuth;

    private String param;

    private AdminServer.AdminCommand currentCommand;

    public AdminServerThread(Socket socket) {
        super("AdminServerThread");
        this.parentNode = AdminServer.INSTANCE.getParentNode();
        this.socket = socket;
    }

    public void run() {
        try {
            PrintWriter sockOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8")), true);
            BufferedReader sockIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            String request;
            while ((request = sockIn.readLine()) != null) {
                if (!this.requiresAuth)
                    LOGGER.log(Level.FINE, "admin service received request: {0}", request);
                String response = processRequest(request);
                if (response != null) {
                    sockOut.println(response);
                    if (this.hangup)
                        break;
                }
            }
            this.socket.close();
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Exception: {0}", ioe.toString());
        }
        if (this.requiresAuth) {
            LOGGER.log(Level.SEVERE, "Authorization code not sent for command {0}.", this.currentCommand);
            return;
        }
        if (this.currentCommand == null)
            return;
        switch (this.currentCommand) {
            case ADD_NODE:
                doAddNode();
                return;
            case ALLOW_NODE:
                doAllowNode();
                return;
            case DISALLOW_NODE:
                doDisallowNode();
                return;
            case PROMOTE:
                this.parentNode.doManualPromotion(null);
                return;
            case REMOVE_NODE:
                doRemoveNode();
                return;
            case SET_PRIORITY:
                doSetPriority();
                return;
            case STOP:
                this.parentNode.shutdown();
                return;
            case STOP_CLUSTER:
                this.parentNode.shutdownCluster();
                return;
            case SWITCHOVER:
                this.parentNode.doSwitchover();
                return;
        }
        LOGGER.log(Level.WARNING, "Unknown command: {0}", this.currentCommand);
    }

    private String processRequest(String request) {
        if (this.requiresAuth) {
            this.requiresAuth = false;
            String authText = request.trim();
            if (this.parentNode.authenticateHCText(authText)) {
                LOGGER.log(Level.FINE, "Authenticated request.");
                if (this.expectingParam) {
                    this.hangup = false;
                    return "_param_needed_";
                }
                this.hangup = true;
                if (this.currentCommand == AdminServer.AdminCommand.STOP_CLUSTER)
                    return String.valueOf(this.parentNode.getCurrentNodeCount());
                if (this.currentCommand == AdminServer.AdminCommand.RESUME) {
                    this.currentCommand = null;
                    try {
                        this.parentNode.resumeMonitoring();
                        return "F_T_W";
                    } catch (Exception e) {
                        String msg = e.getMessage();
                        LOGGER.log(Level.WARNING, "Resume failed: {0}", msg);
                        return msg;
                    }
                }
                return "F_T_W";
            }
            LOGGER.log(Level.WARNING, "Could not authenticate request");
            this.hangup = true;
            this.currentCommand = null;
            return "Could not authorize this request.";
        }
        if (this.expectingParam) {
            this.param = request;
            this.expectingParam = false;
            this.hangup = true;
            LOGGER.log(Level.INFO, "Received param: {0}", this.param);
            if (this.param != null) {
                if (this.currentCommand == AdminServer.AdminCommand.ADD_NODE || this.currentCommand == AdminServer.AdminCommand.REMOVE_NODE) {
                    Environment env = Environment.getEnvironment();
                    String[] splitParams = this.param.split("\\|");
                    if (!env.validateIp(splitParams[0])) {
                        this.currentCommand = null;
                        String msg = "Could not parse IP address: " + splitParams[0];
                        LOGGER.log(Level.WARNING, msg);
                        return msg;
                    }
                    if (splitParams.length > 1)
                        try {
                            int priority = Integer.parseInt(splitParams[1]);
                            if (priority < 1) {
                                this.currentCommand = null;
                                String msg = "Failover priority must be at least 1";
                                LOGGER.log(Level.WARNING, "Failover priority must be at least 1");
                                return "Failover priority must be at least 1";
                            }
                        } catch (NumberFormatException nfe) {
                            this.currentCommand = null;
                            String msg = "Could not parse integer: " + splitParams[1];
                            LOGGER.log(Level.WARNING, msg);
                            return msg;
                        }
                }
                return "F_T_W";
            }
            return "_param_needed_";
        }
        AdminServer.AdminCommand command = AdminServer.AdminCommand.valueOf(request);
        this.hangup = true;
        this.expectingParam = false;
        this.requiresAuth = false;
        String response = null;
        switch (command) {
            case ADD_NODE:
                this.currentCommand = AdminServer.AdminCommand.ADD_NODE;
                LOGGER.log(Level.INFO, "Received add-node request.");
                this.requiresAuth = true;
                this.expectingParam = true;
                break;
            case ALLOW_NODE:
                this.currentCommand = AdminServer.AdminCommand.ALLOW_NODE;
                LOGGER.log(Level.INFO, "Received allow-node request.");
                this.requiresAuth = true;
                this.expectingParam = true;
                break;
            case DISALLOW_NODE:
                this.currentCommand = AdminServer.AdminCommand.DISALLOW_NODE;
                LOGGER.log(Level.INFO, "Received disallow-node request.");
                this.requiresAuth = true;
                this.expectingParam = true;
                break;
            case PROMOTE:
                this.currentCommand = AdminServer.AdminCommand.PROMOTE;
                LOGGER.log(Level.INFO, "Received promote request.");
                this.requiresAuth = true;
                break;
            case REMOVE_NODE:
                this.currentCommand = AdminServer.AdminCommand.REMOVE_NODE;
                LOGGER.log(Level.INFO, "Received remove-node request.");
                this.requiresAuth = true;
                this.expectingParam = true;
                break;
            case RESUME:
                this.currentCommand = AdminServer.AdminCommand.RESUME;
                LOGGER.log(Level.INFO, "Received resume request.");
                this.requiresAuth = true;
                break;
            case SET_PRIORITY:
                this.currentCommand = AdminServer.AdminCommand.SET_PRIORITY;
                LOGGER.log(Level.INFO, "Received set-priority request.");
                this.requiresAuth = true;
                this.expectingParam = true;
                break;
            case STATUS:
                response = getClusterStatus();
                LOGGER.log(Level.INFO, "admin service responding: {0}", response);
                break;
            case STOP:
                this.currentCommand = AdminServer.AdminCommand.STOP;
                LOGGER.log(Level.INFO, "Received stop request.");
                this.requiresAuth = true;
                break;
            case STOP_CLUSTER:
                this.currentCommand = AdminServer.AdminCommand.STOP_CLUSTER;
                LOGGER.log(Level.INFO, "Received stop request.");
                this.requiresAuth = true;
                break;
            case SWITCHOVER:
                this.currentCommand = AdminServer.AdminCommand.SWITCHOVER;
                LOGGER.log(Level.INFO, "Received switchover request.");
                this.requiresAuth = true;
                break;
            default:
                LOGGER.log(Level.INFO, "Unknown request, hanging up: {0}", request);
                response = null;
                break;
        }
        if (response == null)
            if (this.requiresAuth) {
                response = "_authorization_needed_";
            } else if (this.expectingParam) {
                response = "_param_needed_";
            }
        if (this.requiresAuth || this.expectingParam)
            this.hangup = false;
        return response;
    }

    private String getClusterStatus() {
        return this.parentNode.checkClusterStatus();
    }

    private void doAddNode() {
        int priority;
        if (this.param == null) {
            LOGGER.warning("Can not perform add-node with null param.");
            return;
        }
        String[] splitParams = this.param.split("\\|");
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (String p : splitParams)
                sb.append(" ").append(p);
            sb.append(" ]");
            LOGGER.log(Level.FINE, "original: {0} split: {1}", new Object[] { this.param, sb });
        } else {
            LOGGER.log(Level.INFO, "Performing add-node with param: {0}", this.param);
        }
        String address = splitParams[0];
        if (splitParams.length > 1) {
            try {
                priority = Integer.parseInt(splitParams[1]);
            } catch (NumberFormatException nfe) {
                LOGGER.warning("Priority could not be parsed. Will not add to priority list.");
                priority = 0;
            }
        } else {
            priority = 0;
        }
        this.parentNode.addNode(address, priority);
    }

    private void doRemoveNode() {
        if (this.param == null) {
            LOGGER.warning("Can not perform remove-node with null param.");
            return;
        }
        LOGGER.log(Level.INFO, "Performing remove-node with param: {0}", this.param);
        this.parentNode.removeNode(this.param);
    }

    private void doAllowNode() {
        if (this.param == null) {
            LOGGER.warning("Can not perform allow-node with null param.");
            return;
        }
        LOGGER.log(Level.INFO, "Performing add-node with param: {0}", this.param);
        this.parentNode.allowNode(this.param);
    }

    private void doDisallowNode() {
        if (this.param == null) {
            LOGGER.warning("Can not perform disallow-node with null param.");
            return;
        }
        LOGGER.log(Level.INFO, "Performing disallow-node with param: {0}", this.param);
        this.parentNode.disallowNode(this.param);
    }

    private void doSetPriority() {
        int priority;
        if (this.param == null) {
            LOGGER.warning("Can not perform set-priority with null param.");
            return;
        }
        String[] splitParams = this.param.split("\\|");
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (String p : splitParams)
                sb.append(" ").append(p);
            sb.append(" ]");
            LOGGER.log(Level.FINE, "original: {0} split: {1}", new Object[] { this.param, sb });
        } else {
            LOGGER.log(Level.INFO, "Performing set-priority with param: {0}", this.param);
        }
        String address = splitParams[0];
        try {
            priority = Integer.parseInt(splitParams[1]);
        } catch (NumberFormatException nfe) {
            LOGGER.warning("Priority could not be parsed. Will not add to priority list.");
            priority = 0;
        }
        this.parentNode.setPriority(address, priority);
    }
}
