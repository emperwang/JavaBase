package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.ClusterState;
import com.enterprisedb.efm.DBMonitor;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.admin.EfmStatus;
import com.enterprisedb.efm.admin.NodeStatus;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.Rsp;
import org.jgroups.util.RspList;

public final class ClusterUtils {
    private static final Logger LOGGER = LogManager.getEfmLogger();

    public static boolean pingHost(String host) {
        String cmd = Environment.getEnvironment().getPingServerCommand() + " " + host;
        ProcessResult result = ExecUtil.performExec(new String[] { cmd });
        LOGGER.info(result.toString());
        if (result.getExitValue() != 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("Unable to ping host: ");
            builder.append(cmd);
            result.addNiceOutput(builder);
            LOGGER.warning(builder.toString());
        }
        return (result.getExitValue() == 0);
    }

    public static boolean isVipReachable() {
        DBMonitor vipDBMonitor;
        Environment env = Environment.getEnvironment();
        if (!env.isVipEnabled())
            return false;
        if (pingHost(env.getVirtualIp()))
            return true;
        try {
            vipDBMonitor = DBUtils.getVipDBMonitor();
        } catch (PasswordDecryptException e) {
            LOGGER.severe(e.toString());
            return false;
        }
        return (vipDBMonitor != null && vipDBMonitor.checkOnce(false, env.getRemoteDbTimeout()));
    }

    public static <T> RspList<T> getObjectFromNode(Address nodeAddress, MessageDispatcher dispatcher, Object msg) {
        LOGGER.log(Level.INFO, "Sending request {0} to node {1}", new Object[] { msg, nodeAddress });
        long timeout = 1000L * Environment.getEnvironment().getRemoteDbTimeout();
        RequestOptions ro = (new RequestOptions()).setTimeout(timeout).setMode(ResponseMode.GET_FIRST).setAnycasting(true);
        Collection<Address> addresses = new HashSet<Address>();
        addresses.add(nodeAddress);
        try {
            return dispatcher.castMessage(addresses, new Message(null, msg), ro);
        } catch (InterruptedException ie) {
            LOGGER.warning("Cast message interrupted.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exception from castMessage: ", e.toString());
        }
        return new RspList();
    }

    public static boolean sendToNodes(String msg, MessageDispatcher md) throws Exception {
        long timeout = 1000L * Environment.getEnvironment().getRemoteDbTimeout();
        RequestOptions ro = (new RequestOptions()).setTimeout(timeout).setMode(ResponseMode.GET_ALL).setExclusionList(new Address[] { md.getChannel().getAddress() }).setAnycasting(true);
        RspList<Object> rspList = md.castMessage(null, new Message(null, msg), ro);
        LOGGER.log(Level.FINE, "RspList size: {0}", Integer.valueOf(rspList.size()));
        for (Rsp<Object> rsp : (Iterable<Rsp<Object>>)rspList.values()) {
            if (!rsp.wasReceived()) {
                LOGGER.log(Level.WARNING, "Could not reach node: {0}", rsp.getSender());
                return false;
            }
        }
        return true;
    }

    public static void sendMessageToCoordinator(Channel channel, Object object) throws Exception {
        Address coordinator = channel.getView().getMembers().get(0);
        channel.send(coordinator, object);
    }

    public static EfmStatus sendStatusRequestToNodes(MessageDispatcher md, ClusterState state) throws Exception {
        EfmStatus status = new EfmStatus();
        long timeout = 1000L * Environment.getEnvironment().getRemoteDbTimeout();
        RequestOptions ro = (new RequestOptions()).setTimeout(timeout).setMode(ResponseMode.GET_ALL).setAnycasting(true);
        RspList<NodeStatus> rspList = md.castMessage(null, new Message(null, "node_status"), ro);
        for (Rsp<NodeStatus> rsp : (Iterable<Rsp<NodeStatus>>)rspList.values()) {
            if (rsp.getValue() != null)
                status.addNodeStatus((NodeStatus)rsp.getValue());
            if (!rsp.wasReceived()) {
                String error = "Could not reach all nodes.";
                LOGGER.warning("Could not reach all nodes.");
                status.setError("Could not reach all nodes.");
            }
        }
        String coordinatorHost = getHost(md.getChannel(), md.getChannel().getView().getMembers().get(0), state);
        status.setHostLists(state, coordinatorHost);
        return status;
    }

    public static String getHost(Channel channel, Address address, ClusterState cs) {
        IpAddress ipAddr = (IpAddress)channel.down(new Event(87, address));
        if (ipAddr == null || ipAddr.getIpAddress() == null)
            return null;
        return cs.getHostOrIp(ipAddr.getIpAddress().getHostAddress());
    }

    public static String getHostAndPort(Channel channel, Address address, ClusterState cs) {
        IpAddress ipAddr = (IpAddress)channel.down(new Event(87, address));
        if (ipAddr == null)
            throw new IllegalStateException("No InetAddress for " + address);
        String host = cs.getHostOrIp(ipAddr.getIpAddress().getHostAddress());
        return host + ":" + ipAddr.getPort();
    }

    private static Set<String> getLocalHostAddresses() throws SocketException {
        Set<String> retVal = new HashSet<String>();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            Enumeration<InetAddress> addrs = ((NetworkInterface)e.nextElement()).getInetAddresses();
            while (addrs.hasMoreElements())
                retVal.add(((InetAddress)addrs.nextElement()).getHostAddress());
        }
        return retVal;
    }

    public static void releaseVip(boolean force, boolean stopMonitor) {
        Environment env = Environment.getEnvironment();
        if (!env.isVipEnabled())
            return;
        if (stopMonitor)
            VipMonitor.getInstance().stopMonitoring();
        String virtualAddress = env.getVirtualIp();
        String interfaceName = env.getVirtualIpInterface();
        if (force || env.autoFailoverOn()) {
            ProcessResult result;
            LOGGER.log(Level.INFO, "Releasing virtual ip ( {0} from interface {1} ) from this node.", new Object[] { virtualAddress, interfaceName });
            String switchAddrScript = env.getAddressScript();
            if (env.isVipV6()) {
                String netMask = env.getVirtualIpNetmask();
                result = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + switchAddrScript + " " + SudoFunctions.RELEASE_6 + " " + interfaceName + " " + virtualAddress + " " + netMask });
            } else {
                result = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + switchAddrScript + " " + SudoFunctions.RELEASE + " " + interfaceName });
            }
            LOGGER.info(result.toString());
            StringBuilder builder = new StringBuilder();
            result.addNiceOutput(builder);
            Notifications.RELEASE_VIP.addSubjectParams(new String[] { env.getBindingAddress() }).addBodyParams(new String[] { virtualAddress, env.getBindingAddress(), builder.toString() }).send();
        } else {
            LOGGER.info("Not releasing VIP.");
        }
    }

    public static boolean acquireVip() {
        Environment env = Environment.INSTANCE;
        if (env.isVipEnabled()) {
            ProcessResult result;
            if (isBroadcastingVip()) {
                LOGGER.info("This node is already broadcasting the VIP.");
                return true;
            }
            String virtualAddress = env.getVirtualIp();
            String interfaceName = env.getVirtualIpInterface();
            String netMask = env.getVirtualIpNetmask();
            String switchAddrScript = env.getAddressScript();
            LOGGER.info(String.format("Attempting to assign %s to this node on interface %s.", new Object[] { virtualAddress, interfaceName }));
            if (env.isVipV6()) {
                result = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + switchAddrScript + " " + SudoFunctions.ASSIGN_6 + " " + interfaceName + " " + virtualAddress + " " + netMask });
            } else {
                result = ExecUtil.performExec(new String[] { env.getRootSudoCommand() + " " + switchAddrScript + " " + SudoFunctions.ASSIGN + " " + interfaceName + " " + virtualAddress + " " + netMask });
            }
            LOGGER.info(result.toString());
            StringBuilder builder = new StringBuilder();
            result.addNiceOutput(builder);
            if (result.getExitValue() != 0) {
                Notifications.ASSIGN_VIP_FAILED.addSubjectParams(new String[] { env.getBindingAddress() }).addBodyParams(new String[] { virtualAddress, env.getBindingAddress(), builder.toString() }).send();
                return false;
            }
            Notifications.ASSIGN_VIP.addSubjectParams(new String[] { env.getBindingAddress() }).addBodyParams(new String[] { virtualAddress, env.getBindingAddress(), builder.toString() }).send();
            return true;
        }
        return true;
    }

    public static boolean isBroadcastingVip() {
        Set<String> actualAddresses;
        InetAddress vipAddr;
        try {
            actualAddresses = getLocalHostAddresses();
        } catch (SocketException e) {
            System.err.println("Error determining network addresses: " + e);
            return false;
        }
        String vip = Environment.getEnvironment().getVirtualIp();
        if (vip == null || vip.isEmpty())
            return false;
        try {
            vipAddr = InetAddress.getByName(vip);
        } catch (UnknownHostException e) {
            LogManager.getEfmLogger().log(Level.SEVERE, "Could not create inet addr object for " + vip, e);
            return false;
        }
        for (String address : actualAddresses) {
            try {
                InetAddress test = InetAddress.getByName(address);
                if (test.equals(vipAddr))
                    return true;
            } catch (UnknownHostException e) {
                LogManager.getEfmLogger().log(Level.WARNING, "Could not check address " + address + " due to " + e.toString());
            }
        }
        return false;
    }
}
