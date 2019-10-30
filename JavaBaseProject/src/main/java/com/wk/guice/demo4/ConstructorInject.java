package com.wk.guice.demo4;

import com.google.inject.Guice;

import javax.inject.Inject;

// constructorInject
public class ConstructorInject {

    private HelloService helloService;

    @Inject
    public ConstructorInject(HelloService service){
        this.helloService = service;
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public static void main(String[] args) {
        ConstructorInject instance = Guice.createInjector().getInstance(ConstructorInject.class);
        instance.getHelloService().printInfo();
    }
}
