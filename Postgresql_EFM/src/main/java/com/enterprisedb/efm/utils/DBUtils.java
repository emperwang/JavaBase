package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.ClusterState;
import com.enterprisedb.efm.DBMonitor;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.Channel;

public final class DBUtils {
    private static final Environment ENV = Environment.getEnvironment();

    public static DBMonitor getVipDBMonitor() throws PasswordDecryptException {
        String vip = ENV.getVirtualIp();
        if (vip == null || vip.isEmpty())
            return null;
        return createMonitor(vip);
    }

    public static DBMonitor createMonitor(String address) throws PasswordDecryptException {
        String clearPass = PasswordManager.decrypt(ENV.getDBPassword(), ENV.getClusterName());
        return new DBMonitor(address, ENV.getDBPort(), ENV.getDBUser(), clearPass, ENV.getDBDatabase());
    }

    public static List<String> checkConnections(List<String> dbs) throws PasswordDecryptException {
        long timeout = Environment.getEnvironment().getRemoteDbTimeout();
        List<String> couldNotConnect = new ArrayList<String>();
        for (String ip : dbs) {
            DBMonitor monitor = createMonitor(ip);
            if (!monitor.checkOnce(false, timeout))
                couldNotConnect.add(ip);
        }
        return couldNotConnect;
    }

    public static StandbyInfo chooseFailoverNode(List<StandbyInfo> originals, String[] foPriority) {
        final CountDownLatch xlogLatch;
        final Logger logger = LogManager.getEfmLogger();
        long remoteTimeout = Environment.getEnvironment().getRemoteDbTimeout();
        final List<StandbyInfo> pausedList = new ArrayList<StandbyInfo>();
        final List<StandbyInfo> xlogList = new ArrayList<StandbyInfo>();
        final CountDownLatch pauseLatch = new CountDownLatch(originals.size());
        logger.log(Level.INFO, "Executing pause_recovery command on {0} database nodes.", Integer.valueOf(originals.size()));
        for (StandbyInfo si : originals) {
            final StandbyInfo finalSI = si;
            Thread pauseThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        if (finalSI.getDbMonitor().pauseRecovery()) {
                            if (logger.isLoggable(Level.FINE))
                                logger.log(Level.FINE, "recovery paused");
                            synchronized (pausedList) {
                                pausedList.add(finalSI);
                            }
                        } else {
                            logger.log(Level.SEVERE, "Could not pause recovery on {0}. Will not consider for possible promotion.", finalSI.getHost());
                        }
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, "Could not pause recovery on {0}: {1}. Removing from list for possible promotion.",
                                new Object[] { finalSI.getHost(), e.toString() });
                                //new Object[] { this.val$finalSI.getHost(), e.toString() });
                    } finally {
                        pauseLatch.countDown();
                    }
                }
            },"pause_thread_" + si.getHost());
            pauseThread.setDaemon(true);
            pauseThread.start();
        }
        try {
            if (!pauseLatch.await(remoteTimeout, TimeUnit.SECONDS))
                logger.log(Level.WARNING, "Not all pause_recovery threads finished before timeout. Ignoring calls that return after timeout.");
        } catch (InterruptedException e) {
            logger.severe("Code interrupted waiting for pause_recovery results.");
        }
        synchronized (pausedList) {
            if (pausedList.isEmpty())
                return null;
            if (pausedList.size() == 1)
                return pausedList.get(0);
            xlogLatch = new CountDownLatch(pausedList.size());
            logger.log(Level.INFO, "Retrieving xlog locations from {0} db nodes.", Integer.valueOf(pausedList.size()));
            for (StandbyInfo si : pausedList) {
                final StandbyInfo finalSI = si;
                Thread xlogThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            if (logger.isLoggable(Level.FINE))
                                logger.log(Level.FINE, "Checking {0}", finalSI.getHost());
                            String loc = finalSI.getDbMonitor().getXlogLocation(false);
                            if (loc == null) {
                                logger.log(Level.SEVERE, "Could not retrieve xlog location for node {0}.", finalSI.getHost());
                            } else {
                                if (logger.isLoggable(Level.FINE))
                                    logger.log(Level.FINE, "xlog location: {0}", loc);
                                finalSI.setReplayLocation(loc);
                                synchronized (xlogList) {
                                    xlogList.add(finalSI);
                                }
                            }
                        } catch (SQLException e) {
                            logger.log(Level.SEVERE, "Could not retrieve xlog location for node {0}: {1}", new Object[] { finalSI.getHost(), e.toString() });
                            //logger.log(Level.SEVERE, "Could not retrieve xlog location for node {0}: {1}", new Object[] { this.val$finalSI.getHost(), e.toString() });
                        } finally {
                            xlogLatch.countDown();
                        }
                    }
                },"xlog_thread_" + si.getHost());
                xlogThread.setDaemon(true);
                xlogThread.start();
            }
        }
        try {
            if (!xlogLatch.await(remoteTimeout, TimeUnit.SECONDS))
                logger.log(Level.WARNING, "Not all xlog_location threads finished before timeout. Ignoring calls that return after timeout.");
        } catch (InterruptedException ie) {
            logger.severe("Code interrupted waiting for xlog locations.");
        }
        synchronized (xlogList) {
            if (xlogList.isEmpty())
                return null;
            if (xlogList.size() == 1)
                return xlogList.get(0);
            Collections.sort(xlogList, new Comparator<StandbyInfo>() {
                public int compare(StandbyInfo si1, StandbyInfo si2) {
                    return si2.getReplayLocation().compareTo(si1.getReplayLocation());
                }
            });
            logger.log(Level.INFO, "Sorted: {0}", xlogList);
            String latestLocation = ((StandbyInfo)xlogList.get(0)).getReplayLocation();
            for (String priorityHost : foPriority) {
                for (StandbyInfo si : xlogList) {
                    if (!latestLocation.equals(si.getReplayLocation()))
                        continue;
                    if (priorityHost.equals(si.getHost()))
                        return si;
                }
            }
            StandbyInfo retVal = xlogList.get(0);
            logger.log(Level.WARNING, "No standbys found that match hosts in the failover priority list. Returning: {0}", retVal);
            return retVal;
        }
    }

    public static void resumeReplay(Channel channel, Address[] nodes, ClusterState state) {
        Logger logger = LogManager.getEfmLogger();
        logger.log(Level.INFO, "Resuming replay on {0} nodes.", Integer.valueOf(nodes.length));
        for (Address addr : nodes) {
            String host = ClusterUtils.getHost(channel, addr, state);
            if (host == null) {
                logger.log(Level.SEVERE, "Could not find host address for {0}. Can not resume replay on this node.", addr);
                Notifications.RESUME_FAILED.addSubjectParams(new String[] { "unknown" }).addBodyParams(new String[] { "Could not determine IP address for " + addr }).send();
            } else {
                try {
                    createMonitor(host).resumeReplay();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not resume replay for standby {0} due to: {1}", new Object[] { host, e });
                    Notifications.RESUME_FAILED.addSubjectParams(new String[] { host }).addBodyParams(new String[] { e.toString() }).send();
                }
            }
        }
    }
}
