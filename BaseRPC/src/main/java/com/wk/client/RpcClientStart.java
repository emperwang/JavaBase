package com.wk.client;

import com.wk.API.IHello;

public class RpcClientStart {
    public static void main(String[] args) {
        RpcProxyClient<IHello> rpcClient = new RpcProxyClient<>();
        IHello hello = rpcClient.proxyClient(IHello.class, "localhost", 8000);
        String info = hello.sayHello("info");
        System.out.println(info);

    }
}
