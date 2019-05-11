package com.wk.proxy.JDKProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy implements InvocationHandler{

    private Object target;

    /**
     *
     * @param object 要被代理的对象
     * @return  被代理的对象
     */
    public Object getProxyObject(Object object){
        this.target = object;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),this);
    }
    /**
     * 在调用真实的函数时，会从这个开始调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.currentTimeMillis();
        Object object = method.invoke(target, args);
        lazy(2000);
        long end = System.currentTimeMillis();
        System.out.println("总共用时"+(end-start));
        return object;
    }

    //模拟延时
    private void lazy(int a){
        try{
            Thread.sleep(a);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
