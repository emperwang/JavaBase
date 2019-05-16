package com.wk.config;

import com.wk.beans.Color;
import com.wk.beans.ColorFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeanConfig {

    @Bean
    public FactoryBean<Color> factoryBean(){
        return new ColorFactoryBean();
        }
}
