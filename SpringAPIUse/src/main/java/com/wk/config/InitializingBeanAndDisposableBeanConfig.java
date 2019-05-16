package com.wk.config;

import com.wk.beans.Cat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializingBeanAndDisposableBeanConfig {

    @Bean
    public Cat cat(){
        return new Cat();
    }
}
