package com.wk.demo.provider;

import com.google.inject.*;

public class ProviderMain {

    @Inject
    private SimpleOut out;

    @Inject
    private SimpleOut out1;

    public void testSingleton(){
        System.out.println(out);
        System.out.println(out1);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

            }
        });

        ProviderMain instance = injector.getInstance(ProviderMain.class);
        instance.testSingleton();
    }
}
