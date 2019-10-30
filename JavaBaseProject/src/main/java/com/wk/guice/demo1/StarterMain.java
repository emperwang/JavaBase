package com.wk.guice.demo1;

import com.google.inject.*;

public class StarterMain {
    public static void main(String[] args) {
        // GetInstance1();
        GetInstance2();
        GetInstance3();
        GetInstance4();
    }

    private static void GetInstance4() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {  // 此处的in指定了是否是单例
                binder.bind(HelloService.class).to(HelloServiceImpl.class).in(Scopes.SINGLETON);
            }
        });
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();
    }
    private static void GetInstance3() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(HelloService.class).toProvider(new Provider<HelloService>() {
                    @Override
                    public HelloService get() {
                        return  new HelloServiceImpl();
                    }
                });
            }
        });
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();
    }

    private static void GetInstance2() {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(HelloService.class).to(HelloServiceImpl.class);
            }
        });
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();
    }

    private static void GetInstance1() {
        Injector injector = Guice.createInjector(new GuiceServiceModule());
        HelloService instance = injector.getInstance(HelloService.class);
        instance.printInfo();
    }
}
