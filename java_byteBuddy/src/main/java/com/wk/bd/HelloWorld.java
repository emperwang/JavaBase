package com.wk.bd;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author: Sparks
 * @Date: 2021/7/20 14:48
 * @Description
 */
public class HelloWorld {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<?> loaded = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello world"))
                .make()
                .load(HelloWorld.class.getClass().getClassLoader())
                .getLoaded();
        System.out.println(loaded.newInstance().toString());
    }
}
