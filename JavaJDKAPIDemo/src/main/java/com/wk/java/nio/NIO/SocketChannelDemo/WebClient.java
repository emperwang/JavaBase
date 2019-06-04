package com.wk.java.nio.NIO.SocketChannelDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *  socketChannel demo入门
 */
public class WebClient {
    public static void main(String[] args) throws IOException {
        // 1. 创建一个socket
        SocketChannel socketChannel = SocketChannel.open();
        // 2. 连接服务器
        socketChannel.connect(new InetSocketAddress("127.0.0.1",3333));
        // 3. 创建一个写缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        // 4. 写入数据
        writeBuffer.put("Hello SocketChannelServer from webclient".getBytes());
        // 5. 定位到写的数据
        writeBuffer.flip();
        // 6. 把缓冲区内容写入到channel中
        socketChannel.write(writeBuffer);
        // 7. 定义一个读缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        // 8. 读取内容到缓冲区
        socketChannel.read(readBuffer);
        // 9.存放读取的内容
        StringBuilder stringBuilder = new StringBuilder();
        // 10.定位到读取的数据
        readBuffer.flip();
        while (readBuffer.hasRemaining()){
            stringBuilder.append((char)readBuffer.get());
        }
        System.out.println("从服务端接收的数据:"+stringBuilder);
        socketChannel.close();
    }
}
