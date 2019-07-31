package com.wk.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Integer Port = 9001;
    private static final String Server_IP = "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap clientConfig = new Bootstrap();
        clientConfig.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 配置具体数据接收方法的配置
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        try {
            ChannelFuture clientChannel = clientConfig.connect(Server_IP, Port).sync();
            // 发送消息
            clientChannel.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
            Thread.sleep(2000);
            clientChannel.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
            Thread.sleep(2000);
            clientChannel.channel().writeAndFlush(Unpooled.copiedBuffer("999".getBytes()));
            Thread.sleep(2000);

            clientChannel.channel().closeFuture().sync();
            group.shutdownGracefully();
        } catch (InterruptedException e) {
            logger.error("client connect error,msg is:{}",e.getMessage());
        }
    }
}
