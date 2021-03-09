package com.wk.mybatis_plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: Sparks
 * @Date: 2021/3/9 19:42
 * @Description
 */
public class ProxyPlugin1 {

    public interface HelloService{
        void sayHello();
    }

    public static class HelloServiceImpl implements HelloService{

        @Override
        public void sayHello() {
            System.out.println("say hello......");
        }
    }

    static class WkInvocationHandler implements InvocationHandler{
        // 目标对象
        private Object target;

        public WkInvocationHandler(Object obj){
            this.target = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("---------------insert before code...........");
            Object res = method.invoke(target, args);
            System.out.println("---------------insert after code-------------");
            return res;
        }

        public static Object wrap(Object target) {
            return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),
                    new WkInvocationHandler(target));
        }
    }

    public static void main(String[] args) {
        HelloService service = (HelloService) WkInvocationHandler.wrap(new HelloServiceImpl());
        service.sayHello();
    }
}
