package com.wk.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 1. 构建一个serverSocket服务监听来自客户端的请求
 * 2. 接收请求的数据(方法名和参数)
 * 3. 根据请求的数据(方法名和参数),使用反射调用响应的服务
 * 4. 输出服务的响应数据
 */
public class RpcProxyServer {
    public void publishServer(final Object service, Integer port) throws Exception {
        if (service == null) {
            throw new IllegalArgumentException("service instance == null");
        }
        if (port <= 0 || port >= 65535) {
            throw new IllegalArgumentException("Invalid port  " + port);
        }
        System.out.println("Export service " + service.getClass().getName() + " on port " + port);
        ServerSocket ss = new ServerSocket(port);
        while (true){
            Socket rpcSocket = ss.accept();
            new Thread(new RPCProcess(rpcSocket,service)).start();
        }
    }

}
