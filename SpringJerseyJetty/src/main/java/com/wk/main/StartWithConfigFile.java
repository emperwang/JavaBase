package com.wk.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 此启动是在spring.xml中配置到需要的bean,以及bean之间的依赖关系
 * 之后由spring进行相关的注入操作
 *
 * 最后spring在创建server时,会调用初始化函数  start, 也就是启动函数,最终jetty也就启动了
 *
 */
public class StartWithConfigFile {
    public static void main(String[] args) {
        String configFile="/Jersey-config.xml";

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(configFile);
    }
}
