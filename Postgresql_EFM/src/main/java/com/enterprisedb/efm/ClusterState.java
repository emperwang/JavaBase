package com.enterprisedb.efm;

import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.ClusterUtils;
import com.enterprisedb.efm.utils.LogManager;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;

/**
 * @author: wk
 * @Date: 2020/11/6 16:39
 * @Description
 */
public class ClusterState {
    static final Logger LOGGER = LogManager.getEfmLogger();

    private static final long serialVersionUID = -1589292369835972921L;

    private Address master;

    private Address promoting;

    private Address[] idles = new Address[0];

    private Address[] standbys = new Address[0];

    private Address[] witnesses = new Address[0];

    private String[] allowed = new String[0];

    private String[] foPriority = new String[0];

    public EfmNode.NodeType removeAddress(Address target, Channel channel) {
        if (target.equals(this.master)) {
            LOGGER.log(Level.INFO, "Removing master address {0}", target);
            this.master = null;
            return EfmNode.NodeType.MASTER;
        }
        for (Address addr : this.idles) {
            if (target.equals(addr)) {
                LOGGER.log(Level.INFO, "Removing idle address {0}", addr);
                this.idles = removeFrom(this.idles, addr);
                return EfmNode.NodeType.IDLE;
            }
        }
        for (Address addr : this.standbys) {
            if (target.equals(addr)) {
                LOGGER.log(Level.INFO, "Removing standby address {0}", addr);
                this.standbys = removeFrom(this.standbys, addr);
                String host = ClusterUtils.getHost(channel, target, this);
                LOGGER.log(Level.INFO, "Removing standby info {0} from failover priority list.", host);
                this.foPriority = removeFrom(this.foPriority, host);
                return EfmNode.NodeType.STANDBY;
            }
        }
        for (Address addr : this.witnesses) {
            if (target.equals(addr)) {
                LOGGER.log(Level.INFO, "Removing witness address {0}", addr);
                this.witnesses = removeFrom(this.witnesses, addr);
                return EfmNode.NodeType.WITNESS;
            }
        }
        return null;
    }

    public Address getMaster() {
        return this.master;
    }

    public void setMaster(Address master) {
        if (master != null && this.promoting != null)
            throw new AssertionError("Cannot set master. Current promoting: " + this.promoting);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Setting master node {0}", master);
        this.master = master;
    }

    public Address getPromoting() {
        return this.promoting;
    }

    public void setPromoting(Address promoting) {
        if (promoting != null && this.master != null)
            throw new AssertionError("Cannot set promoting. Current master: " + this.master);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Setting promoting node {0}", promoting);
        this.promoting = promoting;
    }

    public Address[] getIdleNodes() {
        return Arrays.<Address>copyOf(this.idles, this.idles.length);
    }

    public void addIdleNode(Address idle, Channel channel) {
        removeAddress(idle, channel);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Adding idle node {0}", idle);
        this.idles = Arrays.<Address>copyOf(this.idles, this.idles.length + 1);
        this.idles[this.idles.length - 1] = idle;
    }

    public Address[] getStandbys() {
        return Arrays.<Address>copyOf(this.standbys, this.standbys.length);
    }

    public void addStandby(Address standby, Channel channel) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Adding standby {0}", standby);
        this.standbys = Arrays.<Address>copyOf(this.standbys, this.standbys.length + 1);
        this.standbys[this.standbys.length - 1] = standby;
        String host = ClusterUtils.getHost(channel, standby, this);
        boolean addIt = true;
        for (String s : this.foPriority) {
            if (s.equals(host)) {
                addIt = false;
                break;
            }
        }
        if (addIt)
            setNodePriority(host, this.foPriority.length + 1);
    }

    public Address[] getWitnesses() {
        return Arrays.<Address>copyOf(this.witnesses, this.witnesses.length);
    }

    public void addWitness(Address witness) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Adding witness {0}", witness);
        this.witnesses = Arrays.<Address>copyOf(this.witnesses, this.witnesses.length + 1);
        this.witnesses[this.witnesses.length - 1] = witness;
    }

    public String[] getFoPriority() {
        return Arrays.<String>copyOf(this.foPriority, this.foPriority.length);
    }

    public String[] getAllowed() {
        return Arrays.<String>copyOf(this.allowed, this.allowed.length);
    }

    public void addAllowedNode(String ip) {
        if (firstIndexOf((Object[])this.allowed, ip) != -1) {
            LOGGER.log(Level.FINE, "{0} already in allowed node list.", ip);
            return;
        }
        this.allowed = addToEnd(this.allowed, ip);
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder("[");
            for (String s : this.allowed)
                sb.append(" ").append(s);
            sb.append(" ]");
            LOGGER.log(Level.FINE, "New allowed address list: {0}", sb.toString());
        }
    }

    public void setNodePriority(String ip, int priority) {
        this.foPriority = removeFrom(this.foPriority, ip);
        if (priority > 0)
            this.foPriority = insertInto(this.foPriority, ip, priority - 1);
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder("[");
            for (String s : this.foPriority)
                sb.append(" ").append(s);
            sb.append(" ]");
            LOGGER.log(Level.FINE, "New failover priority list: {0}", sb.toString());
        }
    }

    public void removeAllowedNode(String ip) {
        if (firstIndexOf((Object[])this.allowed, ip) == -1) {
            LOGGER.log(Level.WARNING, "Address {0} is not in allowed node list", ip);
        } else {
            this.allowed = removeFrom(this.allowed, ip);
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuilder sb0 = new StringBuilder("[");
            for (String s : this.allowed)
                sb0.append(" ").append(s);
            sb0.append(" ]");
            LOGGER.log(Level.FINE, "New allowed address list: {0}", sb0.toString());
        }
    }

    public String toString() {
        StringBuilder sb = (new StringBuilder()).append("[ master: ").append(this.master).append(" promoting: ").append(this.promoting).append(" standbys (").append(this.standbys.length).append("): ");
        for (Address a : this.standbys)
            sb.append(a).append(" ");
        sb.append(" witnesses (").append(this.witnesses.length).append("): ");
        for (Address a : this.witnesses)
            sb.append(a).append(" ");
        sb.append(" idle nodes (").append(this.idles.length).append("): ");
        for (Address a : this.idles)
            sb.append(a).append(" ");
        sb.append(" ]");
        return sb.toString();
    }

    public Set<Address> getNonMasterAddresses() {
        Set<Address> retVal = new HashSet<Address>();
        retVal.addAll(Arrays.asList(this.idles));
        retVal.addAll(Arrays.asList(this.standbys));
        retVal.addAll(Arrays.asList(this.witnesses));
        if (this.master != null && retVal.contains(this.master))
            throw new AssertionError("Master address " + this.master + " is also in non-master list.");
        if (this.promoting != null && retVal.contains(this.promoting))
            throw new AssertionError("Promoting address " + this.promoting + " is also in non-master list.");
        return retVal;
    }

    public String getHostOrIp(String ip) {
        Set<String> hosts = new HashSet<String>();
        Collections.addAll(hosts, this.allowed);
        Collections.addAll(hosts, this.foPriority);
        if (LOGGER.isLoggable(Level.FINER))
            LOGGER.log(Level.FINER, "Host/IP addresses to check for {0}: {1}", new Object[] { ip, hosts });
        for (String s : hosts) {
            if (s.equals(ip)) {
                LOGGER.finer("returning: " + s);
                return s;
            }
        }
        try {
            InetAddress target = InetAddress.getByName(ip);
            for (String s : hosts) {
                InetAddress address = InetAddress.getByName(s);
                if (address.equals(target)) {
                    LOGGER.finer("returning: " + s);
                    return s;
                }
            }
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Could not create InetAddress: {0}", e.toString());
            return ip;
        }
        LOGGER.log(Level.FINE, "Could not find existing match for {0}", ip);
        return ip;
    }

    public boolean containsNodeAtType(Address node, EfmNode.NodeType type) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Checking to see if cluster state contains {0} as type {1}", new Object[] { node, type });
        switch (type) {
            case MASTER:
                if (node.equals(this.master))
                    return true;
                LOGGER.log(Level.FINE, "Returning false.");
                return false;
            case IDLE:
                for (Address addr : this.idles) {
                    if (node.equals(addr))
                        return true;
                }
                LOGGER.log(Level.FINE, "Returning false.");
                return false;
            case PROMOTING:
                if (node.equals(this.promoting))
                    return true;
                LOGGER.log(Level.FINE, "Returning false.");
                return false;
            case STANDBY:
                for (Address addr : this.standbys) {
                    if (node.equals(addr))
                        return true;
                }
                LOGGER.log(Level.FINE, "Returning false.");
                return false;
            case WITNESS:
                for (Address addr : this.witnesses) {
                    if (node.equals(addr))
                        return true;
                }
                LOGGER.log(Level.FINE, "Returning false.");
                return false;
        }
        LOGGER.severe("Unknown type: " + type);
        LOGGER.log(Level.FINE, "Returning false.");
        return false;
    }

    public void clearStandbys() {
        this.standbys = new Address[0];
    }

    public void clearIdles() {
        this.idles = new Address[0];
    }

    public void clearWitnesses() {
        this.witnesses = new Address[0];
    }

    private <T> T[] removeFrom(T[] orig, T target) {
        int index = firstIndexOf((Object[])orig, target);
        if (index < 0)
            return orig;
        T[] retVal = Arrays.copyOf(orig, orig.length - 1);
        System.arraycopy(orig, index + 1, retVal, index, orig.length - 1 - index);
        return removeFrom(retVal, target);
    }

    private <T> T[] insertInto(T[] orig, T target, int index) {
        if (index < 0 || index > orig.length)
            index = orig.length;
        T[] retVal = Arrays.copyOf(orig, orig.length + 1);
        retVal[index] = target;
        System.arraycopy(orig, index, retVal, index + 1, orig.length - index);
        return retVal;
    }

    private int firstIndexOf(Object[] array, Object target) {
        for (int i = 0; i < array.length; i++) {
            if (target.equals(array[i]))
                return i;
        }
        return -1;
    }

    private <T> T[] addToEnd(T[] original, T item) {
        return insertInto(original, item, -1);
    }
}
