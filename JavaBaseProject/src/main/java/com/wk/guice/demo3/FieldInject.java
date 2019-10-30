package com.wk.guice.demo3;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;

// field inject
public class FieldInject {
    @Inject
    private HelloService helloService;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        FieldInject instance = injector.getInstance(FieldInject.class);
        instance.getHelloService().printInfo();
    }

    public HelloService getHelloService() {
        return helloService;
    }
}
