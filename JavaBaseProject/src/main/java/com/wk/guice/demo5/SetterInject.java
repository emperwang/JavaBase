package com.wk.guice.demo5;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;

public class SetterInject {

    private HelloService helloService;

    public HelloService getHelloService() {
        return helloService;
    }
    @Inject
    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        SetterInject instance = injector.getInstance(SetterInject.class);
        instance.getHelloService().printInfo();
    }
}
