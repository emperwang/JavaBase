package com.wk.MainStarter;

import com.wk.config.AutowiredConfig;
import com.wk.util.PrintUtil;
import com.wk.web.controller.AutoWiredController;
import com.wk.web.service.AutoWiredService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AutowiredStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutowiredConfig.class);
        PrintUtil.printNamesInIOC(applicationContext);
        AutoWiredController controller = (AutoWiredController) applicationContext.getBean("autoWiredController");
        System.out.println(controller);
        AutoWiredService service = (AutoWiredService) applicationContext.getBean("service");
        System.out.println(service);
        applicationContext.close();
    }
}
