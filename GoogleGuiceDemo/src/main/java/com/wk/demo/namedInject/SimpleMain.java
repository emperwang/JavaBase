package com.wk.demo.namedInject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

public class SimpleMain {

    //根据module中定义的依赖关系,simple表示 这里注入的类是 simplePrint
    @Inject
    @Named("simple")
    private IPrinter simplePrinter;

    @Inject
    @Named("complex")
    private IPrinter complexPrinter;

    public void hello(){
        System.out.println("simple: " + simplePrinter.hashCode());
        System.out.println("complex: " + complexPrinter.hashCode());
        simplePrinter.print();
        complexPrinter.print();
    }

    //获取类 进行测试s
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleModule());
        SimpleMain instance = injector.getInstance(SimpleMain.class);

        SimpleMain instance1 = injector.getInstance(SimpleMain.class);
        System.out.println(instance.hashCode());
        System.out.println(instance1.hashCode());
        instance.hello();
        instance1.hello();
    }
}
