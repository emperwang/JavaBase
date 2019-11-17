package com.wk.NIO.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private volatile static boolean run = true;
    public static void main(String[] args) throws IOException, InterruptedException {
        serverDemo();
    }

    public static void serverDemo() throws IOException, InterruptedException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress("localhost",7777));
        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (run) {
            if (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println("keys :" + selectionKeys.size());
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey next = keyIterator.next();
                    keyIterator.remove();
                    if (next.isAcceptable()) {
                        System.out.println("accept");
                        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println("register read and write");
                        //socketChannel.register(selector, SelectionKey.OP_READ );
                        socketChannel.register(selector,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                    }
                    if (next.isReadable()) {
                        System.out.println("readable");
                        SocketChannel socketChannel = (SocketChannel) next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                        //ByteBuffer byteBuffer = (ByteBuffer) next.attachment();
                        int len = 0;
                        while ((len = socketChannel.read(byteBuffer)) != 0){
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array()));
                        }
                        byteBuffer.clear();
                       // socketChannel.register(selector, SelectionKey.OP_READ );

                        Thread.sleep(2000);
                    }
                    if (next.isWritable()) {
                        System.out.println("write able");
                        SocketChannel socketChannel = (SocketChannel) next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.wrap("this is server response 11 ".getBytes());
                        socketChannel.write(byteBuffer);

                        Thread.sleep(2000);
                    }

                }
            }
        }
        serverSocketChannel.close();
        selector.close();
    }
}
