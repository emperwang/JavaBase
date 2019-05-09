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
        simplePrinter.print();
        complexPrinter.print();
    }

    //获取类 进行测试s
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SimpleModule());
        SimpleMain instance = injector.getInstance(SimpleMain.class);
        instance.hello();
    }
}
