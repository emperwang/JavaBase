package com.wk.NIO.DatagramChannelDemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * DatagramChannel demo 入门
 */
public class UDPChannelDemo {
    public static void main(String[] args) throws IOException {
        // 1. 创建udp-channel
        // 2. 绑定端口
        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.bind(new InetSocketAddress(3333));
        // 3. 创建读取缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(128);
        // 4. 接收数据
        datagramChannel.receive(readBuffer);
        // 5. 创建写缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(128);
        // 6. 写入数据
        writeBuffer.put("datagramChannel from client".getBytes());
        writeBuffer.flip();
        // 7. 发送数据
        datagramChannel.send(writeBuffer,new InetSocketAddress("127.0.0.1",3333));
        StringBuilder builder = new StringBuilder();
        readBuffer.flip();

        while (readBuffer.hasRemaining()){
            builder.append((char)readBuffer.get());
        }

        System.out.println("从服务器读取的数据:"+builder);
        datagramChannel.close();
    }
}
