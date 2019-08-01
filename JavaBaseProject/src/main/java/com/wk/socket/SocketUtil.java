package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class SocketUtil {
    private  static  final Logger logger = LoggerFactory.getLogger(SocketServerdemo.class);
    private static final String HostAddress = "127.0.0.1";

    private static final Integer HostPort = 9001;

    private static final String CharSet = "UTF-8";

    private static Integer SoTimeOut = 30000;

    private static Integer SendBufSize = 1024;

    private static Integer ReceiveBufSize = 2014;

    private static final String FileToSend = "D:/gc.log";
    private static final String SavePath = "D:/image";

    public static Socket connectServer(String hostAddres, Integer port){
        Socket socket = null;
        try {
            socket = new Socket(hostAddres, port);
            logger.info("create socket connect....");
        } catch (IOException e) {
            logger.info("create connect error,the msg is:{}",e.getMessage());
        }
        return socket;
    }

    /**
     * 向服务端使用TCP发送数据
     *
     * @param data
     * @return
     */
    public static void sendAndReceiveData(String data,Socket socket) throws IOException {
        if (socket == null) {
            connectServer(HostAddress, HostPort);
        }
        // 向服务端发送信息
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
        logger.info("发送完毕！");

        // 接收服务端的返回值
        InputStream inputStream = socket.getInputStream();
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        byte[] recv = new byte[inputStream.available()];
        inputStream.read(recv);

        String str_recv = new String(recv);
        logger.info("客户端：接收到服务端的文字：" + str_recv);

        outputStream.close();
        inputStream.close();
        closeConnect(socket);
    }

    /**
     *  发送消息
     */
    public static void sendMsg(String msg,Socket socket){
        if (msg == null || msg.length() == 0){
            logger.error("msg can not be null");
        }
        if (socket == null){
            connectServer(HostAddress,HostPort);
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write(msg);
            writer.flush();

            writer.close();
            closeConnect(socket);
        } catch (IOException e) {
            logger.error("send msg error,error msg is:{}",e.getMessage());
        }
    }


    /**
     * 发送消息
     */
    public static void sendMsgNotClose(String msg, Socket socket) {
        if (msg == null || msg.length() == 0) {
            logger.error("msg can not be null");
        }
        if (socket == null) {
            socket = connectServer(HostAddress, HostPort);
        }
        PrintWriter writer = null;
        try {
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream));
            writer.println(msg);
            writer.flush();
        } catch (IOException e) {
            logger.error("send msg error,error msg is:{}", e.getMessage());
        } finally {

        }
    }
    /**
     *  关闭连接
     * @param socket
     */
    public static void closeConnect(Socket socket){
        if (socket != null){
            try {
                socket.close();
                logger.info("close socket connection..");
            } catch (IOException e) {
                logger.error("close socket error,erroe msg is:{}",e.getMessage());
            }
        }
    }
    /**
     *  发送文件
     */
    public static void sendFile(String file,Socket socket){
        if (file == null || file.length() == 0){
            logger.error("file must be set");
        }
        if (socket == null){
            socket = connectServer(HostAddress,HostPort);
            logger.debug("socket can not be null");
        }
        File file1 = new File(file);
        String name = file1.getName();
        long length = file1.length();
        try {
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            dataOutputStream.writeUTF(name);
            dataOutputStream.writeLong(length);
            byte[] buf = new byte[SendBufSize];
            int count = -1;
            while ((count = dataInputStream.read(buf)) != -1){
                dataOutputStream.write(buf,0,count);
            }
            dataOutputStream.flush();

            dataInputStream.close();
        } catch (IOException e) {
            logger.error("socket occur error,error msg is:{}",e.getMessage());
        }
    }

    /**
     *  接收文件
     * @param savePath
     * @param
     */
    public static void revFile(String savePath,Socket socket){
        if (savePath == null || savePath.length() == 0){
            logger.error("file must be set");
        }
        if (socket == null){
            socket = connectServer(HostAddress,HostPort);
            logger.debug("socket can not be null");
        }
        try {
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
            String name = dataInputStream.readUTF();
            long length = dataInputStream.readLong();
            if (savePath.endsWith("/")){
                savePath += name;
            }else{
                savePath += "/" + name;
            }
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
            logger.info("savepath is:{},file name is:{},file length is:{}",savePath,name,length);
            byte[] buf = new byte[ReceiveBufSize];
            int count = 0;
            while ((count = dataInputStream.read(buf)) != -1){
                dataOutputStream.write(buf,0,count);
            }
            dataOutputStream.flush();

            dataOutputStream.close();
        } catch (IOException e) {
            logger.error("socket occur error,error msg is:{}",e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = connectServer(HostAddress, HostPort);
        while (true) {
            // 发送文件
            sendMsgNotClose("write", socket);
            sendFile(FileToSend, socket);

            Thread.sleep(5000);
        }
    }
}
