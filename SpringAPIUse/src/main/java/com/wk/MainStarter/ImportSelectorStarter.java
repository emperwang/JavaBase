package com.wk.MainStarter;

import com.wk.config.ImportConfig;
import com.wk.config.ImportSelectConfig;
import com.wk.util.PrintUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ImportSelectorStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImportSelectConfig.class);
        PrintUtil.printNamesInIOC(applicationContext);

    }
}
