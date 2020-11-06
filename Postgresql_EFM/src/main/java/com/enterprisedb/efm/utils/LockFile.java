package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum LockFile {
    INSTANCE;

    public static final String EXTENSION = ".lock";

    private transient Timer lockMonitor;

    private static final Logger LOGGER;

    private final File lockFile;

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    LockFile() {
        this.lockFile = new File("/var/lock/efm-2.1/" + Environment.getEnvironment().getClusterName() + ".lock");
    }

    public static LockFile getInstance() {
        return INSTANCE;
    }

    public synchronized boolean lock() {
        try {
            if (this.lockFile.createNewFile()) {
                LOGGER.log(Level.INFO, "created lock file: {0}", this.lockFile);
                if (!Utils.chmod(this.lockFile, "644"))
                    return false;
            } else {
                LOGGER.log(Level.WARNING, "lock file already exists: {0}", this.lockFile);
                return false;
            }
            Runtime.getRuntime().addShutdownHook(new Thread("LockFile-ShutdownHook") {
                public void run() {
                    LockFile.this.shutdownLockMonitor();
                    if (!LockFile.this.lockFile.delete())
                        LockFile.LOGGER.log(Level.WARNING, "unable to delete: {0}", LockFile.this.lockFile);
                }
            });
            LOGGER.log(Level.INFO, "Starting lock monitor for: {0}", this.lockFile);
            String timerName = "lock_check_timer_" + Environment.getEnvironment().getClusterName();
            this.lockMonitor = new Timer(timerName, true);
            this.lockMonitor.schedule(new FileMonitor(this.lockFile, Notifications.LOCK_FILE_REMOVED, false, new FileExistenceTest() {
                public boolean exists(File f) {
                    return f.exists();
                }
            }), 0L, 5000L);
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "unable to create lock file: " + this.lockFile, e);
            return false;
        }
    }

    public synchronized void shutdownLockMonitor() {
        if (this.lockMonitor != null)
            this.lockMonitor.cancel();
    }

    public String toString() {
        return this.lockFile.toString();
    }
}
