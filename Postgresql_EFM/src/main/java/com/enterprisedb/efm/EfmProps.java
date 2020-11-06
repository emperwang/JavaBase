package com.enterprisedb.efm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * @author: wk
 * @Date: 2020/11/6 16:40
 * @Description
 */
public enum EfmProps {
    DB_SERVICE_OWNER("db.service.owner", true, false, false),
    DB_PORT("db.port", true, true, true),
    DB_USER("db.user", true, true, false),
    DB_PASSWORD("db.password.encrypted", true, true, false),
    DB_DATABASE("db.database", true, true, false),
    DB_RECOVERY_CONF_DIR("db.recovery.conf.dir", true, false, false),
    DB_PG_BIN_DIR("db.bin", false, false, false),
    DB_SERVICE_NAME("db.service.name", false, false, false),
    DB_REUSE_CONNECTION_COUNT("db.reuse.connection.count", true, false, false),
    JDBC_SSL("jdbc.ssl", false, false, false),
    JDBC_SSL_MODE("jdbc.ssl.mode", false, false, false),
    ADMIN_PORT("admin.port", true, true, false),
    AUTO_ALLOW_HOSTS("auto.allow.hosts", true, true, false),
    LOCAL_TIMEOUT("local.timeout", true, false, false),
    LOCAL_PERIOD("local.period", true, false, false),
    LOCAL_FINAL_TIMEOUT("local.timeout.final", true, false, false),
    REMOTE_TIMEOUT("remote.timeout", true, true, false),
    JGROUPS_TOTAL_TIMEOUT("node.timeout", true, true, true),
    AUTO_FAILOVER("auto.failover", true, true, true),
    AUTO_STANDBY_RECONFIGURE("auto.reconfigure", true, false, false),
    STANDBY_AVAIL("promotable", true, false, false),
    MIN_STANDBYS("minimum.standbys", true, true, true),
    EFM_IS_WITNESS("is.witness", true, true, false),
    EFM_BIND_ADDRESS("bind.address", true, true, false),
    EFM_VIRT_IP("virtualIp", false, false, true),
    EFM_VIRT_IP_INTERFACE("virtualIp.interface", false, false, false),
    EFM_VIRT_IP_NETMASK("virtualIp.netmask", false, false, true),
    EFM_PING_SERVER("pingServerIp", true, true, false),
    EFM_PING_SERVER_COMMAND("pingServerCommand", true, true, false),
    RECOVERY_CHECK_PERIOD("recovery.check.period", true, false, false),
    AUTO_RESUME_PERIOD("auto.resume.period", true, false, false),
    EFM_NOTIFICATION_SCRIPT("script.notification", false, false, false),
    EFM_FENCE_SCRIPT("script.fence", false, false, false),
    EFM_POST_PROMOTE_SCRIPT("script.post.promotion", false, false, false),
    EFM_RESUME_SCRIPT("script.resumed", false, false, false),
    EFM_DB_FAIL_SCRIPT("script.db.failure", false, false, false),
    EFM_MASTER_ISO_SCRIPT("script.master.isolated", false, false, false),
    USER_EMAIL("user.email", false, false, true),
    JGROUPS_LOGLEVEL("jgroups.loglevel", false, false, false),
    EFM_LOGLEVEL("efm.loglevel", false, false, false),
    EFM_LICENSE("efm.license", false, false, true),
    TRIAL_PERIOD_DAYS("trial.period.days", false, false, true),
    LICENSE_POLLING_INTERVAL("license.polling.interval", false, false, true),
    SUDO_COMMAND("sudo.command", false, false, false),
    SUDO_USER_COMMAND("sudo.user.command", false, false, false),
    JVM_OPTIONS("jvm.options", false, false, false);

    private final String propName;

    private final boolean requiredByAgent;

    private final boolean requiredByWitness;

    private final boolean shared;

    private static final Map<EfmProps, Set<String>> PARAMS_MAP;

    static {
        PARAMS_MAP = new HashMap<EfmProps, Set<String>>();
        Set<String> okParams = new HashSet<String>();
        okParams.add("%p");
        okParams.add("%f");
        PARAMS_MAP.put(EFM_FENCE_SCRIPT, okParams);
        PARAMS_MAP.put(EFM_POST_PROMOTE_SCRIPT, okParams);
    }

    EfmProps(String name, boolean requiredByAgent, boolean requiredByWitness, boolean shared) {
        this.propName = name;
        this.requiredByAgent = requiredByAgent;
        this.requiredByWitness = requiredByWitness;
        this.shared = shared;
    }

    public String getPropName() {
        return this.propName;
    }

    public boolean isRequiredByAgent() {
        return this.requiredByAgent;
    }

    public boolean isRequiredByWitness() {
        return this.requiredByWitness;
    }

    public boolean isShared() {
        return this.shared;
    }

    public static Set<String> getAllowedParams(EfmProps prop) {
        return PARAMS_MAP.get(prop);
    }
}
