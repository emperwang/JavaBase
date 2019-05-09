package com.wk.demo.staticInject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class TestMain {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
        //requestStaticInjection 静态属性的绑定
            @Override
            public void configure(Binder binder) {
                binder.requestStaticInjection(HelloCaller.class);
            }
        });
        HelloCaller instance = injector.getInstance(HelloCaller.class);
        instance.sayHello();
    }
}
