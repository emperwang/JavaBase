package com.wk.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  每次一个socket连接，都使用一个新的线程去进行处理
 */
public class SocketServerThread {
    private  static  final Logger logger = LoggerFactory.getLogger(SocketServerdemo.class);
    private static final String CharSet = "UTF-8";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9001);
        while (true){
            Socket socket = serverSocket.accept();
            new processSocket(socket).start();
        }
    }

    static class processSocket extends Thread{

        private Socket socket;

        public processSocket(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                // 接收客户端发过来的数据
                InputStream inputStream = socket.getInputStream();
                int count = 0;
                while (count == 0){
                    count = inputStream.available();
                }
                byte[] bytes = new byte[count];
                inputStream.read(bytes);
                logger.info("receive msg is {}",new String(bytes));
                // 向客户端发送数据
                OutputStream outputStream = socket.getOutputStream();
                byte[] data = "hello, welcome to wk server!".getBytes();
                outputStream.write(data);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
