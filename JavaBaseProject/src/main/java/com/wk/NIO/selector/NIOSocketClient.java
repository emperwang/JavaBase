package com.wk.NIO.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(NIOSocketClient.class);
    private static final String Server_IP = "127.0.0.1";
    private static final Integer Server_Port = 9001;

    private static Integer Buffer_Size = 1024;
    private static String LocalCharset = "UTF-8";

    public void start() {
        SocketChannel socketChannel = null;
        try {
            // 创建一个channel
            socketChannel = SocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(Server_IP, Server_Port);
            // 连接
            socketChannel.connect(address);
            // 缓存
            ByteBuffer buffer = ByteBuffer.allocate(Buffer_Size);
            int count = 0;
            while (count < 10) {
                buffer.clear();
                buffer.put(("currentTime is:" + System.currentTimeMillis()).getBytes());
                buffer.flip();
                // 向服务器写入数据
                socketChannel.write(buffer);
                buffer.clear();
                // 读取服务端数据
                int readLength = socketChannel.read(buffer);
                // 读取模式
                buffer.flip();
                byte[] bytes = new byte[readLength];
                buffer.get(bytes);
                logger.info("client receive msg is:{}", new String(bytes));
                buffer.clear();
                count++;
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            logger.error("error msg:{}", e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    logger.error("close channel error,msg is:{}", e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        NIOSocketClient client = new NIOSocketClient();
        client.start();
    }
}
