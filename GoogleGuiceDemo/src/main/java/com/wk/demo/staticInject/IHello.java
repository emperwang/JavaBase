package com.wk.demo.staticInject;

import com.google.inject.ImplementedBy;

@ImplementedBy(HelloImpl.class)
public interface  IHello {
    void sayHello();
}
