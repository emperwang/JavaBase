package com.wk.config;

import com.wk.beans.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DogConfig {

    @Bean
    public Dog dog(){
        return new Dog();
    }
}
