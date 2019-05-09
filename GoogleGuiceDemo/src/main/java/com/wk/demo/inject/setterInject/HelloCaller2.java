package com.wk.demo.inject.setterInject;

import com.google.inject.Inject;
import com.wk.demo.inject.FieldInject.Hello;

/**
 * setter 方法注入
 */
public class HelloCaller2 {

    private Hello hello;

    public Hello getHello() {
        return hello;
    }

    /**
     * setter方法注入
     * @param hello
     */
    @Inject
    public void setHello(Hello hello) {
        this.hello = hello;
    }
}
