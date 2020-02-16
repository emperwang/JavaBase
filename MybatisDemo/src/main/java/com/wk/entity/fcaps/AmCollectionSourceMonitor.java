package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class AmCollectionSourceMonitor extends AmCollectionSourceMonitorKey {
    private Date checkPoint;

    private Integer alarmSeq;

    private Date heartbeatTime;

    private Date updateTime;

    private boolean ifUnclearedAlarmExists;
}