package com.wk.guice.demo8;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class ProvidesModule implements Module {
    @Override
    public void configure(Binder binder) {
        // 可以作为默认的注入操作
        binder.bind(HelloService.class).to(Key.get(HelloService.class, Names.named("serviceOne")));
    }

    @Provides
    @Named(value = "serviceOne")
    public HelloService getServiceOne(){

        return new HelloServiceImpl("service One");
    }

    @Provides
    @Named(value = "serviceTwo")
    public HelloService getServiceTwo(){
        return new HelloServiceImpl("service Two");
    }
}
