package com.wk.proxy.springAOP.spring3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StarterMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beansOfAOP.xml");
        MathImpl target = (MathImpl) applicationContext.getBean("target");
        //target.add(1,2); //前置通知，返回值通知，后置通知
        //target.sub(3,2); //前置，后置通知，环绕通知
        target.div(1,0);//前置，后置通知，异常通知
    }
}
