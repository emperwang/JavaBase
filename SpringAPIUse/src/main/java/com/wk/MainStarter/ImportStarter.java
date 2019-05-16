package com.wk.MainStarter;

import com.wk.config.ImportConfig;
import com.wk.util.PrintUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImportConfig.class);
        PrintUtil.printNamesInIOC(applicationContext);

    }
}
