package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

    static class ReceiveThread extends Thread{
        private Socket socket;
        public ReceiveThread(Socket socket){
            logger.info("有socket连接......");
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                ByteArrayOutputStream outBuf = new ByteArrayOutputStream();

                byte[] bytes = new byte[inputStream.available()];
                int count = -1;
                int available = -1;
/*                available = inputStream.available();
                while (available > 0){
                    count = inputStream.read(bytes);
                    outBuf.write(bytes,0,count);
                    available = inputStream.available();
                }*/
                inputStream.read(bytes);
                logger.info("receive msg is {}",new String(bytes));
                //logger.info("receive msg is {}",outBuf.toString());

                //outputStream.write("welcome to server!".getBytes(CharSet));
                outputStream.write(bytes);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
