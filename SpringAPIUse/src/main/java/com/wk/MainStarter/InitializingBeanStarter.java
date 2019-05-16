package com.wk.MainStarter;

import com.wk.config.InitializingBeanAndDisposableBeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class InitializingBeanStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(InitializingBeanAndDisposableBeanConfig.class);
        applicationContext.close();
    }
}
