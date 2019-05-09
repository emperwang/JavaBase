package com.wk.demo.mapInject;

import com.wk.demo.namedInject.IPrinter;

public class ComplxPrinter implements IPrinter {
    @Override
    public void print() {
        System.out.println("ComplexPrinter outing");
    }
}
