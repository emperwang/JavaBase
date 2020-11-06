package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.EfmProps;
import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.nodes.EfmNode;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum LicenseManager {
    INSTANCE;

    private final transient Object schedulerLock = new Object();

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    private final Environment env = Environment.getEnvironment();

    private static final String TRIAL_LICENSE_FILE = "edbfm";

    private static final int TRIAL_PERIOD_DAYS = 60;

    private static final int TRIAL_EXPIRING_WARNING = 5;

    private static final int FULL_EXPIRING_WARNING = 5;

    private static final int POLLING_INTERVAL = 6;

    private final Date trialExpiration;

    private transient ScheduledExecutorService scheduler;

    private static final Logger LOGGER;

    LicenseManager() {
        String binDir = "/usr/bin/";
        File trialFile = new File(binDir + "edbfm");
        if (trialFile.exists()) {
            long trialExpirationMs = trialFile.lastModified() + TimeUnit.DAYS.toMillis(getTrialPeriodDays());
            this.trialExpiration = new Date(trialExpirationMs);
        } else {
            this.trialExpiration = null;
        }
    }

    public static LicenseManager getLicenseManager() {
        return INSTANCE;
    }

    public boolean monitorLicense(final EfmNode parentNode) {
        synchronized (this.schedulerLock) {
            if (this.scheduler != null)
                return true;
            LOGGER.fine("Starting License Monitor");
            LOGGER.log(Level.INFO, "Trial period ends on: {0}", this.trialExpiration);
            if (!checkLicense())
                return false;
            int licensePollingInterval = this.env.getLicensePollingInterval();
            int interval = 6;
            if (licensePollingInterval > 0 && licensePollingInterval < 6) {
                LOGGER.log(Level.FINE, "Changing polling interval to {0}", Integer.valueOf(licensePollingInterval));
                interval = licensePollingInterval;
            }
            this.scheduler = createExecutorService();
            this.scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (!LicenseManager.this.checkLicense())
                        parentNode.shutdownCluster();
                }
            },  interval, interval, TimeUnit.HOURS);
            return true;
        }
    }

    public void shutdown() {
        synchronized (this.schedulerLock) {
            if (this.scheduler != null)
                this.scheduler.shutdown();
        }
    }

    private int getTrialPeriodDays() {
        int testPeriodDays = this.env.getTrialPeriodDays();
        if (testPeriodDays < 0)
            return 60;
        if (testPeriodDays > 60) {
            LOGGER.log(Level.WARNING, "Trial period cannot be set beyond {0}", Integer.valueOf(60));
            return 60;
        }
        return testPeriodDays;
    }

    private synchronized boolean checkLicense() {
        String licenseKey = getLicenseFromProps();
        if (licenseKey == null) {
            if (!checkTrialLicense()) {
                String expirationText;
                if (this.trialExpiration == null) {
                    expirationText = "unknown";
                } else {
                    expirationText = this.trialExpiration.toString();
                }
                Notifications.LICENSE_EXPIRED_SHUTDOWN.addBodyParams(new String[] { expirationText }).send();
                return false;
            }
            return true;
        }
        LOGGER.log(Level.FINE, "Checking full license: {0}", licenseKey);
        String validationError = ProductKey.productKeyValidate(licenseKey);
        if (validationError != null) {
            LOGGER.log(Level.SEVERE, "Product key validation failed: {0}", validationError);
            if (checkTrialLicense()) {
                Notifications.LICENSE_INVALID_NO_SHUTDOWN.send();
                return true;
            }
            Notifications.LICENSE_INVALID_SHUTDOWN.send();
            return false;
        }
        Date expirationDate = ProductKey.productKeyExpirationDate(licenseKey);
        if (expirationDate == null) {
            LOGGER.fine("Agent running with perpetual license.");
            return true;
        }
        if (expirationDate.before(new Date())) {
            LOGGER.warning("Full license has expired");
            if (checkTrialLicense()) {
                Notifications.LICENSE_EXPIRED_NO_SHUTDOWN.addBodyParams(new String[] { expirationDate.toString() }).send();
                return true;
            }
            Notifications.LICENSE_EXPIRED_SHUTDOWN.addBodyParams(new String[] { expirationDate.toString() }).send();
            return false;
        }
        if (expiringSoon(expirationDate, 5))
            Notifications.FULL_LICENSE_EXPIRING.addBodyParams(new String[] { expirationDate.toString() }).send();
        synchronized (this.schedulerLock) {
            if (this.scheduler == null)
                LOGGER.log(Level.INFO, "Full license expires: {0}", expirationDate);
        }
        return true;
    }

    private String getLicenseFromProps() {
        try {
            this.env.reloadProperties(new EfmProps[] { EfmProps.EFM_LICENSE });
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not reload properties: {0}", ex.toString());
            return null;
        }
        String retVal = this.env.getLicense();
        if (retVal == null || retVal.isEmpty())
            return null;
        return retVal;
    }

    private boolean checkTrialLicense() {
        LOGGER.fine("Checking trial license");
        if (this.trialExpiration == null)
            return false;
        if (this.trialExpiration.before(new Date())) {
            LOGGER.warning("Trial license has expired");
            return false;
        }
        if (expiringSoon(this.trialExpiration, 5))
            Notifications.TRIAL_LICENSE_EXPIRING.addBodyParams(new String[] { this.trialExpiration.toString() }).send();
        return true;
    }

    private boolean expiringSoon(Date expiration, int howSoon) {
        Date now = new Date();
        if (expiration.before(now))
            throw new IllegalArgumentException("Expiration date has already passed.");
        long soonDeadline = expiration.getTime() - TimeUnit.DAYS.toMillis(howSoon);
        return (now.getTime() > soonDeadline);
    }

    private ScheduledExecutorService createExecutorService() {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "License_monitor");
            }
        });
    }
}
