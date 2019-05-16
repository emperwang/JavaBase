package com.wk.MainStarter;

import com.wk.config.DogConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PostConstructStarter {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DogConfig.class);

        applicationContext.close();
    }
}
