package com.enterprisedb.efm.utils;

import com.enterprisedb.efm.DBMonitor;
import com.enterprisedb.efm.exceptions.PasswordDecryptException;
import org.jgroups.Address;

public class StandbyInfo {
    private final Address address;

    private final String host;

    private final DBMonitor dbMonitor;

    private volatile String replayLocation;

    public StandbyInfo(Address address, String host) throws PasswordDecryptException {
        if (host == null)
            throw new AssertionError("Cannot pass in null host.");
        this.address = address;
        this.host = host;
        this.dbMonitor = DBUtils.createMonitor(host);
    }

    public Address getAddress() {
        return this.address;
    }

    public String getHost() {
        return this.host;
    }

    public DBMonitor getDbMonitor() {
        return this.dbMonitor;
    }

    public String getReplayLocation() {
        return this.replayLocation;
    }

    public void setReplayLocation(String replayLocation) {
        this.replayLocation = replayLocation;
    }

    public String toString() {
        return "StandbyInfo{address=" + this.address + ", host='" + this.host + '\'' + ", replayLocation='" + this.replayLocation + '\'' + '}';
    }
}
