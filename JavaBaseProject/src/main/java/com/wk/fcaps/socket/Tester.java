package com.wk.fcaps.socket;

public class Tester {

    public static void StrPrint(){
        String str ="\"locationInfo\": \"36.0Snapshots Availability is 36.0Snapshots Availability is -ProbableCause(OSS)= (Linux) bjenmms01 -  (Snapshots Status) Snapshots Status - -EventType(OSS)=bjenmms01 - Snapshots Status - Snapshots Availability\"," +
                "\"specialty\": \"NFV\"," +
                "\"collectionTime\": \"2020-01-03 23:00:22\"," +
                "\"domainType\": \"CT\"," +
                "\"vendorId\": \"HW\"," +
                "\"pVFlag\": \"pim\"," +
                "\"objectType\": \"switch\"," +
                "\"province\": \"HE\"," +
                "\"alarmId\": \"761b53169a0e2dde6139d7f20f0f31eb\"," +
                "\"eventTime\": \"2020-01-03 20:30:40\"," +
                "\"alarmTitle\": \"PIM告警标题1server Controller 0 Failed!\"," +
                "\"sourceID\": \"pim001\"," +
                "\"specificProblemID\": \"3712.31\"," +
                "\"regionPath\": \"/0HB/0HE/2pim010\"," +
                "\"origSeverity\": 3," +
                "\"objectUID\": \"2d66a1eed9d14524afa8eRhbbTRhbbfT\"," +
                "\"subObjectUID\": \"4350\"," +
                "\"addInfo\": \"Project:null;DeviceName:Compute1;DeviceId:026XZY82\"," +
                "\"vendorName\": \"华为\"," +
                "\"uUID\": \"51337bff6af7468d8e6d921a88161e67\"," +
                "\"alarmStatus\": 1," +
                "\"alarmType\": \"1\"," +
                "\"initialAlarmId\": \"1\"," +
                "\"specificProblem\": \"Hardware Error\"," +
                "\"subObjectName\": \"Controller-0\"," +
                "\"subObjectType\": \"Controller\"," +
                "\"objectName\": \"Switch1\"," +
                "\"alarmSeq\": 475185," +
                "\"dataSource\": \"PIM\"" +
                "}";
        System.out.println(str.length()); //1029 + 9 = 1038
    }

    public static void main(String[] args) {
        StrPrint();
    }
}
