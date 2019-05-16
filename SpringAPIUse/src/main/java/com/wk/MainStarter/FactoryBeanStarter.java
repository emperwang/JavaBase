package com.wk.MainStarter;

import com.wk.config.FactoryBeanConfig;
import com.wk.util.PrintUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FactoryBeanStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(FactoryBeanConfig.class);
        PrintUtil.printNamesInIOC(applicationContext);
        Object factoryBean = applicationContext.getBean("factoryBean");
        System.out.println(factoryBean);
    }
}
