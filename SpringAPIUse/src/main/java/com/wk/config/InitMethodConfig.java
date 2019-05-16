package com.wk.config;

import com.wk.beans.Black;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitMethodConfig {

    @Bean(initMethod = "init" ,destroyMethod = "destory")
    public Black black(){

        return new Black();
    }
}
