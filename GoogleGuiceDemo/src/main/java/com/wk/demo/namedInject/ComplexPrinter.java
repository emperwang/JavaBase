package com.wk.demo.namedInject;

//实现类2  准备注入的另一个类
public class ComplexPrinter implements IPrinter {
    @Override
    public void print() {
        System.out.println("ComplexPrinter printing....");
    }
}
