package com.wk.MainStarter;

import com.wk.beans.Person;
import com.wk.config.BeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationCointext {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfig.class);
        /*Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);*/
        String[] names = applicationContext.getBeanNamesForType(Person.class);
        for (String name: names) {
            System.out.println(name);
        }
    }
}
