package com.wk.MainStarter;

import com.wk.beans.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLContext {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }
}
