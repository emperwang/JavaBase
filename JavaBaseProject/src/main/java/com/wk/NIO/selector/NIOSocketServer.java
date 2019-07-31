package com.wk.NIO.selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(NIOSocketServer.class);
    private static final String Server_IP = "127.0.0.1";
    private static final Integer Server_Port = 9001;

    private static Integer Buffer_Size = 1024;
    private static String LocalCharset = "UTF-8";

    public void start(){
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(Server_IP, Server_Port);
            serverSocketChannel.socket().bind(address);
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            logger.info("server starting working");
            while (true){
                selector.select();
                logger.info("开始处理请求..");
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    // 连接请求
                    if (key.isAcceptable()){
                        logger.info("处理连接请求.....");
                        handleAccept(key);
                    }
                    // 读取请求
                    if (key.isReadable()){
                        logger.info("处理读取请求.....");
                        handleRead(key);
                    }
                    // 处理完后，删除key
                    keyIterator.remove();
                }
                logger.info("一次请求处理完成....");
            }
        } catch (IOException e) {
            logger.error("error msg is:{}",e.getMessage());
        }
    }

    /**
     *  处理连接请求
     */
    public void handleAccept(SelectionKey selectionKey){
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(Buffer_Size);
            // 获取channel
            SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
            // 非阻塞
            socketChannel.configureBlocking(false);
            // 注册selector
            socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ,byteBuffer);

            logger.info("建立请求......");
        } catch (IOException e) {
            logger.error("the error msg is:{}",e.getMessage());
        }
    }

    /**
     *  处理读取请求
     */
    public void handleRead(SelectionKey selectionKey){
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        String receiveStr = "";
        try {
            if (channel.read(byteBuffer) == -1){
                // 没有读取到内容
                channel.shutdownInput();
                channel.shutdownOutput();
                channel.close();
                logger.info("nothing to read,stop connected");
            }else {
                byteBuffer.flip();
                // 读取数据
                receiveStr = Charset.forName(LocalCharset).newDecoder().decode(byteBuffer).toString();
                logger.info("receive msg is:{}",receiveStr);
                byteBuffer.clear();
                // 返回数据给客户端
                byteBuffer = byteBuffer.put(("receive string:" + receiveStr).getBytes());
                byteBuffer.flip();
                // 写入数据
                channel.write(byteBuffer);
                channel.register(selectionKey.selector(),SelectionKey.OP_READ,ByteBuffer.allocate(Buffer_Size));
            }
        } catch (IOException e) {
            logger.error("read msg error,msg is:{}",e.getMessage());
        }
    }

    public static void main(String[] args) {
        NIOSocketServer socketServer = new NIOSocketServer();
        socketServer.start();
    }
}
