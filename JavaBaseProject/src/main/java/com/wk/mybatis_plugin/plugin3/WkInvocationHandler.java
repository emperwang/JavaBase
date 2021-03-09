package com.wk.mybatis_plugin.plugin3;

import com.wk.mybatis_plugin.ProxyPlugin1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:04
 * @Description
 */
public class WkInvocationHandler implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    public WkInvocationHandler(Object obj, Interceptor interceptor){
        this.target = obj;
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(target, method, args);
        return interceptor.intercept(invocation);
    }

    public static Object wrap(Object obj, Interceptor interceptor){
        WkInvocationHandler handler = new WkInvocationHandler(obj, interceptor);
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                handler);
    }

    public static void main(String[] args) {
        ProxyPlugin1.HelloService service = new ProxyPlugin1.HelloServiceImpl();
        TransactionInterceptor interceptor = new TransactionInterceptor();
        ProxyPlugin1.HelloService service1 = (ProxyPlugin1.HelloService) WkInvocationHandler.wrap(service, interceptor);
        service1.sayHello();
    }
}
