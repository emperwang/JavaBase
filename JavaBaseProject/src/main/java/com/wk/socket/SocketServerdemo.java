package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程处理socket连接
 */
public class SocketServerdemo {
    private  static  final Logger logger = LoggerFactory.getLogger(SocketServerdemo.class);
    private static final String CharSet = "UTF-8";
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9001);
            //serverSocket.bind(new InetSocketAddress("127.0.0.1",9001));
            while (true){
                Socket socket = serverSocket.accept();

                // 接收客户端的信息
                InputStream in = socket.getInputStream();
                int count = 0;
                while (count == 0) {
                    count = in.available();
                }
                byte[] b = new byte[count];
                in.read(b);
                String str = new String(b);
                System.out.println(str);

                // 向客户端发送确认消息
                OutputStream outer = socket.getOutputStream();
                byte[] b_out = "已经收到，返回消息码200".getBytes();
                outer.write(b_out);
                outer.flush();

                // 关闭socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
