package com.wk.guice.demo8;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class StartApp {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ProvidesModule());
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();

        HelloService serviceTwo = injector.getInstance(Key.get(HelloService.class, Names.named("serviceTwo")));
        serviceTwo.printInfo();
    }
}
