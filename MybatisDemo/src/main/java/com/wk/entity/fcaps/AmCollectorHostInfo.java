package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class AmCollectorHostInfo {
    private Integer collectorId;

    private String collectorName;

    private String collectorInnerIp;

    private String collectorExternalIp;

    private String state;

    private Date heartBeatTime;

    private Integer lostHeartbeatNum;

    private Integer processCapacity;

    private String nodeState;

    public Integer getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(Integer collectorId) {
        this.collectorId = collectorId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName == null ? null : collectorName.trim();
    }

    public String getCollectorInnerIp() {
        return collectorInnerIp;
    }

    public void setCollectorInnerIp(String collectorInnerIp) {
        this.collectorInnerIp = collectorInnerIp == null ? null : collectorInnerIp.trim();
    }

    public String getCollectorExternalIp() {
        return collectorExternalIp;
    }

    public void setCollectorExternalIp(String collectorExternalIp) {
        this.collectorExternalIp = collectorExternalIp == null ? null : collectorExternalIp.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(Date heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    public Integer getLostHeartbeatNum() {
        return lostHeartbeatNum;
    }

    public void setLostHeartbeatNum(Integer lostHeartbeatNum) {
        this.lostHeartbeatNum = lostHeartbeatNum;
    }

    public Integer getProcessCapacity() {
        return processCapacity;
    }

    public void setProcessCapacity(Integer processCapacity) {
        this.processCapacity = processCapacity;
    }
}