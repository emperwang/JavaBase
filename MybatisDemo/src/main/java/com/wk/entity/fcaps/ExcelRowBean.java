package com.wk.entity.fcaps;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author: wk
 * @Date: 2020/10/20 17:01
 * @Description
 */
@Setter
@Getter
@ToString
public class ExcelRowBean {
   private String  alarmId;
   private String  alarmTitle;
   private String  clearFlag;
   private String  ifRoot;
   private String  ifMaster;
   private String  ifSlave;
   private String  localProducer;
   private String  localOwner;
   private String  objectName;
   private String  objectType;
   private String  alarmType;
   private String  origSeverity;
   private String  netManagerAlarmSeverity;
   private String  dataSource;
   private String  vendorName;
   private String  eventTime;
   private String  latestArriveTime;
   private String  initialAlarmId;
   private Long alarmSeq;
   private String  specificProblemID;
   private String  specificProblem;
   private String  objectUID;
   private String  subObjectUID;
   private String  subObjectName;
   private String  subObjectType;
   private String  locationInfo;
   private String  addInfo;
   private String  pVFlag;
   private String  standardFlag;
   private String  confirmFlag;
   private String  specialty;
   private String  vmIdList;
   private String  hostIdList;
   private String  netManagerAlarmId;
   private String  businessType;
   private String  clearOperateUser;
   private String  clearTime;
   private String  domainType;
   private String  sourceID;
   private String  isFilter;
   private String  pFlag;
   private String  snssaiList;
   private String  nssiList;
   private String  nssiNameList;
}
