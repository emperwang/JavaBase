package com.wk.demo.setInject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.wk.demo.namedInject.IPrinter;

//这里使用guice扩展功能set,来保存注入的依赖关系
public class SimpleModule extends AbstractModule{

    /**
     * 这里使用guice的扩展方法 把所有的绑定关系,保存在一个set集合中
     */
    @Override
    protected void configure() {
        Multibinder<IPrinter> setBinder = Multibinder.newSetBinder(binder(), IPrinter.class);
        setBinder.addBinding().to(SimplePrinter.class);
        setBinder.addBinding().to(ComplexPrinter.class);

    }
}
