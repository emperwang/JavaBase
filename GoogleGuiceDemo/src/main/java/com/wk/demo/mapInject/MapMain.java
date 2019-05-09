package com.wk.demo.mapInject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wk.demo.namedInject.IPrinter;

import java.util.Map;

public class MapMain {

    @Inject
    private Map<String,IPrinter> printers;

    public void  hello(){
        for (String name : printers.keySet()){
            System.out.println("key is :"+name);
            printers.get(name).print();
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleModule());
        MapMain instance = injector.getInstance(MapMain.class);
        instance.hello();
    }
}
