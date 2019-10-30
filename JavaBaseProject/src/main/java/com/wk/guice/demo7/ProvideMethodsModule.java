package com.wk.guice.demo7;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ProvideMethodsModule extends AbstractModule{

    @Override
    protected void configure() {
    }

    @Provides
    public HelloService getHelloService(){

        return new HelloServiceImpl();
    }

}
