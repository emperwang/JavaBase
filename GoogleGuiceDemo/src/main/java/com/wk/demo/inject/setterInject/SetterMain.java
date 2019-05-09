package com.wk.demo.inject.setterInject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class SetterMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

            }
        });
        //获取setter方法注入的 caller2,并调用其方法进行测试
        HelloCaller2 caller2 = injector.getInstance(HelloCaller2.class);
        caller2.getHello().sayHello();
    }
}
