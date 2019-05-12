package com.wk.proxy.springAOP.spring1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SurroundAdvice implements MethodInterceptor{
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        //前置横切
        System.out.println("环绕通知开始");
        System.out.println("method :"+methodInvocation.getMethod()+" ,target is:"+methodInvocation.getThis()+",arguments is:"+methodInvocation.getArguments());
        //方法调用
        Object result = methodInvocation.proceed();
        //后置横切
        System.out.println("return value is:"+result);
        System.out.println("环绕通知结束");
        return result;
    }
}
