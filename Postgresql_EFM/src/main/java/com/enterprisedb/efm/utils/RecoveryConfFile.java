//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import com.enterprisedb.efm.exec.SudoFunctions;
import java.io.File;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum RecoveryConfFile {
    INSTANCE;

    private final File recoveryConfFile;
    private static final Logger LOGGER = LogManager.getEfmLogger();
    private transient Timer recoveryConfMonitor;
    private final Environment env = Environment.getEnvironment();
    private boolean monitoring = false;

    private RecoveryConfFile() {
        this.recoveryConfFile = new File(this.env.getRecoveryConfLocation() + "/recovery.conf");
    }

    public static RecoveryConfFile getInstance() {
        return INSTANCE;
    }

    public synchronized void startMonitoring() {
        if (!this.monitoring) {
            LOGGER.log(Level.INFO, "Starting recovery.conf monitor for: {0}", this.recoveryConfFile);
            String timerName = "recovery_conf_check_timer_" + this.env.getClusterName();
            this.recoveryConfMonitor = new Timer(timerName, true);
            this.recoveryConfMonitor.schedule(new FileMonitor(this.recoveryConfFile, Notifications.RECOVERY_CONF_FILE_EXISTS, true, new FileExistenceTest() {
                boolean recoveryDone = false;

                public synchronized boolean exists(File f) {
                    RecoveryConfFile.LOGGER.log(Level.FINE, "recoveryDone: {0}", this.recoveryDone);
                    ProcessResult result = ExecUtil.performExec(new String[]{RecoveryConfFile.this.env.getUserSudoCommand() + " " + RecoveryConfFile.this.env.getDBFunctionsScript() + " " + SudoFunctions.RECOVERY_CONF_EXISTS + " " + RecoveryConfFile.this.env.getClusterName()});
                    if (result.getExitValue() == 0) {
                        if (this.recoveryDone) {
                            return true;
                        }

                        RecoveryConfFile.LOGGER.info("Recovery still in process. Ignoring recovery.conf file.");
                    } else if (!this.recoveryDone) {
                        RecoveryConfFile.LOGGER.fine("Recovery.conf monitor storing that recovery.conf does not exist.");
                        this.recoveryDone = true;
                    }

                    return false;
                }
            }), 120000L, 3600000L);
            this.monitoring = true;
        }

    }

    public synchronized boolean isMonitoring() {
        return this.monitoring;
    }

    public synchronized void stopMonitoring() {
        this.monitoring = false;
        if (this.recoveryConfMonitor != null) {
            this.recoveryConfMonitor.cancel();
        }

    }
}
