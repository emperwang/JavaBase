package com.wk.MainStarter;

import com.wk.beans.AwareBean;
import com.wk.config.AwareConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AwareStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AwareConfig.class);
        AwareBean awareBean = (AwareBean) applicationContext.getBean("awareBean");
        ApplicationContext applicationContext1 = awareBean.getApplicationContext();
        System.out.println(applicationContext == applicationContext1);
        applicationContext.close();

    }
}
