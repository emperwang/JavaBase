package com.wk.MainStarter;

import com.wk.beans.Person;
import com.wk.config.ConditionConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConditionStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConditionConfig.class);
        String[] names = applicationContext.getBeanNamesForType(Person.class);//获得容器中注册的这个类型的bean名字
        for (String name : names) {
            System.out.println(name);
        }
    }
}
