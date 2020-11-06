package com.enterprisedb.efm.exec;

public enum SudoFunctions {
    WRITE_TRIGGER_FILE("writetriggerfile"),
    READ_TRIGGER_FILE_LOCATION("readtriggerlocation"),
    WRITE_RECOVERY_CONF("writerecoveryconf"),
    WRITE_CUSTOM_RECOVERY_CONF("writecustomrecoveryconf"),
    REMOVE_RECOVERY_CONF("removerecoveryconf"),
    VALIDATE_RECOVERY_CONF("validaterecoveryconf"),
    VALIDATE_DB_BIN("validatepgbin"),
    RECOVERY_CONF_EXISTS("recoveryconfexists"),
    CHANGE_MASTER("changemasterhost"),
    RESTART_DB("restartdb"),
    START_DB("startdb"),
    STOP_DB("stopdb"),
    READ_RECOVERY_CONF("readrecoveryconf"),
    VALIDATE_DB_OWNER("validatedbowner"),
    WRITE_PID_KEY("writepidkey"),
    DELETE_PID_KEY("deletepidkey"),
    READ_NODES("readnodes"),
    WRITE_NODES("writenodes"),
    CHECK_AUTH("checkauthfileperms"),
    RESTART_DB_SERVICE("restartdbservice"),
    START_DB_SERVICE("startdbservice"),
    STOP_DB_SERVICE("stopdbservice"),
    DB_SERVICE_STATUS("dbservicestatus"),
    RELEASE("release"),
    ASSIGN("assign"),
    RELEASE_6("release6"),
    ASSIGN_6("assign6");

    private final String fnName;

    SudoFunctions(String name) {
        this.fnName = name;
    }

    public String toString() {
        return this.fnName;
    }
}
