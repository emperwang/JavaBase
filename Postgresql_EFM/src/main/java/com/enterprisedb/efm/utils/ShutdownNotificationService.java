package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import java.util.logging.Logger;

public enum ShutdownNotificationService {
    INSTANCE;

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    volatile boolean nodeStarted = false;

    private static final Logger LOGGER;

    private volatile Notifications shutdownNotification;

    public static ShutdownNotificationService getInstance() {
        return INSTANCE;
    }

    public void setShutdownNotification(Notifications n) {
        this.shutdownNotification = n;
    }

    public void registerShutdownNotificationHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("Notification-ShutdownHook") {
            public void run() {
                Environment env = Environment.getEnvironment();
                if (!ShutdownNotificationService.this.nodeStarted || ShutdownNotificationService.this.shutdownNotification == null) {
                    String bindingAddress = env.getBindingAddress();
                    if (bindingAddress == null || bindingAddress.length() == 0) {
                        Notifications.AGENT_EXITED.addSubjectParams(new String[] { env.getClusterName() }).send();
                    } else {
                        Notifications.IDLE_EXITED.addSubjectParams(new String[] { bindingAddress, env.getClusterName() }).send();
                    }
                } else {
                    ShutdownNotificationService.this.shutdownNotification.addSubjectParams(new String[] { env.getBindingAddress(), env.getClusterName() }).send();
                }
            }
        });
    }

    public void nodeHasStarted() {
        this.nodeStarted = true;
    }
}
