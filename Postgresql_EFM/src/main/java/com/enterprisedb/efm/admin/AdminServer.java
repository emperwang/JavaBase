package com.enterprisedb.efm.admin;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.nodes.EfmNode;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.SuccessHolder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author: wk
 * @Date: 2020/11/6 16:43
 * @Description
 */
public enum AdminServer {
    INSTANCE;

    public enum AdminCommand {
        ADD_NODE, ALLOW_NODE, DISALLOW_NODE, PROMOTE, REMOVE_NODE, RESUME, SET_PRIORITY, STATUS, STOP, STOP_CLUSTER, SWITCHOVER;
    }

    private volatile boolean listening = true;

    private transient Thread requestServer = null;

    public static final String OK_RESP = "F_T_W";

    public static final String PARAM_NEEDED = "_param_needed_";

    public static final String PARAM_SEP = "|";

    public static final String PARAM_SEP_REGEX = "\\|";

    public static final String AUTH_NEEDED = "_authorization_needed_";

    private transient EfmNode parentNode;

    private final int port;

    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    AdminServer() {
        this.port = Environment.getEnvironment().getAdminPort();
    }

    public static AdminServer getAdminServer() {
        return INSTANCE;
    }

    public EfmNode getParentNode() {
        return this.parentNode;
    }

    public synchronized boolean start(EfmNode parent) {
        if (this.requestServer == null) {
            this.parentNode = parent;
            final CountDownLatch latch = new CountDownLatch(1);
            final SuccessHolder sh = new SuccessHolder();
            this.requestServer = new Thread(new Runnable() {
                public void run() {
                    try {
                        AdminServer.LOGGER.log(Level.INFO, "AdminServer starting on port: {0}", Integer.valueOf(AdminServer.this.port));
                        ServerSocket serverSocket = new ServerSocket(AdminServer.this.port, 50, InetAddress.getByName("127.0.0.1"));
                        sh.success();
                        latch.countDown();
                        while (AdminServer.this.listening)
                            (new AdminServerThread(serverSocket.accept())).start();
                    } catch (IOException e) {
                        AdminServer.LOGGER.log(Level.SEVERE, "AdminServer could not bind on port " + AdminServer.this.port, e);
                    } finally {
                        AdminServer.LOGGER.info("AdminServer stopped");
                        latch.countDown();
                    }
                }
            });
            this.requestServer.setDaemon(true);
            this.requestServer.start();
            try {
                latch.await();
            } catch (InterruptedException e) {
                LOGGER.log(Level.FINE, "{0} interrupted.", Thread.currentThread().getName());
            }
            return sh.isSuccess();
        }
        LOGGER.log(Level.WARNING, "AdminServer is already running on port: {0}", Integer.valueOf(this.port));
        return true;
    }

    public synchronized void shutdown() {
        LOGGER.info("Stopping AdminServer...");
        this.listening = false;
        this.requestServer.interrupt();
    }
}
