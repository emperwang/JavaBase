package com.wk.fcaps.socket.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OrigAlarm {

    private String uuid;
    private String alarmId;
    private String newAlarmId;
    private Long alarmSeq;
    private Date collectTimestamp;
    private String vendorId;
    private String sourceId;
    private String alarmContent;
}
