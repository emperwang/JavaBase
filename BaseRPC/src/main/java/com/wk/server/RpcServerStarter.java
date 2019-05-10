package com.wk.server;

import com.wk.API.IHello;

public class RpcServerStarter {
    public static void main(String[] args) throws Exception {
        RpcProxyServer server = new RpcProxyServer();
        IHello hello = new HelloService();
        server.publishServer(hello,8000);
    }
}
