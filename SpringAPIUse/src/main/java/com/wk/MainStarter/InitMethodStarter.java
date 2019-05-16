package com.wk.MainStarter;

import com.wk.config.InitMethodConfig;
import com.wk.util.PrintUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class InitMethodStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(InitMethodConfig.class);
        PrintUtil.printNamesInIOC(applicationContext);
        applicationContext.close();
    }
}
