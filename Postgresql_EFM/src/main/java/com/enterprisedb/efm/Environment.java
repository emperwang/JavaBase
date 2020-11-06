//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.enterprisedb.efm;

import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import com.enterprisedb.efm.main.SubCommand;
import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import com.enterprisedb.efm.utils.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.jgroups.Event;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.PhysicalAddress;
import org.jgroups.auth.AuthToken;
import org.jgroups.auth.FixedMembershipToken;
import org.jgroups.protocols.AUTH;
import org.jgroups.protocols.BARRIER;
import org.jgroups.protocols.FD_ALL;
import org.jgroups.protocols.FRAG2;
import org.jgroups.protocols.MERGE3;
import org.jgroups.protocols.MFC;
import org.jgroups.protocols.TCP;
import org.jgroups.protocols.TCPPING;
import org.jgroups.protocols.UNICAST3;
import org.jgroups.protocols.VERIFY_SUSPECT;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.protocols.pbcast.STATE_TRANSFER;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.Protocol;
import org.jgroups.stack.ProtocolStack;

public enum Environment {
    INSTANCE;

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String SSL_VERIFY_CA = "verify-ca";
    public static final String SSL_REQUIRE = "require";
    private static final String[] SSL_MODES = new String[]{"verify-ca", "require"};
    private static final String SSL_DEFAULT_MODE = "verify-ca";
    private final Properties properties = new Properties();
    private File propsFile;
    private boolean initialized = false;
    private String bindingAddress;
    private int bindingPort;
    private String clusterName;
    private List<String> initialNodes = new ArrayList();
    private final transient Object initLock = new Object();
    private boolean isWitness = false;
    static final String AUTH_SEP = ",";
    private int viewChangeWindowSeconds = 300;
    private String sslMode = "verify-ca";
    private boolean v6vip = false;
    static final String DEFAULT_SUDO = "sudo";
    static final String DEFAULT_USER_SUDO = "sudo -u %u";

    private Environment() {
    }

    public static Environment getEnvironment() {
        return INSTANCE;
    }

    public boolean init(String propsFileLocation) {
        return this.init(propsFileLocation, (SubCommand)null);
    }

    public boolean init(String propsFileLocation, SubCommand command) {
        synchronized(this.initLock) {
            if (this.initialized) {
                throw new IllegalStateException("Environment already initialized.");
            } else {
                boolean var4;
                try {
                    var4 = this.doInit(propsFileLocation, command);
                } finally {
                    this.initialized = true;
                }

                return var4;
            }
        }
    }

    private boolean doInit(String propsFileLocation, SubCommand command) {
        if (this.initialized) {
            throw new IllegalStateException("doInit: Environment already initialized.");
        } else {
            this.propsFile = new File(propsFileLocation);
            this.clusterName = this.propsFile.getName().split("\\.")[0];
            if (!this.loadPropsFile(this.properties, this.propsFile)) {
                return false;
            } else {
                boolean allValid = true;
                if (EfmProps.EFM_IS_WITNESS.isRequiredByAgent() && EfmProps.EFM_IS_WITNESS.isRequiredByWitness()) {
                    this.isWitness = Boolean.parseBoolean(this.properties.getProperty(EfmProps.EFM_IS_WITNESS.getPropName()));
                    EfmProps[] arr$ = EfmProps.values();
                    int len$ = arr$.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        EfmProps prop = arr$[i$];
                        String val = this.properties.getProperty(prop.getPropName());
                        if (this.isWitness) {
                            if (prop.isRequiredByWitness() && (val == null || val.trim().length() == 0)) {
                                System.err.println("ERROR: Required property '" + prop.getPropName() + "' is not set.");
                                allValid = false;
                            }
                        } else if (prop.isRequiredByAgent() && (val == null || val.trim().length() == 0)) {
                            System.err.println("ERROR: Required property '" + prop.getPropName() + "' is not set.");
                            allValid = false;
                        }

                        if (val != null) {
                            String valTrimmed = val.trim();
                            if (valTrimmed.startsWith("'") || valTrimmed.startsWith("\"") || valTrimmed.endsWith("'") || valTrimmed.endsWith("\"")) {
                                System.err.println("ERROR: The value for '" + prop.getPropName() + "' cannot be enclosed in quotes.");
                                allValid = false;
                            }

                            this.properties.setProperty(prop.getPropName(), valTrimmed);
                        }
                    }

                    String bindingAddressPort;
                    if (SubCommand.INT_START == command) {
                        LogManager.init();
                        bindingAddressPort = propsFileLocation.replace(".properties", ".nodes");
                        File nodesFile = new File(bindingAddressPort);
                        if (!nodesFile.exists()) {
                            System.err.println("Cannot find file: " + bindingAddressPort);
                            return false;
                        }

                        if (!this.parseNodesFile()) {
                            return false;
                        }

                        allValid &= this.validatePasswordLength();
                    }

                    if (this.isVipEnabled()) {
                        this.v6vip = Utils.isIPv6(this.getVirtualIp());
                    }

                    allValid &= this.validateIpWithPort(EfmProps.EFM_BIND_ADDRESS);
                    allValid &= this.validateIp(EfmProps.EFM_VIRT_IP);
                    allValid &= this.validatePort(EfmProps.ADMIN_PORT);
                    allValid &= this.validatePort(EfmProps.DB_PORT);
                    allValid &= this.validateTrueFalse(EfmProps.AUTO_FAILOVER);
                    allValid &= this.validateTrueFalse(EfmProps.EFM_IS_WITNESS);
                    allValid &= this.validateTrueFalse(EfmProps.AUTO_ALLOW_HOSTS);
                    allValid &= this.validateTrueFalse(EfmProps.STANDBY_AVAIL);
                    if (this.isVipEnabled()) {
                        allValid &= this.validateNetmask(EfmProps.EFM_VIRT_IP_NETMASK);
                        allValid &= this.validateInterfaceName(EfmProps.EFM_VIRT_IP_INTERFACE);
                    }

                    if (!allValid) {
                        return false;
                    } else {
                        bindingAddressPort = this.properties.getProperty(EfmProps.EFM_BIND_ADDRESS.getPropName());
                        this.bindingAddress = this.getHostPart(bindingAddressPort);
                        this.bindingPort = Integer.parseInt(this.getPortPart(bindingAddressPort));
                        if (SubCommand.INT_START == command) {
                            if (!this.isWitness()) {
                                if (this.getUserSudoCommand().equals("sudo -u %u".replaceAll("%u", this.getDBServiceOwner()))) {
                                    allValid = this.validateDbOwner() && this.validateRecoveryConfFile();
                                } else {
                                    LogManager.getEfmLogger().log(Level.INFO, "Skipping db owner/sudo validation check.");
                                }

                                allValid &= this.validateBinOrService();
                                allValid &= this.validateFinalTimeout();
                                allValid &= this.validateRecoveryPeriod();
                                allValid &= this.validateAutoResumePeriod();
                                allValid &= this.validateScriptPath(EfmProps.EFM_FENCE_SCRIPT);
                                allValid &= this.validateScriptPath(EfmProps.EFM_POST_PROMOTE_SCRIPT);
                                allValid &= this.validateScriptPath(EfmProps.EFM_DB_FAIL_SCRIPT);
                                allValid &= this.validateScriptPath(EfmProps.EFM_MASTER_ISO_SCRIPT);
                                allValid &= this.validateScriptPath(EfmProps.EFM_RESUME_SCRIPT);
                                allValid &= this.validateInterfaceExists();
                            }

                            allValid &= this.bindingAddressLocal();
                            allValid &= this.validateScriptPath(EfmProps.EFM_NOTIFICATION_SCRIPT);
                        }

                        allValid &= this.validateTimeoutPeriod();
                        allValid &= this.validateRemoteTimeout();
                        allValid &= this.validateJGroupsTotalTimeout();
                        allValid &= this.validateConnReuseCount();
                        allValid &= this.validateEmailAddrOrScript();
                        allValid &= this.validateEmailSyntax();
                        allValid &= this.validateLogLevels();
                        allValid &= this.validateSslMode();
                        allValid &= this.validateMinStandbys();
                        long jgroupsTimeoutSeconds = this.getJGroupsTotalTimeout();
                        if (jgroupsTimeoutSeconds * 2L > (long)this.viewChangeWindowSeconds) {
                            this.viewChangeWindowSeconds = (int)jgroupsTimeoutSeconds * 2;
                        }

                        this.checkLocalRemoteTimeouts();
                        return allValid;
                    }
                } else {
                    throw new AssertionError(EfmProps.EFM_IS_WITNESS.getPropName() + " property must be required for all nodes.");
                }
            }
        }
    }

    private boolean parseNodesFile() {
        String command = this.getRootSudoCommand() + " " + this.getRootFunctionsScript() + " " + SudoFunctions.READ_NODES + " " + this.clusterName;
        ProcessResult result = ExecUtil.performExec(false, new String[]{command});
        if (result.getExitValue() == 0) {
            String output = result.getStdOut();
            BufferedReader reader = new BufferedReader(new StringReader(output));

            try {
                for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                    String upToComment = line.split("#", -1)[0].trim();
                    if (!upToComment.isEmpty()) {
                        String[] nodes = upToComment.split("\\s+");
                        String[] arr$ = nodes;
                        int len$ = nodes.length;

                        for(int i$ = 0; i$ < len$; ++i$) {
                            String node = arr$[i$];
                            if (!this.doIpPortValidation(node, (String)null)) {
                                System.err.println("ERROR: Node value '" + node + "' must be of the form <ip>:<port>.");
                                return false;
                            }

                            this.initialNodes.add(node);
                        }
                    }
                }

                return true;
            } catch (IOException var12) {
                System.err.println("Could not read nodes file: " + var12);
                return false;
            }
        } else {
            System.err.println("Could not parse nodes file : ");
            StringBuilder outputBuilder = new StringBuilder();
            result.addNiceOutput(outputBuilder);
            System.err.println(command + "\n" + outputBuilder.toString());
            return false;
        }
    }

    private boolean loadPropsFile(Properties p, File file) {
        FileInputStream input = null;

        boolean var5;
        try {
            input = new FileInputStream(file);
            p.load(input);
            return true;
        } catch (IOException var15) {
            System.err.println("Could not read properties file: " + file.getAbsolutePath());
            var5 = false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var14) {
                    System.err.println("Closing props stream: " + var14);
                }
            }

        }

        return var5;
    }

    public JChannel createJChannel(EfmNode efmNode) throws Exception {
        JChannel channel = new JChannel(false);
        ProtocolStack stack = new ProtocolStack();
        channel.setProtocolStack(stack);
        stack.addProtocol((new TCP()).setValue("oob_thread_pool_keep_alive_time", 5000).setValue("timer_keep_alive_time", 3000).setValue("bind_addr", InetAddress.getByName(this.getBindingAddress())).setValue("bind_port", this.bindingPort).setValue("thread_pool_min_threads", 1).setValue("thread_pool_keep_alive_time", 5000).setValue("send_buf_size", 640000).setValue("oob_thread_pool_queue_max_size", 100).setValue("oob_thread_pool_max_threads", 8).setValue("thread_pool_queue_enabled", false).setValue("sock_conn_timeout", 300).setValue("oob_thread_pool_min_threads", 1).setValue("oob_thread_pool_queue_enabled", false).setValue("max_bundle_timeout", 30).setValue("thread_pool_queue_max_size", 100).setValue("recv_buf_size", 5000000)).addProtocol((new TCPPING()).setValue("initial_hosts", this.parseHostList()).setValue("send_cache_on_join", true).setValue("port_range", 0)).addProtocol((new MERGE3()).setValue("min_interval", 10000).setValue("max_interval", 30000)).addProtocol((new FD_ALL()).setValue("timeout", this.getJGroupsTotalTimeout() * 1000L)).addProtocol((new VERIFY_SUSPECT()).setValue("timeout", 1500)).addProtocol(new BARRIER()).addProtocol((new NAKACK2()).setValue("use_mcast_xmit", false)).addProtocol(new UNICAST3()).addProtocol((new STABLE()).setValue("desired_avg_gossip", 50000).setValue("max_bytes", 4000000).setValue("stability_delay", 1000)).addProtocol(this.createAuthProtocol(efmNode)).addProtocol((new GMS()).setValue("join_timeout", 3000)).addProtocol((new MFC()).setValue("max_credits", 2000000).setValue("min_credits", 800000)).addProtocol(new FRAG2()).addProtocol(new STATE_TRANSFER());
        stack.init();
        return channel;
    }

    public Properties getProperties() {
        return new Properties(this.properties);
    }

    public synchronized void reloadProperties(EfmProps... reloadProps) throws IOException {
        FileInputStream input = null;

        try {
            Properties tempProps = new Properties();
            input = new FileInputStream(this.propsFile);
            tempProps.load(input);
            EfmProps[] arr$ = reloadProps;
            int len$ = reloadProps.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                EfmProps reloadProp = arr$[i$];
                String newValue = tempProps.getProperty(reloadProp.getPropName());
                if (newValue != null) {
                    this.properties.setProperty(reloadProp.getPropName(), newValue.trim());
                } else {
                    this.properties.remove(reloadProp.getPropName());
                }
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var14) {
                    LogManager.getEfmLogger().log(Level.WARNING, "Could not close properties file input stream: {0}", var14.toString());
                }
            }

        }

    }

    public String getAddressScript() {
        return "/usr/efm-2.1/bin/efm_address";
    }

    public String getDBFunctionsScript() {
        return "/usr/efm-2.1/bin/efm_db_functions";
    }

    public String getRootFunctionsScript() {
        return "/usr/efm-2.1/bin/efm_root_functions";
    }

    public String getAuthFileLocation() {
        return "/var/run/efm-2.1." + this.getClusterName() + ".key";
    }

    public String getFencingScript() {
        String val = this.properties.getProperty(EfmProps.EFM_FENCE_SCRIPT.getPropName());
        return val != null && !val.isEmpty() ? val : null;
    }

    public String getNotificationScript() {
        String val = this.properties.getProperty(EfmProps.EFM_NOTIFICATION_SCRIPT.getPropName());
        return val != null && val.length() != 0 ? val : null;
    }

    public String getRootSudoCommand() {
        String val = this.properties.getProperty(EfmProps.SUDO_COMMAND.getPropName());
        return val != null && !val.isEmpty() ? val : "sudo";
    }

    public String getUserSudoCommand() {
        String val = this.properties.getProperty(EfmProps.SUDO_USER_COMMAND.getPropName());
        return val != null && !val.isEmpty() ? val.trim().replaceAll("%u", this.getDBServiceOwner()) : "sudo -u %u".replaceAll("%u", this.getDBServiceOwner());
    }

    public boolean useDbService() {
        return !this.propEmpty(EfmProps.DB_SERVICE_NAME);
    }

    public String getPostPromotionScript() {
        String val = this.properties.getProperty(EfmProps.EFM_POST_PROMOTE_SCRIPT.getPropName());
        return val != null && !val.isEmpty() ? val : null;
    }

    public String getResumedScript() {
        String val = this.properties.getProperty(EfmProps.EFM_RESUME_SCRIPT.getPropName());
        return val != null && !val.isEmpty() ? val : null;
    }

    public String getPostDbFailScript() {
        String val = this.properties.getProperty(EfmProps.EFM_DB_FAIL_SCRIPT.getPropName());
        return val != null && !val.isEmpty() ? val : null;
    }

    public String getMasterIsolatedScript() {
        String val = this.properties.getProperty(EfmProps.EFM_MASTER_ISO_SCRIPT.getPropName());
        return val != null && val.length() != 0 ? val : null;
    }

    public int getAdminPort() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.ADMIN_PORT.getPropName()));
    }

    public String getRecoveryConfLocation() {
        return this.properties.getProperty(EfmProps.DB_RECOVERY_CONF_DIR.getPropName());
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public String getDBServiceOwner() {
        return this.properties.getProperty(EfmProps.DB_SERVICE_OWNER.getPropName());
    }

    public int getDBPort() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.DB_PORT.getPropName()));
    }

    public String getDBUser() {
        return this.properties.getProperty(EfmProps.DB_USER.getPropName());
    }

    public String getDBPassword() {
        return this.properties.getProperty(EfmProps.DB_PASSWORD.getPropName());
    }

    public String getDBDatabase() {
        return this.properties.getProperty(EfmProps.DB_DATABASE.getPropName());
    }

    public String getBindingAddress() {
        return this.bindingAddress;
    }

    public int getBindingPort() {
        return this.bindingPort;
    }

    public String getVirtualIp() {
        return this.properties.getProperty(EfmProps.EFM_VIRT_IP.getPropName());
    }

    public boolean isVipV6() {
        return this.v6vip;
    }

    public String getVirtualIpNetmask() {
        return this.properties.getProperty(EfmProps.EFM_VIRT_IP_NETMASK.getPropName());
    }

    public String getVirtualIpInterface() {
        return this.properties.getProperty(EfmProps.EFM_VIRT_IP_INTERFACE.getPropName());
    }

    public String getPingServer() {
        return this.properties.getProperty(EfmProps.EFM_PING_SERVER.getPropName());
    }

    public String getPingServerCommand() {
        return this.properties.getProperty(EfmProps.EFM_PING_SERVER_COMMAND.getPropName());
    }

    public String[] getUserEmails() {
        String rawVal = this.properties.getProperty(EfmProps.USER_EMAIL.getPropName());
        return rawVal != null && !rawVal.isEmpty() ? rawVal.split("\\s+") : null;
    }

    public int getReuseDbConnectionCount() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.DB_REUSE_CONNECTION_COUNT.getPropName()));
    }

    public int getLocalDbTimeout() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.LOCAL_TIMEOUT.getPropName()));
    }

    public int getLocalDbPeriod() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.LOCAL_PERIOD.getPropName()));
    }

    public int getLocalFinalTimeout() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.LOCAL_FINAL_TIMEOUT.getPropName()));
    }

    public long getRemoteDbTimeout() {
        return Long.parseLong(this.properties.getProperty(EfmProps.REMOTE_TIMEOUT.getPropName()));
    }

    public long getRecoveryCheckPeriod() {
        return Long.parseLong(this.properties.getProperty(EfmProps.RECOVERY_CHECK_PERIOD.getPropName()));
    }

    public long getAutoResumePeriod() {
        return Long.parseLong(this.properties.getProperty(EfmProps.AUTO_RESUME_PERIOD.getPropName()));
    }

    public int getMinStandbys() {
        return Integer.parseInt(this.properties.getProperty(EfmProps.MIN_STANDBYS.getPropName()));
    }

    public long getJGroupsTotalTimeout() {
        return (long)Integer.parseInt(this.properties.getProperty(EfmProps.JGROUPS_TOTAL_TIMEOUT.getPropName()));
    }

    public boolean getJdbcSsl() {
        return Boolean.parseBoolean(this.properties.getProperty(EfmProps.JDBC_SSL.getPropName()));
    }

    public String getJdbcSslMode() {
        return this.sslMode;
    }

    public String[] getJvmOptions() {
        String rawVal = this.properties.getProperty(EfmProps.JVM_OPTIONS.getPropName());
        return rawVal != null && !rawVal.isEmpty() ? rawVal.split("\\s+") : new String[0];
    }

    public boolean autoFailoverOn() {
        return Boolean.parseBoolean(this.properties.getProperty(EfmProps.AUTO_FAILOVER.getPropName()));
    }

    public boolean autoReconfigure() {
        return Boolean.parseBoolean(this.properties.getProperty(EfmProps.AUTO_STANDBY_RECONFIGURE.getPropName()));
    }

    public boolean autoAllowHosts() {
        return Boolean.parseBoolean(this.properties.getProperty(EfmProps.AUTO_ALLOW_HOSTS.getPropName()));
    }

    public boolean isPromotable() {
        return Boolean.parseBoolean(this.properties.getProperty(EfmProps.STANDBY_AVAIL.getPropName()));
    }

    public boolean isWitness() {
        return this.isWitness;
    }

    public boolean readTriggerFileLocation() {
        Logger logger = LogManager.getEfmLogger();
        logger.log(Level.INFO, "Reading trigger file location from recovery.conf file.");
        ProcessResult result = ExecUtil.performExec(new String[]{this.getUserSudoCommand() + " " + this.getDBFunctionsScript() + " " + SudoFunctions.READ_TRIGGER_FILE_LOCATION + " " + this.getClusterName()});
        String triggerFile = result.getStdOut().trim();
        if (result.getExitValue() == 0 && !triggerFile.isEmpty() && !"''".equals(triggerFile)) {
            logger.log(Level.INFO, "Current trigger file location for promotion: {0}", triggerFile);
            return true;
        } else {
            System.err.println("Could not read the trigger file location from recovery.conf in directory specified in properties file. See log for more details.");
            logger.log(Level.SEVERE, "Could not read the trigger file location from recovery.conf in directory specified in properties file.");
            return false;
        }
    }

    public List<String> getInitialNodeAddresses() {
        List<String> retVal = new ArrayList();
        Iterator i$ = this.initialNodes.iterator();

        while(i$.hasNext()) {
            String initialNode = (String)i$.next();
            retVal.add(this.getHostPart(initialNode));
        }

        return retVal;
    }

    private Protocol createAuthProtocol(EfmNode efmNode) throws UnknownHostException {
        Environment.DynamicMembershipToken dmt = new Environment.DynamicMembershipToken();
        dmt.setFixedMembersSeparator(",");
        dmt.setMemberList("");
        AUTH auth = new AUTH();
        auth.setAuthToken(dmt);
        dmt.setAuth(auth);
        dmt.setNode(efmNode);
        return auth;
    }

    private List<IpAddress> parseHostList() throws UnknownHostException {
        List<IpAddress> retVal = new ArrayList();
        List<String> nodeAddresses = this.getNodeAddresses();
        Iterator i$ = nodeAddresses.iterator();

        while(i$.hasNext()) {
            String node = (String)i$.next();
            retVal.add(new IpAddress(this.getHostPart(node), Integer.parseInt(this.getPortPart(node))));
        }

        Logger logger = LogManager.getEfmLogger();
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "Host list: {0}", retVal);
        }

        return retVal;
    }

    public boolean isVipEnabled() {
        String vip = this.getVirtualIp();
        return vip != null && !vip.isEmpty();
    }

    private boolean validateDbOwner() {
        String command = this.getRootSudoCommand() + " " + this.getRootFunctionsScript() + " " + SudoFunctions.VALIDATE_DB_OWNER + " " + this.getClusterName();
        ProcessResult result = ExecUtil.performExec(false, new String[]{command});
        if (result.getExitValue() == 0) {
            return true;
        } else {
            System.err.println("Could not validate " + EfmProps.DB_SERVICE_OWNER.getPropName() + " value: ");
            StringBuilder outputBuilder = new StringBuilder();
            result.addNiceOutput(outputBuilder);
            System.err.println(command + "\n" + outputBuilder.toString());
            return false;
        }
    }

    private boolean validateRecoveryConfFile() {
        if (this.isWitness()) {
            return true;
        } else {
            String command = this.getUserSudoCommand() + " " + this.getDBFunctionsScript() + " " + SudoFunctions.VALIDATE_RECOVERY_CONF + " " + this.getClusterName();
            ProcessResult result = ExecUtil.performExec(false, new String[]{command});
            if (result.getExitValue() == 0) {
                return true;
            } else {
                System.err.println("Could not validate recovery conf file:");
                StringBuilder outputBuilder = new StringBuilder();
                result.addNiceOutput(outputBuilder);
                System.err.println(command + "\n" + outputBuilder.toString());
                return false;
            }
        }
    }

    private boolean validateBinOrService() {
        if (this.propEmpty(EfmProps.DB_PG_BIN_DIR) && this.propEmpty(EfmProps.DB_SERVICE_NAME)) {
            System.err.println(String.format("Either the %s or %s property must be set.", EfmProps.DB_PG_BIN_DIR.getPropName(), EfmProps.DB_SERVICE_NAME.getPropName()));
            return false;
        } else {
            return this.propEmpty(EfmProps.DB_PG_BIN_DIR) || this.validateDbBinDir();
        }
    }

    private boolean validateDbBinDir() {
        String command = this.getUserSudoCommand() + " " + this.getDBFunctionsScript() + " " + SudoFunctions.VALIDATE_DB_BIN + " " + this.getClusterName();
        ProcessResult result = ExecUtil.performExec(false, new String[]{command});
        if (result.getExitValue() == 0) {
            return true;
        } else {
            System.err.println("Could not validate DbBinDir :");
            StringBuilder outputBuilder = new StringBuilder();
            result.addNiceOutput(outputBuilder);
            System.err.println(command + "\n" + outputBuilder.toString());
            return false;
        }
    }

    private boolean validateRecoveryPeriod() {
        try {
            if (this.getRecoveryCheckPeriod() > 0L) {
                return true;
            } else {
                System.err.println("The 'recovery.check.period' value must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'recovery.check.period' value must be an integer.");
            return false;
        }
    }

    private boolean validateAutoResumePeriod() {
        try {
            if (this.getAutoResumePeriod() >= 0L) {
                return true;
            } else {
                System.err.println("The 'auto.resume.period' value must be >= zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'auto.resume.period' value must be an integer.");
            return false;
        }
    }

    private boolean validateTimeoutPeriod() {
        if (this.isWitness) {
            if (!EfmProps.LOCAL_TIMEOUT.isRequiredByWitness()) {
                return true;
            }

            System.err.println("Remove witness check in validateTimeoutPeriod().");
        }

        try {
            if (this.getLocalDbTimeout() > this.getLocalDbPeriod()) {
                if (this.getLocalDbTimeout() < 1) {
                    System.err.println("The 'local.timeout' property must be at least 1.");
                    return false;
                } else if (this.getLocalDbPeriod() < 1) {
                    System.err.println("The 'local.period' property must be at least 1.");
                    return false;
                } else {
                    return true;
                }
            } else {
                System.err.println("The 'local.timeout' property must be set higher than the 'local.period' property.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'local.timeout' and 'local.period' properties must both be integers.");
            return false;
        }
    }

    private boolean validateFinalTimeout() {
        try {
            if (this.getLocalFinalTimeout() > 0) {
                return true;
            } else {
                System.err.println("The 'local.timeout.final' value must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'local.timeout.final' value must be an integer.");
            return false;
        }
    }

    private boolean validateSslMode() {
        if (!this.getJdbcSsl()) {
            return true;
        } else {
            String sslModeRaw = this.properties.getProperty(EfmProps.JDBC_SSL_MODE.getPropName());
            if (sslModeRaw != null && !sslModeRaw.isEmpty()) {
                String[] arr$ = SSL_MODES;
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String mode = arr$[i$];
                    if (mode.equals(sslModeRaw)) {
                        this.sslMode = sslModeRaw;
                        System.out.println("Using ssl mode: " + this.sslMode);
                        return true;
                    }
                }

                System.err.println("Unknown ssl mode: " + sslModeRaw);
                return false;
            } else {
                System.out.println("Using default ssl mode: verify-ca");
                return true;
            }
        }
    }

    private boolean validateConnReuseCount() {
        if (this.isWitness) {
            if (!EfmProps.DB_REUSE_CONNECTION_COUNT.isRequiredByWitness()) {
                return true;
            }

            System.err.println("Remove witness check in validateConnReuseCount().");
        }

        try {
            if (this.getReuseDbConnectionCount() >= 0) {
                return true;
            } else {
                System.err.println("The 'db.reuse.connection.count' value must be greater than or equal to zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'db.reuse.connection.count' value must be an integer.");
            return false;
        }
    }

    private boolean validateRemoteTimeout() {
        try {
            if (this.getRemoteDbTimeout() > 0L) {
                return true;
            } else {
                System.err.println("The 'remote.timeout' value must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'remote.timeout' value must be an integer.");
            return false;
        }
    }

    private boolean validateMinStandbys() {
        try {
            if (this.getMinStandbys() >= 0) {
                return true;
            } else {
                System.err.println("The 'minimum.standbys' value must be at least zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'minimum.standbys' value must be an integer.");
            return false;
        }
    }

    private void checkLocalRemoteTimeouts() {
        if (this.getRemoteDbTimeout() > (long)this.getLocalDbTimeout()) {
            String localValue = Long.toString((long)this.getLocalDbTimeout());
            String remoteValue = Long.toString(this.getRemoteDbTimeout());
            String local = EfmProps.LOCAL_TIMEOUT.getPropName();
            String remote = EfmProps.REMOTE_TIMEOUT.getPropName();
            String msg = String.format("The %s value %s is less than the %s value %s", local, localValue, remote, remoteValue);
            System.err.println("WARNING: " + msg);
            LogManager.getEfmLogger().warning(msg);
            Notifications.REMOTE_TIMEOUT_TOO_HIGH.addBodyParams(new String[]{remote, remoteValue, local, localValue}).send();
        }

    }

    private boolean validateJGroupsTotalTimeout() {
        try {
            if (this.getJGroupsTotalTimeout() > 0L) {
                return true;
            } else {
                System.err.println("The 'jgroups.timeout' value must be greater than zero.");
                return false;
            }
        } catch (NumberFormatException var2) {
            System.err.println("The 'jgroups.timeout' value must be an integer.");
            return false;
        }
    }

    private boolean validateEmailAddrOrScript() {
        String[] emails = this.getUserEmails();
        String script = this.getNotificationScript();
        if (emails != null || script != null && !script.isEmpty()) {
            return true;
        } else {
            System.err.println("The 'user.email' or 'script.notification' property must be set.");
            return false;
        }
    }

    private boolean validateEmailSyntax() {
        String[] emails = this.getUserEmails();
        if (emails == null) {
            return true;
        } else {
            boolean retVal = true;
            String[] arr$ = emails;
            int len$ = emails.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String email = arr$[i$];

                try {
                    InternetAddress addr = new InternetAddress(email);
                    addr.validate();
                } catch (AddressException var8) {
                    System.err.println("The 'user.email' value " + email + " must be valid syntax.");
                    retVal = false;
                }
            }

            return retVal;
        }
    }

    private boolean validateLogLevels() {
        String jgroupsLevel = this.properties.getProperty("jgroups.loglevel");
        String efmLevel = this.properties.getProperty("efm.loglevel");
        boolean retVal = true;
        if (jgroupsLevel != null && !jgroupsLevel.isEmpty()) {
            try {
                Level.parse(jgroupsLevel);
            } catch (IllegalArgumentException var6) {
                System.err.println("The 'jgroups.loglevel' value could not be parsed: " + jgroupsLevel);
                retVal = false;
            }
        }

        if (efmLevel != null && !efmLevel.isEmpty()) {
            try {
                Level.parse(efmLevel);
            } catch (IllegalArgumentException var5) {
                System.err.println("The 'efm.loglevel' value could not be parsed: " + efmLevel);
                retVal = false;
            }
        }

        return retVal;
    }

    private boolean validatePasswordLength() {
        if (this.getDBPassword().length() % 2 != 0) {
            System.err.println("Encrypted password format unknown. Please check that password is properly encrypted in properties file.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateIpWithPort(EfmProps prop) {
        String targetVal = this.properties.getProperty(prop.getPropName());
        String errMsg = "ERROR: '" + prop.getPropName() + "' must contain a value of the form <ip>:<port> " + "(0 < port <= 65535), not " + targetVal + ".";
        return this.doIpPortValidation(targetVal, errMsg);
    }

    private boolean doIpPortValidation(String targetVal, String msgForStdErr) {
        if (targetVal != null) {
            try {
                if (targetVal.contains(":")) {
                    String addr = this.getHostPart(targetVal);
                    String portVal = this.getPortPart(targetVal);
                    if (this.validateIp(addr)) {
                        int port = Integer.parseInt(portVal);
                        if (0 < port && port <= 65535) {
                            return true;
                        }
                    }
                }
            } catch (NumberFormatException var6) {
                if (msgForStdErr != null) {
                    System.err.println(msgForStdErr);
                }

                return false;
            }
        }

        if (msgForStdErr != null) {
            System.err.println(msgForStdErr);
        }

        return false;
    }

    private boolean validateIp(EfmProps prop) {
        String val = this.properties.getProperty(prop.getPropName());
        boolean nullOrEmpty = val == null || val.isEmpty();
        if (nullOrEmpty) {
            if (this.isWitness && !prop.isRequiredByWitness()) {
                return true;
            } else if (!prop.isRequiredByAgent()) {
                return true;
            } else {
                System.err.println("ERROR: '" + prop.getPropName() + "' must contain a value of the form <IPv4 | IPv6>.");
                return false;
            }
        } else if (this.validateIp(val)) {
            return true;
        } else {
            System.err.println("ERROR: '" + prop.getPropName() + "' must contain a value of the form <IPv4 | IPv6>, not " + val + ".");
            return false;
        }
    }

    private boolean validatePort(EfmProps prop) {
        String val = this.properties.getProperty(prop.getPropName());
        String errMsg = "ERROR: '" + prop.getPropName() + "' must contain a port value (0 < port <= 65535), not " + val + ".";
        if (val != null) {
            try {
                int port = Integer.parseInt(val);
                if (0 < port && port <= 65535) {
                    return true;
                }
            } catch (NumberFormatException var5) {
                System.err.println(errMsg);
                return false;
            }
        }

        System.err.println(errMsg);
        return false;
    }

    private boolean validateTrueFalse(EfmProps prop) {
        String val = this.properties.getProperty(prop.getPropName());
        if (!"true".equalsIgnoreCase(val) && !"false".equalsIgnoreCase(val)) {
            System.err.println("ERROR: '" + prop.getPropName() + "' must be either true or false.");
            return false;
        } else {
            return true;
        }
    }

    public boolean validateIp(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr != null;
        } catch (UnknownHostException var4) {
            return false;
        }
    }

    private boolean validateNetmask(EfmProps prop) {
        String val = this.properties.getProperty(prop.getPropName());
        if (val != null && !val.isEmpty()) {
            if (this.isVipV6()) {
                try {
                    int length = Integer.parseInt(val);
                    int min = false;
                    if (length < 0) {
                        System.err.println("ERROR: '" + prop.getPropName() + " must be above " + 0);
                        return false;
                    } else {
                        return true;
                    }
                } catch (NumberFormatException var5) {
                    System.err.println("ERROR: '" + prop.getPropName() + " must be an integer.");
                    return false;
                }
            } else if (this.validateIp(val)) {
                return true;
            } else {
                System.err.println("ERROR: '" + prop.getPropName() + "' must contain a value of the form <ip>, not " + val + ".");
                return false;
            }
        } else {
            System.err.println("ERROR: '" + prop.getPropName() + "' must be set.");
            return false;
        }
    }

    private boolean validateInterfaceName(EfmProps prop) {
        String val = this.properties.getProperty(prop.getPropName());
        if (val != null && !val.isEmpty()) {
            if (this.isVipV6()) {
                if (val.contains(":")) {
                    System.err.println("ERROR: '" + prop.getPropName() + "' must not include a virtual interface id.");
                    return false;
                }
            } else if (!val.contains(":")) {
                System.err.println("ERROR: '" + prop.getPropName() + "' must include a virtual interface id (ie ':1'). Not '" + val + "'.");
                return false;
            }

            return true;
        } else {
            System.err.println("ERROR: '" + prop.getPropName() + "' must be set.");
            return false;
        }
    }

    private boolean validateInterfaceExists() {
        if (!this.isVipEnabled()) {
            return true;
        } else {
            String val = this.properties.getProperty(EfmProps.EFM_VIRT_IP_INTERFACE.getPropName());
            if (val != null && !val.trim().isEmpty()) {
                String target = val.split(":")[0].trim();

                Enumeration e;
                try {
                    e = NetworkInterface.getNetworkInterfaces();
                } catch (SocketException var6) {
                    System.err.println("Cannot list network interfaces to check VIP interface: " + var6);
                    return false;
                }

                HashSet names = new HashSet();

                while(e.hasMoreElements()) {
                    String name = ((NetworkInterface)e.nextElement()).getDisplayName();
                    if (target.equals(name)) {
                        return true;
                    }

                    names.add(name);
                }

                System.err.println(String.format("Could not find interface name %s in %s", target, names));
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean isVirtualInterfaceAssigned(String interfaceName) {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                Enumeration e2 = ni.getSubInterfaces();

                while(e2.hasMoreElements()) {
                    NetworkInterface ni2 = (NetworkInterface)e2.nextElement();
                    if (interfaceName.equals(ni2.getDisplayName())) {
                        return true;
                    }
                }
            }

            return false;
        } catch (SocketException var6) {
            System.err.println("WARNING: Unable to validate virtual interface '" + interfaceName + "': " + var6.getMessage());
            return true;
        }
    }

    private boolean validateScriptPath(EfmProps script) {
        String val = this.properties.getProperty(script.getPropName());
        if (val != null && !val.isEmpty()) {
            String[] propArray = val.split("\\s+");
            File s = new File(propArray[0]);
            if (s.exists() && s.canExecute()) {
                Set<String> params = EfmProps.getAllowedParams(script);
                if (params != null && !params.isEmpty()) {
                    for(int i = 0; i < propArray.length; ++i) {
                        if (i > 0 && !params.contains(propArray[i])) {
                            System.err.println("Parameter " + propArray[i] + " is not understood for property " + script.getPropName());
                            return false;
                        }
                    }
                } else if (propArray.length > 1) {
                    System.err.println("ERROR: Unknown information after script path for property " + script.getPropName());
                    return false;
                }

                return true;
            } else {
                System.err.println("ERROR: '" + propArray[0] + "' either doesn't exist or isn't executable by the efm user.");
                return false;
            }
        } else if ((this.isWitness || !script.isRequiredByAgent()) && (!this.isWitness || !script.isRequiredByWitness())) {
            return true;
        } else {
            System.err.println("ERROR: '" + script.getPropName() + "' must contain a value.");
            return false;
        }
    }

    private boolean propEmpty(EfmProps prop) {
        String rawVal = this.properties.getProperty(prop.getPropName());
        return rawVal == null || rawVal.trim().isEmpty();
    }

    public Level getLogLevel() {
        return this.parseLevel(EfmProps.EFM_LOGLEVEL);
    }

    public Level getJgroupsLogLevel() {
        return this.parseLevel(EfmProps.JGROUPS_LOGLEVEL);
    }

    private Level parseLevel(EfmProps prop) {
        String value = this.properties.getProperty(prop.getPropName());
        Level level = Level.INFO;
        if (value != null && value.length() != 0) {
            try {
                level = Level.parse(value);
            } catch (IllegalArgumentException var5) {
                System.err.println("Error parsing log level for " + prop.getPropName() + ": " + value);
            }
        }

        return level;
    }

    public void outputPropertyCheck() {
        System.out.println("Agents:");
        Iterator i$ = this.getNodeAddresses().iterator();

        while(i$.hasNext()) {
            String agent = (String)i$.next();
            System.out.println("\t" + agent);
        }

        System.out.println("Binding address: " + this.getBindingAddress());
        System.out.println("I am witness node: " + this.isWitness());
        System.out.println("Cluster name: " + this.getClusterName());
        System.out.println("User emails: " + Arrays.toString(this.getUserEmails()));
        System.out.println("Notification script: " + this.getNotificationScript());
        if (this.isVipEnabled()) {
            System.out.println("VIP: " + this.getVirtualIp());
            System.out.println("VIP interface: " + this.getVirtualIpInterface());
            System.out.println("VIP netmask: " + this.getVirtualIpNetmask());
        } else {
            System.out.println("VIP Support: DISABLED");
        }

        System.out.println("Automatic failover set to: " + this.autoFailoverOn());
        System.out.println("\nNetwork interfaces:");

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(e.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface)e.nextElement();
                System.out.println(ni.getDisplayName());
                Enumeration subs = ni.getInetAddresses();

                while(subs.hasMoreElements()) {
                    InetAddress iAddr = (InetAddress)subs.nextElement();
                    System.out.println("\t" + iAddr.getHostAddress());
                }

                subs = ni.getSubInterfaces();

                while(subs.hasMoreElements()) {
                    NetworkInterface sub = (NetworkInterface)subs.nextElement();
                    System.out.println("\n\t" + sub.getDisplayName());
                    Enumeration addrs = sub.getInetAddresses();

                    while(addrs.hasMoreElements()) {
                        InetAddress iAddr = (InetAddress)addrs.nextElement();
                        System.out.println("\t\t" + iAddr.getHostAddress());
                    }
                }
            }
        } catch (SocketException var7) {
            System.err.println("Could not list network interfaces: " + var7);
        }

    }

    private String getHostPart(String fullAddress) {
        if (fullAddress != null && !fullAddress.isEmpty()) {
            String addr = fullAddress.substring(0, fullAddress.lastIndexOf(58));
            if (addr.startsWith("[") && addr.endsWith("]")) {
                addr = addr.substring(1, addr.length() - 1);
            }

            return addr;
        } else {
            return null;
        }
    }

    private String getPortPart(String fullAddress) {
        return fullAddress.substring(fullAddress.lastIndexOf(58) + 1);
    }

    public String getLicense() {
        return this.properties.getProperty(EfmProps.EFM_LICENSE.getPropName());
    }

    public int getTrialPeriodDays() {
        String retVal = this.properties.getProperty(EfmProps.TRIAL_PERIOD_DAYS.getPropName());
        return retVal != null && !retVal.isEmpty() ? Integer.parseInt(retVal) : -1;
    }

    public int getLicensePollingInterval() {
        String retVal = this.properties.getProperty(EfmProps.LICENSE_POLLING_INTERVAL.getPropName());
        return retVal != null && !retVal.isEmpty() ? Integer.parseInt(retVal) : -1;
    }

    public File getLockFile() {
        return new File("/var/lock/efm-2.1/" + this.getClusterName() + ".lock");
    }

    private List<String> getNodeAddresses() {
        List<String> retVal = new ArrayList();
        if (!this.initialNodes.isEmpty()) {
            retVal.addAll(this.initialNodes);
        }

        String thisNode = this.bindingAddress + ":" + this.bindingPort;
        if (!retVal.contains(thisNode)) {
            retVal.add(thisNode);
        }

        return retVal;
    }

    private boolean bindingAddressLocal() {
        try {
            InetAddress addr = InetAddress.getByName(this.bindingAddress);
            if (NetworkInterface.getByInetAddress(addr) != null) {
                return true;
            }
        } catch (UnknownHostException var2) {
            System.err.println("Received unknown host error for " + this.bindingAddress);
        } catch (SocketException var3) {
            System.err.println("Received socket exception for " + this.bindingAddress);
        }

        System.err.println("The address specified in the " + EfmProps.EFM_BIND_ADDRESS.getPropName() + " property must be local to this node.");
        return false;
    }

    public String getLogFileLocation() {
        return "/var/log/efm-2.1/" + this.getClusterName() + ".log";
    }

    public int getUpdaterSeconds() {
        return this.viewChangeWindowSeconds;
    }

    public static class DynamicMembershipToken extends FixedMembershipToken {
        final Logger logger = LogManager.getEfmLogger();
        EfmNode node;

        public DynamicMembershipToken() {
        }

        public void setNode(EfmNode node) {
            this.node = node;
        }

        public boolean authenticate(AuthToken token, Message msg) {
            StringBuilder sb = new StringBuilder();
            String[] arr$ = this.node.getAllowedIPs();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String ip = arr$[i$];
                if (sb.length() != 0) {
                    sb.append(",");
                }

                sb.append(this.getIpFor(ip));
            }

            this.setMemberList(sb.toString());
            if (this.logger.isLoggable(Level.FINE)) {
                this.logger.log(Level.FINE, "Authenticating {0} against member list {1}", new Object[]{msg.getSrc(), sb.toString()});
            }

            if (super.authenticate(token, msg)) {
                return true;
            } else {
                return this.ipv6AuthCheck(token, msg);
            }
        }

        private boolean ipv6AuthCheck(AuthToken token, Message msg) {
            if (token != null && token instanceof FixedMembershipToken) {
                this.logger.info("jgroups auth returned false. Checking manually.");
                PhysicalAddress src = (PhysicalAddress)this.auth.down(new Event(87, msg.getSrc()));
                if (src == null) {
                    this.logger.log(Level.WARNING, "did not find physical address for {0}", msg.getSrc());
                    return false;
                } else {
                    String rawAddressWithPort = src.toString();
                    String rawAddress = rawAddressWithPort.substring(0, rawAddressWithPort.lastIndexOf(":"));

                    String[] arr$;
                    int i$;
                    int i$;
                    String member;
                    try {
                        InetAddress target = InetAddress.getByName(rawAddress);
                        this.logger.log(Level.FINE, "checking with inet addresses: {0}", target);
                        arr$ = this.node.getAllowedIPs();
                        i$ = arr$.length;

                        for(i$ = 0; i$ < i$; ++i$) {
                            member = arr$[i$];

                            try {
                                InetAddress ia = InetAddress.getByName(member);
                                this.logger.log(Level.FINE, "comparing to {0}", ia);
                                if (ia.equals(target)) {
                                    this.logger.info("Returning true from auth call.");
                                    return true;
                                }
                            } catch (UnknownHostException var12) {
                                this.logger.log(Level.WARNING, "Cannot check {0} against {1} due to: {2}", new Object[]{target, member, var12});
                            }
                        }
                    } catch (UnknownHostException var13) {
                        this.logger.log(Level.SEVERE, "Error creating inet address for {0}: {1}", new Object[]{rawAddress, var13});
                    }

                    this.logger.fine("inet address did not match anything in list. attempting manual string comparison.");
                    if (rawAddress.contains("%")) {
                        if (this.logger.isLoggable(Level.FINE)) {
                            this.logger.log(Level.FINE, "Removing zone index from: {0}", rawAddress);
                        }

                        rawAddress = rawAddress.substring(0, rawAddress.indexOf("%"));
                    }

                    if (this.logger.isLoggable(Level.FINE)) {
                        StringBuilder sb = (new StringBuilder()).append("Address: ").append(rawAddress).append("\nAllowed:");
                        arr$ = this.node.getAllowedIPs();
                        i$ = arr$.length;

                        for(i$ = 0; i$ < i$; ++i$) {
                            member = arr$[i$];
                            sb.append("\n\t").append(member);
                        }

                        this.logger.fine(sb.toString());
                    }

                    String[] arr$ = this.node.getAllowedIPs();
                    int len$ = arr$.length;

                    for(i$ = 0; i$ < len$; ++i$) {
                        String member = arr$[i$];
                        if (rawAddress.equals(member)) {
                            this.logger.log(Level.FINE, "Match: {0}", member);
                            this.logger.info("Returning true from auth call.");
                            return true;
                        }
                    }

                    this.logger.info("Returning false from auth call.");
                    return false;
                }
            } else {
                return false;
            }
        }

        private String getIpFor(String hostOrIP) {
            try {
                InetAddress addr = InetAddress.getByName(hostOrIP);
                return addr.getHostAddress();
            } catch (UnknownHostException var3) {
                this.logger.log(Level.SEVERE, "Could not determine host address for {0}", hostOrIP);
                return hostOrIP;
            }
        }
    }
}
