package com.wk.proxy.springAOP.spring1;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BeforeAdvice implements MethodBeforeAdvice {
    /**
     * @param method  方法信息
     * @param objects 参数
     * @param o         被代理的对象
     * @throws Throwable
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("------前置通知-------------");
    }
}
