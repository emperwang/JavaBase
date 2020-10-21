package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author: wk
 * @Date: 2020/10/20 15:53
 * @Description
 */
@Setter
@Getter
@ToString
public class HisField {
    private String alarmId;
    private int alarmStatus;
    private Integer alarmSeq;
    private boolean clearFlag;
    private Date collectionTime;
    private Date eventTime;
    private String alarmContent;
    private boolean ifConfirm;
    private String confirmContent;
    private boolean ifComment;
    private String comment;
    private Date clearTime;
    private String clearOperateUser;
    private Date latestArriveTime;
    private boolean ifMaster;
    private boolean ifSlave;
    private String slaveCorrelationid;
    private Date updateTime;
}
