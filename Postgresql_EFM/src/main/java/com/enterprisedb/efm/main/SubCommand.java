package com.enterprisedb.efm.main;

public enum SubCommand {
    START("start", 2),
    STOP("stop", 2),
    INT_START("__int_start", 2),
    RESUME("resume", 2),
    STOP_CLUSTER("stop-cluster", 2),
    PROMOTE("promote", 3),
    FORCE_PROMOTE("force-promote", 3),
    CLUSTER_STATUS("cluster-status", 2),
    CLUSTER_STATUS_JSON("cluster-status-json", 2),
    ENCRYPT("encrypt", 3),
    PROP("prop-check", 2),
    ALLOW_NODE("allow-node", 3),
    DISALLOW_NODE("disallow-node", 3),
    SET_PRIORITY("set-priority", 4),
    ADD_NODE("add-node", 4),
    REMOVE_NODE("remove-node", 3),
    UPGRADE("upgrade-conf", 2);

    public static final String SWITCHOVER = "-switchover";

    public static final String EFM_PASS = "EFMPASS";

    public static final String PASS_FROM_ENV = "--from-env";

    private final String value;

    private final int maxArgs;

    SubCommand(String value, int maxArgs) {
        this.value = value;
        this.maxArgs = maxArgs;
    }

    public String getValue() {
        return this.value;
    }

    public int getMaxArgs() {
        return this.maxArgs;
    }

    public static SubCommand getCommand(String[] args, String errMsg) {
        String cmdString = args[0];
        for (SubCommand c : values()) {
            if (c.getValue().equalsIgnoreCase(cmdString)) {
                if (args.length > c.getMaxArgs()) {
                    System.err.println(String.format("Too many parameters for %s command.", new Object[] { cmdString }));
                    return null;
                }
                return c;
            }
        }
        System.err.println(errMsg);
        return null;
    }
}
