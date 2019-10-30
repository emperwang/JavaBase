package com.wk.guice.demo7;

import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.inject.Inject;

public class StartMain {
    @Inject
    private HelloService helloService;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ProvideMethodsModule());
        StartMain instance = injector.getInstance(StartMain.class);

        instance.getHelloService().printInfo();
    }

    public HelloService getHelloService() {
        return helloService;
    }
}
