package com.wk.demo.mapInject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.wk.demo.namedInject.IPrinter;

/**
 * 设置好绑定关系
 */
public class SimpleModule extends AbstractModule {
    //使用内置的map映射关系  保存注入的依赖关系
    //simple字符串对应SimplePrinter类
    // complex字符串对应 ComplexPrinter类
    @Override
    protected void configure() {
        MapBinder<String, IPrinter> mapBinder = MapBinder.newMapBinder(binder(), String.class, IPrinter.class);
        mapBinder.addBinding("simple").to(SimplePrinter.class);
        mapBinder.addBinding("complex").to(ComplxPrinter.class);
    }
}
