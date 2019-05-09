package com.wk.demo.instance;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

// 常量注入
public class instanceInject {
    @Inject
    @Named("v")
    private int v;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            /**
             * 常量注入,除了注入int,还可以注入byte short int long float string boolean double string
             *
             */
            @Override
            public void configure(Binder binder) {
                binder.bindConstant().annotatedWith(Names.named("v")).to(12);
            }
        });
        instanceInject instance = injector.getInstance(instanceInject.class);
        int v = instance.v;
        System.out.println(v);
    }
}
