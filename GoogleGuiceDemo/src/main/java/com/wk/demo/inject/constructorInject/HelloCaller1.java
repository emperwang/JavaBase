package com.wk.demo.inject.constructorInject;

import com.google.inject.Inject;
import com.wk.demo.inject.FieldInject.Hello;

/**
 * 测试构造方法进行注入
 */
public class HelloCaller1 {

    private Hello hello;
    //构造方法注入
    @Inject
    public HelloCaller1(Hello hello){
        this.hello = hello;
    }

    public void testHelloFunc(){
        hello.sayHello();
    }
}
