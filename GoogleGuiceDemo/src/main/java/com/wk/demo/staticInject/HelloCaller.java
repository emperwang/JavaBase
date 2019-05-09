package com.wk.demo.staticInject;

import com.google.inject.Inject;

public class HelloCaller {
    //注入一个静态属性
    @Inject
    private static IHello hello;

    public static void sayHello(){
        hello.sayHello();
    }
}
