package com.wk.demo.inject.FieldInject;

import com.google.inject.Singleton;

/**
 * 具体的实现类  并且是单例模式
 */
@Singleton
public class HelloImple implements Hello {
    @Override
    public void sayHello() {
        System.out.println("Hello World");
    }
}
