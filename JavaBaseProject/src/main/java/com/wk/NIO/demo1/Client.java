package com.wk.NIO.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        clientDemo1();
    }

    public static void clientDemo1() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",8888));
        ByteBuffer buffer = ByteBuffer.wrap("this is client speaking".getBytes());
        System.out.println("is connect : "+socketChannel.isConnected());
        System.out.println("connect pending :"+socketChannel.isConnectionPending());
        System.out.println("connect complete: "+socketChannel.finishConnect());

        socketChannel.write(buffer);

        socketChannel.close();
    }
}
