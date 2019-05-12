package com.wk.proxy.springAOP.spring2;

import com.wk.proxy.API.MathCalc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StarterMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        MathCalc proxy = (MathCalc) applicationContext.getBean("proxy");
        proxy.add(1,2);
    }
}
