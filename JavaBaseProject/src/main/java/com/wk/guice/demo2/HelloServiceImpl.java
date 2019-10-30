package com.wk.guice.demo2;

import com.google.inject.Singleton;

@Singleton   // 不加此注释,每次获取一个实例都是不同的;此注解表示单例
public class HelloServiceImpl implements HelloService {
    @Override
    public void printInfo() {
        System.out.println("printInfo method");
    }
}
