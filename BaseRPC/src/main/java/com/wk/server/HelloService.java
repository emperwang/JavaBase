package com.wk.server;

import com.wk.API.IHello;

public class HelloService implements IHello {
    @Override
    public String sayHello(String info) {
        String result = "hello" + info;
        System.out.println(result);
        return result;
    }
}
