package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import java.io.File;
import java.util.TimerTask;

public class FileMonitor extends TimerTask {
    final File file;

    final Notifications warningMsg;

    final boolean notifyExists;

    final FileExistenceTest fileTester;

    final Environment env = Environment.getEnvironment();

    boolean notifyUser = true;

    FileMonitor(File file, Notifications warningMsg, boolean notifyExists, FileExistenceTest fileTester) {
        this.file = file;
        this.warningMsg = warningMsg;
        this.notifyExists = notifyExists;
        this.fileTester = fileTester;
    }

    public void run() {
        boolean fileExists = this.fileTester.exists(this.file);
        if ((this.notifyExists && fileExists) || (!this.notifyExists && !fileExists)) {
            if (this.notifyUser) {
                this.warningMsg.addSubjectParams(new String[] { this.env.getClusterName() }).addBodyParams(new String[] { this.env.getClusterName(), this.file.toString(), this.env.getBindingAddress() }).send();
                this.notifyUser = false;
            }
        } else if (!this.notifyUser) {
            this.notifyUser = true;
        }
    }
}
