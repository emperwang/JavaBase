package com.wk.config;

import com.wk.beans.Black;
import com.wk.beans.Blue;
import com.wk.beans.Red;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//-DSpring.profiles.active=dev  进行激活
//@Profile(value = "dev") //表示只有在dev环境下,此配置类才会被注册到容器中
@Configuration
public class ProfileConfig {

    @Profile("test")
    @Bean
    public Red red(){
        return new Red();
    }

    @Profile("test")
    @Bean
    public Blue Blue(){
        return new Blue();
    }

    @Profile("pro")
    @Bean
    public Black black(){
        return new Black();
    }
}
