package com.wk.demo.inject.FieldInject;

import com.google.inject.Inject;

/**
 * 属性注入
 */
public class HelloCaller {
    //通过inject 进行属性注入操作
    @Inject
    private Hello hello;

    //调用注入的hello的方法
    public void sayHelloFunc(){
        hello.sayHello();
    }
}
