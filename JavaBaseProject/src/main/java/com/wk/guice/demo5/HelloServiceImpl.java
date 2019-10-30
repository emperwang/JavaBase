package com.wk.guice.demo5;

import com.google.inject.Singleton;

@Singleton
public class HelloServiceImpl implements HelloService {
    @Override
    public void printInfo() {
        System.out.println("printInfo method");
    }
}
