package com.wk.config;

import com.wk.beans.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    //@Bean(name = "person01")  //这里使用注册对bean的名字进行了定义
    @Bean//这里注册bean，默认的bean的名字时函数名字，person  可以修改为person01进行测试
    public Person person01(){
        return new Person("kkkk",30);
    }
}
