package com.wk.MainStarter;

import com.wk.config.ConfigScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ComponentScanStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigScan.class);

        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name:names) {
            System.out.println(name);
        }
    }
}
