package com.wk.demo.setInject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wk.demo.namedInject.IPrinter;

import java.util.Set;

public class SetInjectMain {

    @Inject
    private Set<IPrinter> printers;

    /**
     * 调用所有注入的IPrinter实现类的print函数
     */
    public void hello(){
        for (IPrinter printer:printers){
            printer.print();
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleModule());
        SetInjectMain instance = injector.getInstance(SetInjectMain.class);
        instance.hello();
    }
}
