package com.wk.config;

import com.wk.beans.Dog;
import com.wk.beans.MyBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DogConfig {

    @Bean
    public Dog dog(){
        return new Dog();
    }
    @Bean
    public BeanPostProcessor beanPostProcessor(){
        return new MyBeanPostProcessor();
    }
}
