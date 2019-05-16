package com.wk.config;

import com.wk.beans.Person;
import com.wk.condition.LinuxCondition;
import com.wk.condition.WindowsCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//@Conditional({})   //只有满足条件，这个类中的配置信息才能生效
@Configuration
public class ConditionConfig {

    @Bean("person")
    public Person person(){
        return new Person("zhaoliu",30);
    }
    @Conditional(value = {
            WindowsCondition.class
    })
    @Bean("bill")
    public Person bill(){
        return new Person("bill",60);
    }
    @Conditional(value = {
            LinuxCondition.class
    })
    @Bean("linua")
    public Person linua(){
        return new Person("linua",48);
    }
}
