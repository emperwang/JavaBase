package com.wk.guice.demo2;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class StartApp {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();
    }
}
