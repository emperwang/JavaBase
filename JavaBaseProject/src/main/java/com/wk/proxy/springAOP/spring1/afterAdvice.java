package com.wk.proxy.springAOP.spring1;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class afterAdvice implements AfterReturningAdvice {
    /**
     *
     * @param returnValye  返回值
     * @param method    被调用方法
     * @param args      方法参数
     * @param target    被代理对象
     * @throws Throwable
     */
    @Override
    public void afterReturning(Object returnValye, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("-------------后置通知------------------");
    }
}
