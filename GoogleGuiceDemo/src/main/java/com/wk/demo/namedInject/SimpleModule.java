package com.wk.demo.namedInject;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

//module类  创建类之间的依赖关系
public class SimpleModule extends AbstractModule{

    /**
     * 这里的named 表示在多实现时,识别哪一个类需要进行注入
     */
    @Override
    protected void configure() {
        bind(IPrinter.class).annotatedWith(Names.named("simple")).to(SimplePrint.class);
        bind(IPrinter.class).annotatedWith(Names.named("complex")).to(ComplexPrinter.class);
    }
}
