package com.wk.demo.namedInject;

//定义一个实现类,作为准备注入的一个类
public class SimplePrint implements IPrinter {
    @Override
    public void print() {
        System.out.println("SimplePrint printing.....");
    }
}
