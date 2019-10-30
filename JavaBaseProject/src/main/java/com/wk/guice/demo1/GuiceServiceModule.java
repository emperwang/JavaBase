package com.wk.guice.demo1;

import com.google.inject.Binder;
import com.google.inject.Module;

public class GuiceServiceModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(HelloService.class).to(HelloServiceImpl.class);
    }
}
