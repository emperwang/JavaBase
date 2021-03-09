package com.wk.mybatis_plugin.plugin2;

import com.wk.mybatis_plugin.ProxyPlugin1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Sparks
 * @Date: 2021/3/9 19:53
 * @Description
 */
public class WkInvocationHandler implements InvocationHandler {
    private Object target;
    private List<Interceptor> interceptors = new ArrayList<>();

    public WkInvocationHandler(Object obj,List<Interceptor> interceptors ){
        this.target = obj;
        this.interceptors.addAll(interceptors);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 先处理多个拦截器
        for (Interceptor interceptor : interceptors) {
            interceptor.interceptor();
        }
        Object res = method.invoke(target, args);
        return res;
    }

    public static Object wrap (Object target, List<Interceptor> lists){
        WkInvocationHandler handler = new WkInvocationHandler(target, lists);
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
    }

    public static void main(String[] args) {
        ProxyPlugin1.HelloServiceImpl service = new ProxyPlugin1.HelloServiceImpl();
        LogInterceptor log = new LogInterceptor();
        TransactionInterceptor interceptor = new TransactionInterceptor();
        ProxyPlugin1.HelloService helloService = (ProxyPlugin1.HelloService) WkInvocationHandler.wrap(service, Arrays.asList(log, interceptor));
        helloService.sayHello();
    }
}
