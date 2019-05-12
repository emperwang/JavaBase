package com.wk.proxy.springAOP.spring1;

import com.wk.proxy.API.MathCalc;
import com.wk.proxy.API.MathImpl;
import org.springframework.aop.framework.ProxyFactory;

public class StartMain {
    public static void main(String[] args) {
        //创建spring代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        //设置被代理对象
        proxyFactory.setTarget(new MathImpl());
        //添加通知
        proxyFactory.addAdvice(new BeforeAdvice());
        proxyFactory.addAdvice(new afterAdvice());
        proxyFactory.addAdvice(new SurroundAdvice());
        //从代理工厂中获得代理对象
        MathCalc mathCalc = (MathCalc) proxyFactory.getProxy();
        mathCalc.add(100,200);
    }
}
