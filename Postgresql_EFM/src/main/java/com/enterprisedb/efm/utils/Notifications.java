//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.Environment;
import com.enterprisedb.efm.exec.ExecUtil;
import com.enterprisedb.efm.exec.ProcessResult;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public enum Notifications {
    FENCING_SCRIPT(I18N.getString("FENCING_SCRIPT_SUBJ"), I18N.getString("FENCING_SCRIPT_BODY"), Notifications.Severity.INFO),
    POST_PROMOTION_SCRIPT_RUN(I18N.getString("POST_PROMOTION_SCRIPT_SUBJ"), I18N.getString("POST_PROMOTION_SCRIPT_BODY"), Notifications.Severity.INFO),
    POST_DB_FAIL_SCRIPT_RUN(I18N.getString("POST_DB_FAIL_SCRIPT_SUBJ"), I18N.getString("POST_DB_FAIL_SCRIPT_BODY"), Notifications.Severity.INFO),
    MASTER_ISO_SCRIPT_RUN(I18N.getString("MASTER_ISO_SCRIPT_SUBJ"), I18N.getString("MASTER_ISO_SCRIPT_BODY"), Notifications.Severity.INFO),
    WITNESS_STARTED(I18N.getString("WITNESS_STARTED_SUBJ"), I18N.getString("WITNESS_STARTED_BODY"), Notifications.Severity.INFO),
    MASTER_STARTED(I18N.getString("MASTER_STARTED_SUBJ"), I18N.getString("MASTER_STARTED_BODY"), Notifications.Severity.INFO),
    STANDBY_STARTED(I18N.getString("STANDBY_STARTED_SUBJ"), I18N.getString("STANDBY_STARTED_BODY"), Notifications.Severity.INFO),
    IDLE_STARTED(I18N.getString("IDLE_STARTED_SUBJ"), I18N.getString("IDLE_STARTED_BODY"), Notifications.Severity.INFO),
    ASSIGN_VIP(I18N.getString("ASSIGN_VIP_SUBJ"), I18N.getString("ASSIGN_VIP_BODY"), Notifications.Severity.INFO),
    RELEASE_VIP(I18N.getString("RELEASE_VIP_SUBJ"), I18N.getString("RELEASE_VIP_BODY"), Notifications.Severity.INFO),
    WITNESS_EXITED(I18N.getString("WITNESS_EXITED_SUBJ"), I18N.getString("WITNESS_EXITED_BODY"), Notifications.Severity.INFO),
    MASTER_EXITED(I18N.getString("MASTER_EXITED_SUBJ"), I18N.getString("MASTER_EXITED_BODY"), Notifications.Severity.INFO),
    COORD_SEES_MASTER_EXITED(I18N.getString("COORD_SEES_MASTER_EXITED_SUBJ"), I18N.getString("COORD_SEES_MASTER_EXITED_BODY"), Notifications.Severity.INFO),
    STANDBY_EXITED(I18N.getString("STANDBY_EXITED_SUBJ"), I18N.getString("STANDBY_EXITED_BODY"), Notifications.Severity.INFO),
    PROMOTING_EXITED(I18N.getString("PROMOTING_EXITED_SUBJ"), I18N.getString("PROMOTING_EXITED_BODY"), Notifications.Severity.INFO),
    IDLE_EXITED(I18N.getString("IDLE_EXITED_SUBJ"), I18N.getString("IDLE_EXITED_BODY"), Notifications.Severity.INFO),
    AGENT_EXITED(I18N.getString("AGENT_EXITED_SUBJ"), I18N.getString("AGENT_EXITED_BODY"), Notifications.Severity.INFO),
    AUTO_RESUME_STARTED(I18N.getString("AUTO_RESUME_STARTED_SUBJ"), I18N.getString("AUTO_RESUME_STARTED_BODY"), Notifications.Severity.INFO),
    RESUMED_SCRIPT_RUN(I18N.getString("RESUMED_SCRIPT_RUN_SUBJ"), I18N.getString("RESUMED_SCRIPT_RUN_BODY"), Notifications.Severity.INFO),
    VIP_ASSIGNED_TO_NON_MASTER(I18N.getString("VIP_ASSIGNED_TO_NON_MASTER_SUBJ"), I18N.getString("VIP_ASSIGNED_TO_NON_MASTER_BODY"), Notifications.Severity.WARNING),
    VIP_NOT_ASSIGNED_TO_MASTER(I18N.getString("VIP_NOT_ASSIGNED_TO_MASTER_SUBJ"), I18N.getString("VIP_NOT_ASSIGNED_TO_MASTER_BODY"), Notifications.Severity.WARNING, 5, TimeUnit.MINUTES),
    NO_STANDBY(I18N.getString("NO_STANDBY_SUBJ"), I18N.getString("NO_STANDBY_BODY"), Notifications.Severity.WARNING),
    NO_STANDBY_BUT_DB_UP(I18N.getString("NO_STANDBY_BUT_DB_UP_SUBJ"), I18N.getString("NO_STANDBY_BUT_DB_UP_BODY"), Notifications.Severity.WARNING),
    STANDBY_DB_FAILED(I18N.getString("STANDBY_DB_FAIL_SUBJ"), I18N.getString("STANDBY_DB_FAIL_BODY"), Notifications.Severity.WARNING),
    STANDBY_AGENT_CANNOT_REACH_DB(I18N.getString("STANDBY_DB_FAIL_BUT_DB_UP_SUBJ"), I18N.getString("STANDBY_DB_FAIL_BUT_DB_UP_BODY"), Notifications.Severity.WARNING),
    FEWER_THAN_THREE_NODES(I18N.getString("FEWER_THAN_THREE_SUBJ"), I18N.getString("FEWER_THAN_THREE_BODY"), Notifications.Severity.WARNING),
    CLUSTER_SUBSET_NO_MASTER(I18N.getString("CLUSTER_SUBSET_NO_MASTER_SUBJ"), I18N.getString("CLUSTER_SUBSET_NO_MASTER_BODY"), Notifications.Severity.WARNING),
    PROMOTION_STARTED(I18N.getString("PROMOTION_STARTED_SUBJ"), I18N.getString("PROMOTION_STARTED_BODY"), Notifications.Severity.WARNING),
    WITNESS_FAIL(I18N.getString("NO_WITNESS_SUBJ"), I18N.getString("NO_WITNESS_BODY"), Notifications.Severity.WARNING),
    IDLE_FAIL(I18N.getString("NO_IDLE_SUBJ"), I18N.getString("NO_IDLE_BODY"), Notifications.Severity.WARNING),
    NODE_ISOLATED(I18N.getString("NODE_ISOLATED_SUBJ"), I18N.getString("NODE_ISOLATED_BODY"), Notifications.Severity.WARNING, 10, TimeUnit.MINUTES),
    NODE_NOT_ISOLATED(I18N.getString("NODE_NOT_ISOLATED_SUBJ"), I18N.getString("NODE_NOT_ISOLATED_BODY"), Notifications.Severity.WARNING, 10, TimeUnit.MINUTES),
    PROMOTE_BUT_MASTER_UP(I18N.getString("PROMOTE_BUT_MASTER_UP_SUBJ"), I18N.getString("PROMOTE_BUT_MASTER_UP_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_MASTER_REJOIN(I18N.getString("PROMOTE_BUT_MASTER_REJOIN_SUBJ"), I18N.getString("PROMOTE_BUT_MASTER_REJOIN_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_MASTER_UNKNOWN(I18N.getString("PROMOTE_BUT_MASTER_UNKNOWN_SUBJ"), I18N.getString("PROMOTE_BUT_MASTER_UNKNOWN_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_VIP_ASSIGNED(I18N.getString("PROMOTE_BUT_VIP_ASSIGNED_SUBJ"), I18N.getString("PROMOTE_BUT_VIP_ASSIGNED_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_ORPHANED(I18N.getString("PROMOTE_BUT_ORPHANED_SUBJ"), I18N.getString("PROMOTE_BUT_ORPHANED_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_NO_STANDBY(I18N.getString("PROMOTE_BUT_NO_STANDBY_SUBJ"), I18N.getString("PROMOTE_BUT_NO_STANDBY_BODY"), Notifications.Severity.SEVERE),
    FAILOVER_DISABLED(I18N.getString("FAILOVER_DISABLED_SUBJ"), I18N.getString("FAILOVER_DISABLED_BODY"), Notifications.Severity.WARNING),
    FAILOVER_COMPLETE(I18N.getString("FAILOVER_COMPLETE_SUBJ"), I18N.getString("FAILOVER_COMPLETE_BODY"), Notifications.Severity.WARNING),
    LOCK_FILE_REMOVED(I18N.getString("LOCK_FILE_REMOVED_SUBJ"), I18N.getString("LOCK_FILE_REMOVED_BODY"), Notifications.Severity.WARNING),
    RECOVERY_CONF_FILE_EXISTS(I18N.getString("RECOVERY_CONF_FILE_EXISTS_SUBJ"), I18N.getString("RECOVERY_CONF_FILE_EXISTS_BODY"), Notifications.Severity.WARNING),
    PROMOTE_BUT_ALREADY_PROMOTING(I18N.getString("PROMOTE_BUT_ALREADY_PROMOTING_SUBJ"), I18N.getString("PROMOTE_BUT_ALREADY_PROMOTING_BODY"), Notifications.Severity.WARNING),
    RECONFIGURE_OFF(I18N.getString("RECONFIGURE_OFF_SUBJ"), I18N.getString("RECONFIGURE_OFF_BODY"), Notifications.Severity.WARNING),
    RESUME_FAILED_PROMOTED_NODE(I18N.getString("RESUME_FAILED_PROMOTED_NODE_SUBJ"), I18N.getString("RESUME_FAILED_PROMOTED_NODE_BODY"), Notifications.Severity.WARNING),
    RESUME_FAILED(I18N.getString("RESUME_FAILED_SUBJ"), I18N.getString("RESUME_FAILED_BODY"), Notifications.Severity.WARNING),
    TRIAL_LICENSE_EXPIRING(I18N.getString("TRIAL_LICENSE_EXPIRING_SUBJ"), I18N.getString("TRIAL_LICENSE_EXPIRING_BODY"), Notifications.Severity.WARNING, 1, TimeUnit.DAYS),
    FULL_LICENSE_EXPIRING(I18N.getString("FULL_LICENSE_EXPIRING_SUBJ"), I18N.getString("FULL_LICENSE_EXPIRING_BODY"), Notifications.Severity.WARNING, 1, TimeUnit.DAYS),
    LICENSE_EXPIRED_NO_SHUTDOWN(I18N.getString("LICENSE_EXPIRED_NO_SHUTDOWN_SUBJ"), I18N.getString("LICENSE_EXPIRED_NO_SHUTDOWN_BODY"), Notifications.Severity.WARNING, 1, TimeUnit.DAYS),
    LICENSE_INVALID_NO_SHUTDOWN(I18N.getString("LICENSE_INVALID_NO_SHUTDOWN_SUBJ"), I18N.getString("LICENSE_INVALID_NO_SHUTDOWN_BODY"), Notifications.Severity.WARNING, 1, TimeUnit.DAYS),
    REMOTE_TIMEOUT_TOO_HIGH(I18N.getString("REMOTE_TIMEOUT_TOO_HIGH_SUBJ"), I18N.getString("REMOTE_TIMEOUT_TOO_HIGH_BODY"), Notifications.Severity.WARNING),
    DB_UP_BUT_ERROR(I18N.getString("DB_UP_BUT_ERROR_SUBJ"), (String)null, Notifications.Severity.SEVERE, 10, TimeUnit.MINUTES),
    MASTER_CANNOT_PING_RUNNING_DB(I18N.getString("MASTER_CANNOT_PING_RUNNING_DB_SUBJ"), I18N.getString("MASTER_CANNOT_PING_RUNNING_DB_BODY"), Notifications.Severity.SEVERE),
    NODE_IP_ERROR(I18N.getString("NODE_IP_ERROR_SUBJ"), I18N.getString("NODE_IP_ERROR_BODY"), Notifications.Severity.SEVERE),
    FENCING_SCRIPT_ERROR(I18N.getString("FENCING_SCRIPT_ERROR_SUBJ"), I18N.getString("FENCING_SCRIPT_ERROR_BODY"), Notifications.Severity.SEVERE),
    POST_PROMOTION_SCRIPT_FAILURE(I18N.getString("POST_PROMOTION_SCRIPT_FAIL_SUBJ"), I18N.getString("POST_PROMOTION_SCRIPT_FAIL_BODY"), Notifications.Severity.SEVERE),
    POST_DB_FAIL_SCRIPT_FAILURE(I18N.getString("POST_DB_FAIL_SCRIPT_FAIL_SUBJ"), I18N.getString("POST_DB_FAIL_SCRIPT_FAIL_BODY"), Notifications.Severity.SEVERE),
    RESUMED_SCRIPT_FAILURE(I18N.getString("RESUMED_SCRIPT_FAILURE_SUBJ"), I18N.getString("RESUMED_SCRIPT_FAILURE_BODY"), Notifications.Severity.SEVERE, 10, TimeUnit.MINUTES),
    MASTER_ISO_SCRIPT_FAILURE(I18N.getString("MASTER_ISO_SCRIPT_FAIL_SUBJ"), I18N.getString("MASTER_ISO_SCRIPT_FAIL_BODY"), Notifications.Severity.SEVERE),
    TRIGGER_FAILED(I18N.getString("TRIGGER_FAILED_SUBJ"), I18N.getString("TRIGGER_FAILED_BODY"), Notifications.Severity.SEVERE),
    RECOVERY_CONF_ERROR(I18N.getString("RECOVERY_CONF_ERROR_SUBJ"), I18N.getString("RECOVERY_CONF_ERROR_BODY"), Notifications.Severity.SEVERE),
    UNEXPECTED_ERROR(I18N.getString("UNEXPECTED_PROBLEM_SUBJECT"), I18N.getString("UNEXPECTED_PROBLEM_BODY"), Notifications.Severity.SEVERE, 5, TimeUnit.MINUTES),
    MASTER_ISOLATED_FROM_MAJORITY(I18N.getString("MASTER_ISOLATED_FROM_MAJORITY_SUBJ"), I18N.getString("MASTER_ISOLATED_FROM_MAJORITY_BODY"), Notifications.Severity.SEVERE),
    MASTER_ISOLATED_FROM_MAJORITY_REJOIN(I18N.getString("MASTER_ISOLATED_FROM_MAJORITY_REJOIN_SUBJ"), I18N.getString("MASTER_ISOLATED_FROM_MAJORITY_REJOIN_BODY"), Notifications.Severity.SEVERE),
    ASSIGN_VIP_FAILED(I18N.getString("ASSIGN_VIP_FAILED_SUBJ"), I18N.getString("ASSIGN_VIP_FAILED_BODY"), Notifications.Severity.SEVERE),
    DB_FAILURE(I18N.getString("DATABASE_FAILURE_SUBJ"), I18N.getString("DATABASE_FAILURE_BODY"), Notifications.Severity.SEVERE),
    MONITOR_TIMING_OUT(I18N.getString("MONITOR_TIMING_OUT_SUBJ"), I18N.getString("MONITOR_TIMING_OUT_BODY"), Notifications.Severity.SEVERE, 30, TimeUnit.MINUTES),
    RESTART_RESUME_TIMED_OUT(I18N.getString("RESTART_RESUME_TIMED_OUT_SUBJ"), I18N.getString("RESTART_RESUME_TIMED_OUT_BODY"), Notifications.Severity.SEVERE),
    LICENSE_EXPIRED_SHUTDOWN(I18N.getString("LICENSE_EXPIRED_SHUTDOWN_SUBJ"), I18N.getString("LICENSE_EXPIRED_SHUTDOWN_BODY"), Notifications.Severity.SEVERE),
    LICENSE_INVALID_SHUTDOWN(I18N.getString("LICENSE_INVALID_SHUTDOWN_SUBJ"), I18N.getString("LICENSE_INVALID_SHUTDOWN_BODY"), Notifications.Severity.SEVERE),
    CLUSTER_STATE_MISMATCH(I18N.getString("CLUSTER_STATE_MISMATCH_SUBJ"), I18N.getString("CLUSTER_STATE_MISMATCH_BODY"), Notifications.Severity.SEVERE, 5, TimeUnit.MINUTES),
    PROMOTE_BUT_NOT_ENOUGH_STANDBYS(I18N.getString("PROMOTE_BUT_NOT_ENOUGH_STANDBYS_SUBJ"), I18N.getString("PROMOTE_BUT_NOT_ENOUGH_STANDBYS_BODY"), Notifications.Severity.SEVERE),
    DATABASE_SHOULD_BE_IN_RECOVERY(I18N.getString("DATABASE_SHOULD_BE_IN_RECOVERY_SUBJ"), I18N.getString("DATABASE_SHOULD_BE_IN_RECOVERY_BODY"), Notifications.Severity.SEVERE, 1, TimeUnit.HOURS),
    DATABASE_SHOULD_NOT_BE_IN_RECOVERY(I18N.getString("DATABASE_SHOULD_NOT_BE_IN_RECOVERY_SUBJ"), I18N.getString("DATABASE_SHOULD_NOT_BE_IN_RECOVERY_BODY"), Notifications.Severity.SEVERE, 1, TimeUnit.HOURS),
    PING_NEW_DB_FAIL(I18N.getString("PING_NEW_DB_FAIL_SUBJ"), I18N.getString("PING_NEW_DB_FAIL_BODY"), Notifications.Severity.SEVERE);

    private static final Logger LOGGER = LogManager.getEfmLogger();
    private static final ThreadLocal<String[]> BODY_PARAMS = new ThreadLocal();
    private static final ThreadLocal<String[]> SUBJECT_PARAMS = new ThreadLocal();
    private final String[] emailAddrs;
    private final String notificationScript;
    private final String subject;
    private final String body;
    private final Notifications.Severity severity;
    private final int quietPeriod;
    private final TimeUnit unit;
    private long okTimeToSend;

    private Notifications(String subject, String body, Notifications.Severity severity) {
        this(subject, body, severity, 0, TimeUnit.SECONDS);
    }

    private Notifications(String subject, String body, Notifications.Severity severity, int quietPeriod, TimeUnit unit) {
        this.okTimeToSend = 0L;
        Environment env = Environment.getEnvironment();
        this.emailAddrs = env.getUserEmails();
        this.notificationScript = env.getNotificationScript();
        this.subject = subject;
        this.body = body;
        this.severity = severity;
        this.quietPeriod = quietPeriod;
        this.unit = unit;
    }

    public Notifications addSubjectParams(String... params) {
        SUBJECT_PARAMS.set(params);
        return this;
    }

    public Notifications addBodyParams(String... params) {
        BODY_PARAMS.set(params);
        return this;
    }

    public synchronized void send() {
        String[] params = (String[])SUBJECT_PARAMS.get();
        String tmpSubject = this.subject;
        if (params != null) {
            tmpSubject = MessageFormat.format(this.subject, params);
        }

        String msgSubject = "[" + this.severity.name() + "] EFM " + tmpSubject;
        if (System.currentTimeMillis() < this.okTimeToSend) {
            LOGGER.log(Level.WARNING, "NOT sending following message. Was sent too recently. Subject: {0}", msgSubject);
        } else {
            String tmpBody = this.body;
            params = (String[])BODY_PARAMS.get();
            if (this.body == null) {
                if (params == null || params.length < 1) {
                    throw new AssertionError("Null message body for: '" + msgSubject + "'");
                }

                tmpBody = params[0];
            } else if (params != null) {
                tmpBody = MessageFormat.format(this.body, params);
            }

            Environment env = Environment.getEnvironment();
            StringBuilder msgBody = (new StringBuilder()).append("EFM node:     ").append(env.getBindingAddress()).append("\n").append("Cluster name:  ").append(env.getClusterName()).append("\n").append("Database name: ").append(env.getDBDatabase()).append("\n").append("VIP support:   ").append(env.isVipEnabled() ? "ENABLED" : "DISABLED").append("\n");
            if (!env.autoFailoverOn()) {
                msgBody.append("Auto Failover: DISABLED\n");
            }

            msgBody.append("\n").append(tmpBody);
            String fullBody = msgBody.toString();
            this.sendMail(msgSubject, fullBody);
            this.callScript(msgSubject, fullBody);
            this.okTimeToSend = System.currentTimeMillis() + this.unit.toMillis((long)this.quietPeriod);
            SUBJECT_PARAMS.remove();
            BODY_PARAMS.remove();
        }
    }

    private void sendMail(String msgSubject, String msgBody) {
        if (this.emailAddrs != null) {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", "localhost");
            Session session = Session.getInstance(properties);

            try {
                LOGGER.log(Level.INFO, "Sending notification:\nTo: {0}\nSubject: {1}\nBody:\n{2}", new Object[]{Arrays.toString(this.emailAddrs), msgSubject, msgBody});
                MimeMessage message = new MimeMessage(session);
                String[] arr$ = this.emailAddrs;
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String email = arr$[i$];
                    message.addRecipient(RecipientType.TO, new InternetAddress(email));
                }

                message.setSubject(msgSubject);
                message.setText(msgBody);
                message.setFrom(new InternetAddress("efm@localhost"));
                LOGGER.fine("About to call Transport.send().");
                Transport.send(message);
                LOGGER.fine("Transport.send() has returned.");
            } catch (MessagingException var10) {
                LOGGER.log(Level.WARNING, "Unable to send mail to: {0}", Arrays.toString(this.emailAddrs));
                LOGGER.warning(var10.toString());
            }

        }
    }

    private void callScript(String msgSubject, String msgBody) {
        if (this.notificationScript != null && !this.notificationScript.isEmpty()) {
            String[] command = new String[]{this.notificationScript, msgSubject, msgBody};
            ProcessResult result = ExecUtil.performExec(command);
            if (result.getExitValue() != 0) {
                LOGGER.log(Level.SEVERE, "Could not execute notifications script: {0}", result.getErrorOut());
            }

        }
    }

    static enum Severity {
        INFO,
        WARNING,
        SEVERE;

        private Severity() {
        }
    }
}
