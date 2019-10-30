package com.wk.guice.demo3;

import com.google.inject.Singleton;

@Singleton
public class HelloServiceImpl implements HelloService {
    @Override
    public void printInfo() {
        System.out.println("printInfo method");
    }
}
