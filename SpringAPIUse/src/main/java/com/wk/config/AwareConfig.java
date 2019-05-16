package com.wk.config;

import com.wk.beans.AwareBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwareConfig {

    @Bean
    public AwareBean awareBean(){
        return new AwareBean();
    }
}
