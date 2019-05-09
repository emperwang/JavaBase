package com.wk.demo.mapInject;

import com.wk.demo.namedInject.IPrinter;

public class SimplePrinter implements IPrinter {
    @Override
    public void print() {
        System.out.println("simplePrinter outing");
    }
}
