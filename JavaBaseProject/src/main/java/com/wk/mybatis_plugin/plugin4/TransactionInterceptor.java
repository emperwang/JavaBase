package com.wk.mybatis_plugin.plugin4;

import com.wk.mybatis_plugin.ProxyPlugin1;
import com.wk.mybatis_plugin.plugin3.Invocation;
import com.wk.mybatis_plugin.plugin4.WkInvocationHandler;

/**
 * @author: Sparks
 * @Date: 2021/3/9 20:30
 * @Description
 */
public class TransactionInterceptor implements Interceptor {
    @Override
    public Object interceptor(Invocation invocation) throws Exception {
        System.out.println("---------insert before code -----------");
        Object res = invocation.process();
        System.out.println("---------=insert after code-------------");
        return res;
    }

    @Override
    public Object plugin(Object target) {
        Object wrap = WkInvocationHandler.wrap(target, this);
        return wrap;
    }

    public static void main(String[] args) {
        ProxyPlugin1.HelloServiceImpl service = new ProxyPlugin1.HelloServiceImpl();
        TransactionInterceptor interceptor = new TransactionInterceptor();
        TransactionInterceptor interceptor2 = new TransactionInterceptor();
        ProxyPlugin1.HelloService service1 = (ProxyPlugin1.HelloService) interceptor.plugin(service);
        ProxyPlugin1.HelloService service2 = (ProxyPlugin1.HelloService) interceptor2.plugin(service1);
        service2.sayHello();
    }

    public static void test1(){
        ProxyPlugin1.HelloServiceImpl service = new ProxyPlugin1.HelloServiceImpl();
        TransactionInterceptor interceptor = new TransactionInterceptor();
        ProxyPlugin1.HelloService service1 = (ProxyPlugin1.HelloService) interceptor.plugin(service);
        service1.sayHello();
    }
}
