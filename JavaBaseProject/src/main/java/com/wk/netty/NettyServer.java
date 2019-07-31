package com.wk.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private static final Integer Port = 9001;
    public static void main(String[] args) {
        /**
         *  创建两个线程组
         *  一个是用于处理服务器端接收客户端连接的
         *  一个是进行网络通信的（网络读写）
         */
        NioEventLoopGroup pGroup = new NioEventLoopGroup();
        NioEventLoopGroup cGroup = new NioEventLoopGroup();
        // 辅助工具类,用于服务器通道的一系列配置s
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(pGroup,cGroup) // 添加俩个线程组
                .channel(NioServerSocketChannel.class) // 指定NIO模式
                .option(ChannelOption.SO_BACKLOG,1024)  // 设置tcp缓冲区
                .option(ChannelOption.SO_SNDBUF,32*1024) // 设置发送缓冲区大小
                .option(ChannelOption.SO_RCVBUF,32*1024) // 设置接收缓冲区大小
                .option(ChannelOption.SO_KEEPALIVE,true) // 保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 配置具体数据的接收方法的处理
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });
        try {
            ChannelFuture channel = serverBootstrap.bind(Port).sync();
            // 等待关闭
            channel.channel().closeFuture().sync();
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
        } catch (InterruptedException e) {
            logger.error("bind error,msg is:{}",e.getMessage());
        }
    }
}
