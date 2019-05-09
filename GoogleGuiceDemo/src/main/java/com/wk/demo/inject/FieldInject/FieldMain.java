package com.wk.demo.inject.FieldInject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 属性注入的测试
 */
public class FieldMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

            }
        });

        HelloCaller caller = injector.getInstance(HelloCaller.class);
        caller.sayHelloFunc();
    }
}
