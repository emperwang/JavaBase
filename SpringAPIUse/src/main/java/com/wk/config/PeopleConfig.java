package com.wk.config;

import com.wk.beans.People;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PeopleConfig {
    /**
     * @Value
     * 1. 直接解析字符串
     * 2. SpEL表达式
     * 3. ${} 取出环境变量的值
     */
    @Value("zhangsan")
    private String name;

    @Value("#{20-2}")
    private Integer age;
    @Value("${os.name}")
    private String address;
    @Bean
    public People people(){
        return new People(name,age,address);
    }
}
