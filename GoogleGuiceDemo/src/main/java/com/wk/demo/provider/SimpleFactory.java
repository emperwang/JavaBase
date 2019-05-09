package com.wk.demo.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

/**
 * SimpltOut 类的工厂类
 */
@Slf4j
public class SimpleFactory extends AbstractModule{

    /**
     *  @Provides  表示这个类是Simpleout的提供类
     *  @Singleton 单例
     * @return
     */
    /**
     * 这里有个需要注意的 把@Singleton放在这里,并不能保证最后simpleOut就是单例的
     * @return
     */
    @Provides
    @Singleton
    public SimpleOut getOut(){
        SimpleOut simpleOut = new SimpleOut();
        return simpleOut;
    }

    @Override
    protected void configure() {
    }
}
