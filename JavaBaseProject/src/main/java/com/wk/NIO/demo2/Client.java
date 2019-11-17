package com.wk.NIO.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        //clientDemo1();
       // clientDemo3();
        clientDemo2();
    }

    public static void clientDemo1() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",7777));

        socketChannel.close();
    }

    public static void clientDemo2() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost",7777));
        while (!socketChannel.finishConnect()){
            System.out.println("connecting...");
        }
        ByteBuffer buffer = ByteBuffer.wrap("clientDemo2 write ".getBytes());
        socketChannel.write(buffer);
        buffer.clear();
        Thread.sleep(Long.MAX_VALUE);
        socketChannel.close();
    }

    public static void clientDemo3() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 7777));
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        boolean isRun = true;
        while (isRun) {
            if (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    if (next.isConnectable()) {
                        System.out.println("connected.....");
                        SocketChannel channel = (SocketChannel) next.channel();

                        while (!channel.finishConnect()) {
                            System.out.println("connecting...");
                        }
                        ByteBuffer buffer = ByteBuffer.wrap("client write msg 1111".getBytes());
                        channel.write(buffer);
                        channel.register(next.selector(), SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                    }
                    if (next.isReadable()) {
                        System.out.println("readable.....");
                        ByteBuffer buffer = ByteBuffer.allocate(100);
                        SocketChannel channel = (SocketChannel) next.channel();
                        int len = 0;
                        while ((len = channel.read(buffer))!= 0){
                            buffer.flip();
                            System.out.println("read msg is :"+new String(buffer.array()));
                        }
                        buffer.clear();
                    }

                    if (next.isWritable()) {
                        System.out.println("writeable.....");
                        Thread.sleep(2000);
                        ByteBuffer buffer = ByteBuffer.wrap("client write msg 222".getBytes());
                        SocketChannel channel = (SocketChannel) next.channel();
                        channel.write(buffer);
                    }

                }
            }
        }
        socketChannel.close();

    }

    public static void clientDemo4() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(true);
        socketChannel.connect(new InetSocketAddress("localhost",7777));

        ByteBuffer buffer = ByteBuffer.wrap("client write ".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        ByteBuffer readBuf = ByteBuffer.allocate(50);
        socketChannel.read(readBuf);
        readBuf.flip();
        System.out.println("read msg is :"+new String(readBuf.array()));
        socketChannel.close();
    }
}
