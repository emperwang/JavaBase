package com.wk.proxy.CglibProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGlib继承被代理的类，重写方法，织入通知，动态生成字节码并运行，因为时继承所以final类时没有方法动态代理的
 */
public class CglibProxy implements MethodInterceptor {
    //被代理对象
    private Object target;
    //动态生成一个新的类, 使用父类的无参构造方法创建一个指定了特定回调的代理实例
    public Object getProxyObject(Object object){
        this.target = object;
        //增强器，动态代码生成器
        Enhancer enhancer = new Enhancer();
        //回调方法
        enhancer.setCallback(this);
        //设置生成类的父类类型
        enhancer.setSuperclass(target.getClass());
        //动态生成字节码并返回代理对象
        return enhancer.create();
    }
    /**
     *  拦截方法
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        long start = System.currentTimeMillis();
        lazy(2000);
        //调用方法
        Object result = methodProxy.invoke(target, args);
        long end = System.currentTimeMillis();
        System.out.println("用时："+(end-start));
        return result;
    }

    private void lazy(int time) {
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
