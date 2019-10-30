package com.wk.guice.demo6;

import com.google.inject.Provider;

public class HelloProvider implements Provider<HelloService> {
    @Override
    public HelloService get() {
        return new HelloServiceImpl();
    }
}
