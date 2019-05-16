package com.wk.MainStarter;

import com.wk.config.ProfileConfig;
import com.wk.util.PrintUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 激活profile两种方法:
 * 1. 使用jvm参数 -Dspring.profiles.active=dev
 * 2. 使用代码
 */
public class ProfileStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().setActiveProfiles("pro");
        applicationContext.register(ProfileConfig.class);
        applicationContext.refresh();
        PrintUtil.printNamesInIOC(applicationContext);
        applicationContext.close();
    }
}
