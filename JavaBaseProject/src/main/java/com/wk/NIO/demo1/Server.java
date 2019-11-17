package com.wk.NIO.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        serverDemo1();
    }

    public static void serverDemo1() throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        //socketChannel.configureBlocking(false);
        socketChannel.configureBlocking(true);
        socketChannel.bind(new InetSocketAddress("localhost",8888));
        Set<SocketOption<?>> supportedOptions = socketChannel.supportedOptions();
        supportedOptions.forEach((option)->{
            System.out.println(option.name());
        });
        SocketChannel accept = socketChannel.accept();
        System.out.println("is accept block");
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        accept.read(buffer);

        buffer.flip();

        String result = buffer.toString();
        System.out.println(result);
        System.out.println(new String(buffer.array()));
        socketChannel.close();
    }
}
