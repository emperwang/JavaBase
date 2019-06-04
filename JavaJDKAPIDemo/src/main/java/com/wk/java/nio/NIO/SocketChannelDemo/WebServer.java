package com.wk.java.nio.NIO.SocketChannelDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *  socketChannelServer demo 入门
 */
public class WebServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建服务器channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2. 服务器channel绑定地址
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",3333));
        // 3. 接收连接
        SocketChannel accept = serverSocketChannel.accept();
        // 4. 定义读取缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        // 5. 写入缓冲区内容
        writeBuffer.put("hello socketChannel,this is from webServer".getBytes());
        // 6. 定位写入的数据
        writeBuffer.flip();
        // 7. 写入数据到channel
        accept.write(writeBuffer);
        // 8. 定义读取缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        // 9. 服务客户端内容
        accept.read(readBuffer);

        StringBuilder builder = new StringBuilder();
        //  0. 定位读取的数据位置
        readBuffer.flip();

        while (readBuffer.hasRemaining()){
            builder.append((char)readBuffer.get());
        }

        System.out.println("从客户端读取的数据:"+builder);
        accept.close();
        serverSocketChannel.close();
    }
}
