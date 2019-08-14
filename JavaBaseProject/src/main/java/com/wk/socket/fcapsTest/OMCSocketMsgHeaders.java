package com.wk.socket.fcapsTest;

/**
 *  OMC请求的消息头字段
 */
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
    public static final String reqLoginAlarm = "realLoginAlarm";
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
    public static final long HeartBeatInterval = 60 * 1000;

    // OMC消息返回超时时间 180秒 如果180秒内没有收到OMC返回的消息，则端口连接，重新建立连接
    public static final long ReceiveTimeOut = 180 * 1000;

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
        socketPacket.setMsgType(OMCSocketMsgHeaders.reqSyncAlarmMsg_Int);
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
        return socketPacket;
    }
}
