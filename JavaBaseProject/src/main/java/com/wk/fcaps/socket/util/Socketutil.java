package com.wk.fcaps.socket.util;

import com.wk.fcaps.socket.constants.OMCSocketMsgHeaders;
import com.wk.fcaps.socket.constants.SocketPacket;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Socketutil {
    private  static  final Logger logger = LoggerFactory.getLogger(Socketutil.class);

    private static final String CharSet = "UTF-8";

    /**
     *  连接
     * @param hostAddres
     * @param port
     * @return
     */
    public static Socket connectServer(String hostAddres, Integer port){
        Socket socket = null;
        try {
            socket = new Socket(hostAddres, port);
            socket.setKeepAlive(true);// 开启保持活动状态的套接字
            socket.setSoTimeout(OMCSocketMsgHeaders.SocketTimeOut);// 设置超时时间
            logger.info("create socket connect....");
        } catch (IOException e) {
            logger.info("create connect error,the msg is:{}",e.getMessage());
        }
        return socket;
    }

    /**
     *  发送数据包
     * @param socketPacket
     * @param socket
     * @throws Exception
     */
    public static void sendSocket(SocketPacket socketPacket, Socket socket) throws IOException {
        OutputStream output = null;
        output = socket.getOutputStream();
        // 写数据发送报文
        output.write(socketPacket.getByteStream());
        output.flush();
    }

    /**
     *  接收消息
     * @return
     */
    public static String receiveSocket(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] msgHeader = null;
        byte[] startSign = null;
        byte[] msgTyps = null;
        byte[] timeStamp = null;
        byte[] bodyLen = null;
        byte[] bodyBytes = null;

        msgHeader = getDataBytesFromStream(inputStream, 9);
        startSign = subBytes(msgHeader,0,2);
        msgTyps = subBytes(msgHeader,2,1);
        timeStamp = subBytes(msgHeader,3,4);
        bodyLen = subBytes(msgHeader,7,2);
        int lenght = bytesToShort(bodyLen);
        log.info("receive socket header is startSign:{},msgTyps:{},timeStamp:{},lenght:{}",bytesToShort(startSign), bytesTobyte(msgTyps),bytesToInt(timeStamp),lenght);
        byte[] body = getDataBytesFromStream(inputStream, lenght);


        String revBody = new String(body,CharSet);
        if (revBody != null && revBody.length() > 0) {
            logger.info("receive socket info :{}", revBody);
        }
        return revBody;
    }

    public static final String StartSign = "startSign";
    public static final String MsgTypes = "msgType";
    public static final String TimeStamp = "timeStamp";
    public static final String BodyLen = "bodyLen";
    public static final String Body = "body";
    public static Map<String,Object> serverReceiveSocket(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        Map<String,Object> res = new HashMap<>();
        byte[] msgHeader = null;
        byte[] startSign = null;
        byte[] msgTyps = null;
        byte[] timeStamp = null;
        byte[] bodyLen = null;
        byte[] bodyBytes = null;

        msgHeader = getDataBytesFromStream(inputStream, 9);
        startSign = subBytes(msgHeader,0,2);
        msgTyps = subBytes(msgHeader,2,1);
        timeStamp = subBytes(msgHeader,3,4);
        bodyLen = subBytes(msgHeader,7,2);
        int lenght = bytesToShort(bodyLen);
        log.info("receive socket header is startSign:{},msgTyps:{},timeStamp:{},lenght:{}",
                bytesToShort(startSign), bytesTobyte(msgTyps),bytesToInt(timeStamp),lenght);
        byte[] body = getDataBytesFromStream(inputStream, lenght);


        String revBody = new String(body,CharSet);
        if (revBody != null && revBody.length() > 0) {
            logger.info("receive socket info :{}", revBody);
        }
        res.put(StartSign, bytesToShort(startSign));
        res.put(MsgTypes, bytesTobyte(msgTyps));
        res.put(TimeStamp, bytesToInt(timeStamp));
        res.put(BodyLen, lenght);
        res.put(Body, revBody);
        return res;
    }



    public static short bytesToShort(byte[] b){
       short value =  (short) (b[1] & 0xFF | (b[0] & 0xFF) << 8);
       return value;
    }

    public static int bytesToInt(byte[] b){
        int value =  (b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF)<<16 | (b[0] & 0xFF) << 24);
        return value;
    }

    public static byte bytesTobyte(byte[] b){
        byte value =  (byte)(b[0] & 0xFF);
        return value;
    }

    /**
     *  读取输入流中的数据
     * @param inputStream
     * @param len
     * @return
     */
    public static byte[] getDataBytesFromStream(InputStream inputStream,int len) throws IOException {
        byte[] bytes = new byte[len];
        inputStream.read(bytes,0,len);
        return bytes;
    }

    /**
     *  截取byte数组
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public static byte[] subBytes(byte[] src,int begin,int count){
        byte[] data = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            data[i-begin] = src[i];
        }
        return data;
    }

    public static void closeOMCConnect(Socket socket) throws IOException {
        SocketPacket loginOutPacket = OMCSocketMsgHeaders.configLoginOutPacket();
        Socketutil.sendSocket(loginOutPacket,socket);
        closeSocket(socket);
    }

    /**
     *  关闭连接
     * @param socket
     */
    public static void closeSocket(Socket socket){
        if (socket != null){
            try {
                socket.close();
                logger.info(OMCSocketMsgHeaders.closeConnAlarm);
                logger.info("close socket connection..");
            } catch (IOException e) {
                logger.error("close socket error,erroe msg is:{}",e.getMessage());
            }
        }
    }

    public static Socket loginSocket(String hostAddres, Integer port, String userName, String password) throws Exception{
        return login(hostAddres, port, userName, password,OMCSocketMsgHeaders.SocketType);
    }

    /**
     *  登录
     * @return
     */
    public static Socket login(String hostAddres, Integer port, String userName,String
            password,String msgType) throws Exception {
        Socket socket = Socketutil.connectServer(hostAddres,port);
        // 2. 登录
        SocketPacket loginPacket = OMCSocketMsgHeaders.configLoginPacket(userName, password, msgType);
        try {
            Socketutil.sendSocket(loginPacket, socket);
            String loginResult = Socketutil.receiveSocket(socket);
            logger.info("socket login msg body is:{}",loginResult);
            int failCount = 0;
            while (!analysisLoginResult(loginResult)){
                failCount++;
                Thread.sleep(2000);
                loginPacket = OMCSocketMsgHeaders.configLoginPacket(userName, password, msgType);
                Socketutil.sendSocket(loginPacket, socket);
                loginResult = Socketutil.receiveSocket(socket);
                if (failCount >= 3){
                    throw new RuntimeException("123 socket login failed more than three times");
                }
            }
        } catch (IOException e) {
            logger.error("socket login IOException error,error msg is:{}",e.getMessage());
            throw new RuntimeException("500 socket login error :" + e.getMessage());
        } catch (Exception e) {
            logger.error("socket login Exception error,error msg is:{}",e.getMessage());
            throw new RuntimeException("500 socket login error :" + e.getMessage());
        }
        return socket;
    }

    /**
     *  分析登录的结果
     *  结果: ackLoginAlarm;result=fail;resDesc=username-error
     * @param response
     * @return
     */
    private static boolean analysisLoginResult(String response){
        if (response == null || response.length() == 0){
            return false;
        }
        String[] split = response.split(";");
        String res = split[1].split("=")[1].toLowerCase();
        if (res.trim().equals("fail")){
            return false;
        }
        if (res.trim().equals("succ")){
            return true;
        }
        return false;
    }

}
