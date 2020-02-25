package com.wk.fcaps.socket.constants;

import lombok.extern.slf4j.Slf4j;

/**
 *  OMC请求的消息头字段
 */
@Slf4j
public class OMCSocketMsgHeaders {
    public static final short realTimeAlarm_Int = 0;
    public static final short reqLoginAlarm_Int = 1;
    public static final short ackLoginAlarm_Int = 2;
    public static final short reqSyncAlarmMsg_Int = 3;
    public static final short ackSyncAlarmMsg_Int = 4;
    public static final short reqSyncAlarmFile_Int = 5;
    public static final short ackSyncAlarmFile_Int = 6;
    public static final short ackSyncAlarmFileResult_Int = 7;
    public static final short reqHeartBeat_Int = 8;
    public static final short ackHeartBeat_Int = 9;
    public static final short closeConnAlarm_Int = 10;

    public static final String realTimeAlarm = "realTimeAlarm";
    public static final String reqLoginAlarm = "reqLoginAlarm";
    public static final String ackLoginAlarm = "ackLoginAlarm";
    public static final String reqSyncAlarmMsg = "reqSyncAlarmMsg";
    public static final String ackSyncAlarmMsg = "ackSyncAlarmMsg";
    public static final String reqSyncAlarmFile = "reqSyncAlarmFile";
    public static final String ackSyncAlarmFile = "ackSyncAlarmFile";
    public static final String ackSyncAlarmFileResult = "ackSyncAlarmFileResult";
    public static final String reqHeartBeat = "reqHeartBeat";
    public static final String ackHeartBeat = "ackHeartBeat";
    public static final String closeConnAlarm = "closeConnAlarm";
    // 连接omc时，设定数据采集方式
    public static final String SocketType = "msg";
    public static final String FTPType = "ftp";
    // omc告警文件方式同步时的数据源设置
    public static final Integer FileSyncSource0 = 0;
    public static final Integer FileSyncSource1 = 1;


    // socket超时时间
    public static final int SocketTimeOut = 10 * 1000;

    // 心跳间隔时间 60秒
    public static final long HeartBeatInterval = 60;

    // OMC消息返回超时时间 180秒 如果180秒内没有收到OMC返回的消息，则端口连接，重新建立连接
    public static final long ReceiveTimeOut = 180;

    // sockek客户端重连服务端次数
    public static final int SocketRetry = 3;

    // 告警消息序号的步伐最大限制，当两次序号差距大于1000，客户端不应向OMC主动发起告警同步
    public static final long AlarmSeqMaxLastLimit = 1000;

    /**
     *  配置登录发送包
     * @param user
     * @param key
     * @param type msg:socket传输消息   ftp:文件采集
     * @return
     */
    public static SocketPacket configLoginPacket(String user,String key,String type){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.reqLoginAlarm);
        loginbuffer.append(";user=");
        loginbuffer.append(user);
        loginbuffer.append(";key=");
        loginbuffer.append(key);
        loginbuffer.append(";type=");
        loginbuffer.append(type);
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqLoginAlarm_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        log.info("socket login msg is :{}， length:{}",loginbuffer.toString(),
                (short) (loginbuffer.toString().length()));
        return socketPacket;
    }

    public static SocketPacket configLoginResultPacket(){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.ackLoginAlarm);
        loginbuffer.append(";result=succ;");
        loginbuffer.append("resDesc");
        socketPacket.setMsgType(OMCSocketMsgHeaders.ackLoginAlarm_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        log.info("socket login result msg is :" + OMCSocketMsgHeaders.ackLoginAlarm + ";result=succ;resDesc");
        return socketPacket;
    }

    /**
     *  关闭连接
     * @return
     */
    public static SocketPacket configLoginOutPacket(){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.closeConnAlarm);
        socketPacket.setMsgType(OMCSocketMsgHeaders.closeConnAlarm_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        return socketPacket;
    }

    /**
     *  消息同步包
     * @return
     */
    public static SocketPacket configMsgSyncPacket(Integer reqId,Integer alarmSeq){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.reqSyncAlarmMsg);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        loginbuffer.append(";alarmSeq=");
        loginbuffer.append(alarmSeq);
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqSyncAlarmMsg_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        return socketPacket;
    }

    public static SocketPacket configMsgSyncResultPacket(Integer reqId,Integer alarmSeq){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.ackSyncAlarmMsg);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        loginbuffer.append(";result=succ;resDesc=null");
        loginbuffer.append(alarmSeq);
        socketPacket.setMsgType(OMCSocketMsgHeaders.ackSyncAlarmMsg_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        return socketPacket;
    }

    /**
     *  配置文件同步包
     * @return
     */
    public static SocketPacket configFileSyncPacket(Integer reqId,String startTime,
                                                    String endTime,Integer syncSource){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.reqSyncAlarmFile);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        loginbuffer.append(";startTime=");
        loginbuffer.append(startTime);
        loginbuffer.append(";endTime=");
        loginbuffer.append(endTime);
        loginbuffer.append(";syncSource=");
        loginbuffer.append(syncSource);
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqSyncAlarmMsg_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        return socketPacket;
    }
    public static SocketPacket configFileSyncPacket(Integer reqId,Integer alarmSeq,Integer syncSource){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.reqSyncAlarmFile);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        loginbuffer.append(";alarmSeq=");
        loginbuffer.append(alarmSeq);
        loginbuffer.append(";syncSource=");
        loginbuffer.append(syncSource);
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqSyncAlarmFile_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        return socketPacket;
    }
    /**
     *  心跳包
     * @return
     */
    public static SocketPacket configHeartBeatPacket(Integer reqId){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.reqHeartBeat);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqHeartBeat_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        log.info("send heart beat package : {}",loginbuffer.toString());
        return socketPacket;
    }

    public static SocketPacket configHeartBeatAckPacket(Integer reqId){
        SocketPacket socketPacket = new SocketPacket();
        StringBuilder loginbuffer = new StringBuilder();
        loginbuffer.append(OMCSocketMsgHeaders.ackHeartBeat);
        loginbuffer.append(";reqId=");
        loginbuffer.append(reqId);
        socketPacket.setMsgType(OMCSocketMsgHeaders.ackHeartBeat_Int);
        socketPacket.setBody(loginbuffer.toString());
        socketPacket.setLenOfBody((short) (loginbuffer.toString().length()));
        log.info("send heart beat ack package : {}",loginbuffer.toString());
        return socketPacket;
    }

    public static String str ="\"locationInfo\": \"36.0Snapshots Availability is 36.0Snapshots Availability is -ProbableCause(OSS)= (Linux) bjenmms01 -  (Snapshots Status) Snapshots Status - -EventType(OSS)=bjenmms01 - Snapshots Status - Snapshots Availability\"," +
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

    public static SocketPacket msgPacket(){
        SocketPacket packet = new SocketPacket();
        packet.setMsgType(OMCSocketMsgHeaders.realTimeAlarm_Int);
        packet.setBody(str);
        packet.setLenOfBody((short)str.length());
        return packet;
    }
}
