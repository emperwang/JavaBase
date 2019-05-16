package com.wk.config;

import com.wk.beans.Person;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class ScopeAndLazyConfig {
    /**
     * SCOPE_PROTOTYPE  多实例
     * SCOPE_SINGLETON 单实例
     * SCOPE_REQUEST 请求域
     * SCOPE_SESSION session域
     */
    //@Scope(value = "prototype")
    @Lazy //在初始化的时候不注册bean，只有在第一次使用的时候才会取注册bean
    @Bean("person")
    public Person person(){
        System.out.println("注册bean");
        return new Person("wwww",20);
    }
}
