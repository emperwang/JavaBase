package com.wk.MainStarter;

import com.wk.beans.People;
import com.wk.config.PeopleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PeopleStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PeopleConfig.class);
        People people = (People) applicationContext.getBean("people");
        System.out.println(people);
    }

}
