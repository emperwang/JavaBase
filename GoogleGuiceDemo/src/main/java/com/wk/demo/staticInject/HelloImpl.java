package com.wk.demo.staticInject;

public class HelloImpl implements IHello {
    @Override
    public void sayHello() {
        System.out.println("HelloImpl Say hello");
    }
}
