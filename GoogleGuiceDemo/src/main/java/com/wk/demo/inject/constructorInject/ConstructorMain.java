package com.wk.demo.inject.constructorInject;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * 构造方法注入 测试
 */
public class ConstructorMain {
    public static void main(String[] args) {
        //这里没有定义module  所以需要定义一个空的module,去和其他object交互
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

            }
        });
        //测试构造函数注入
        HelloCaller1 caller1 = injector.getInstance(HelloCaller1.class);
        caller1.testHelloFunc();
    }
}
