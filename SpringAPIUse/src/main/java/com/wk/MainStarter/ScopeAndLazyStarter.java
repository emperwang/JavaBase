package com.wk.MainStarter;

import com.wk.beans.Person;
import com.wk.config.ScopeAndLazyConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScopeAndLazyStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ScopeAndLazyConfig.class);
        /*String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }*/
        System.out.println("容器创建完成");
        Person person = (Person)applicationContext.getBean("person");
        Person person2 = (Person)applicationContext.getBean("person");
        System.out.println(person == person2);

    }
}
