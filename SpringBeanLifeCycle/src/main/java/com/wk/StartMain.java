package com.wk;

import com.wk.bean.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartMain {
    public static void main(String[] args) {
        String configPath = "spring-context.xml";
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(configPath);
        Person person = applicationContext.getBean("person", Person.class);

        applicationContext.registerShutdownHook();
    }
}
