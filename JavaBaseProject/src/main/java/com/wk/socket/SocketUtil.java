package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketUtil {
    private  static  final Logger logger = LoggerFactory.getLogger(SocketServerdemo.class);
    private static final String HostAddress = "127.0.0.1";

    private static final Integer HostPort = 9001;

    private static final String CharSet = "UTF-8";

    private static Socket socket = null;

    private static Integer SoTimeOut = 30000;

    private static Integer SendBufSize = 1024;

    private static Integer ReceiveBufSize = 2014;

    public static void connetServer(String hostAddres, Integer port){
        try {
            socket = new Socket(hostAddres, port);
            logger.info("create socket connect....");
        } catch (IOException e) {
            logger.info("create connect error,the msg is:{}",e.getMessage());
        }
    }

    /**
     * 向服务端使用TCP发送数据
     *
     * @param data
     * @return
     */
    public static void sendDataTCP(String data) throws IOException {
        if (socket == null) {
            connetServer(HostAddress, HostPort);
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

    public static void main(String[] args) throws IOException {
        sendDataTCP("hello, this is www client.");
    }
}
