package com.wk.config;

import com.wk.web.service.AutoWiredService;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"com.wk.web"},includeFilters = {
        @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {AutowiredFilter.class})
},useDefaultFilters = false)
public class AutowiredConfig {

    @Primary  //优先注入此bean
    @Bean("service")
    public AutoWiredService autoWiredService(){

        return new AutoWiredService();
    }
}
