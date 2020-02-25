package com.wk.fcaps.socket.server;

import com.wk.fcaps.socket.constants.OMCSocketMsgHeaders;
import com.wk.fcaps.socket.constants.SocketPacket;
import com.wk.fcaps.socket.util.Socketutil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class ServerSimulator extends Thread {
    private static final Integer port = 19000;
    private static final String CharSet = "UTF-8";
    private static Socket socket = null;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static boolean runFlag = true;

    public ServerSimulator(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            while (runFlag){
                Map<String, Object> msg = Socketutil.serverReceiveSocket(socket);
                sleep(2);
                processMsg(msg);
            }
        } catch (IOException e) {
            log.error("IOException :{}",e.getMessage());
        }
    }

    private void pringMsg(Map<String, Object> msg){
        if (msg != null && !msg.isEmpty()){
            log.info("StartSign :{}, msgType:{}, timeStamp:{}, length:{}, body:{}",
                    (short)msg.get(Socketutil.StartSign), (byte)msg.get(Socketutil.MsgTypes),
                    (int)msg.get(Socketutil.TimeStamp),(int)msg.get(Socketutil.BodyLen),
                    (String)msg.get(Socketutil.Body));
        }else{
            log.debug("msg is empty or null.");
        }
    }

    private void processMsg(Map<String, Object> msg) throws IOException {
        pringMsg(msg);
        String body = getBody(msg);
        if (StringUtils.isNotBlank(body)){
            OutputStream outputStream = getOutputStream();
            if (body.contains(OMCSocketMsgHeaders.reqLoginAlarm)){ // login
                SocketPacket loginPacket = OMCSocketMsgHeaders.configLoginResultPacket();
                outputStream.write(loginPacket.getByteStream());
            }else if(body.contains(OMCSocketMsgHeaders.ackHeartBeat)){  // heartBeat
                SocketPacket heartBeatPacket = OMCSocketMsgHeaders.configHeartBeatPacket(1);
                outputStream.write(heartBeatPacket.getByteStream());
            }else if(body.contains(OMCSocketMsgHeaders.reqSyncAlarmMsg)){
                log.debug("reqSyncAlarmMsg");
                SocketPacket msgSyncResultPacket = OMCSocketMsgHeaders.configMsgSyncResultPacket(1, 2);
                outputStream.write(msgSyncResultPacket.getByteStream());
            }else if(body.contains(OMCSocketMsgHeaders.closeConnAlarm)){
                log.info("close socket connect.");
            }
            outputStream.flush();
        }else{
            log.debug("body is Invalid.");
        }
    }

    public static void writeDate(Socket socket) throws IOException {
        SocketPacket msgPacket = OMCSocketMsgHeaders.msgPacket();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(msgPacket.getByteStream());
        outputStream.flush();
    }

    public static void writeDate2() throws IOException {
        SocketPacket msgPacket = OMCSocketMsgHeaders.msgPacket();
        OutputStream outputStream = getOutputStream();
        byte[] stream = msgPacket.getByteStream();
        outputStream.write(stream,0,40);
        sleep(5);
        outputStream.write(stream,40,stream.length);
        outputStream.flush();
    }

    private int getStartSign(Map<String, Object> msg){
        return (int)msg.get(Socketutil.StartSign);
    }

    private int getMsgType(Map<String, Object> msg){
        return (int)msg.get(Socketutil.MsgTypes);
    }

    private int getTimeStamp(Map<String, Object> msg){
        return (int)msg.get(Socketutil.TimeStamp);
    }

    private int getBodyLength(Map<String, Object> msg){
        return (int)msg.get(Socketutil.BodyLen);
    }

    private String getBody(Map<String, Object> msg){
        return (String)msg.get(Socketutil.Body);
    }

    public static void StopRun(){
        runFlag = false;
    }

    public static void sleep(int secs){
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            log.error("sleep error:{}", e.getMessage());
        }
    }

    public static OutputStream getOutputStream(){
        return outputStream;
    }
}
