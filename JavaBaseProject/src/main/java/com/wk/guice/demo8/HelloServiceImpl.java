package com.wk.guice.demo8;

public class HelloServiceImpl implements HelloService {

    private String msg;

    public HelloServiceImpl(String msg) {
        this.msg = msg;
    }

    @Override
    public void printInfo() {
        System.out.println("printInfo method :"+msg);
    }
}
