package com.wk.demo.setInject;

import com.wk.demo.namedInject.IPrinter;

public class SimplePrinter implements IPrinter {
    @Override
    public void print() {
        System.out.println("SimplePrinter");
    }
}
