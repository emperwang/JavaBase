package com.wk.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 *  bean生命周期的一个实例
 *  1. BeanNameAware
 *  2.BeanClassLoaderAware
 *  3.BeanFactoryAware
 *  4.EnviromentAware
 *  5.EmbeddedValueResolverAware
 *  6.ResourceLoaderAware
 *  7.ApplicationEventPublisherAware
 *  8.MessageSourceAware
 *  9.ApplicationContextAware
 *  10.ServletContextAware
 *  11.BeanPostProcessor-postProcessBeforeInitialization
 *  12.InitializationBean-afterPropertiesSet
 *  13.自定义执行初始化方法
 *  14.BeanPostProcessor-postProcessAfterInitialization
 *
 *  销毁顺序
 *  1.DestructionAwareBeanPostProcessor- postProcessBeforeDestory
 *  2.DisposableBean destory bean 销毁回调方法
 *  3.a custom destory-method definition  用户自定义销毁方法
 */
public class Person implements BeanNameAware,BeanClassLoaderAware,BeanFactoryAware,EnvironmentAware,
        InitializingBean,DisposableBean{
    private Logger log = LoggerFactory.getLogger(Person.class);
    private String name;
    private Integer age;
    public Person(){
        log.info("[构造器] 调用person的构造器实例化");
    }

    public void setName(String name) {
        log.info("[属性注入] Name");
        this.name = name;
    }

    public void setAge(Integer age) {
        log.info("[属性注入] age");
        this.age = age;
    }

    @Override
    public void setBeanName(String name) {
        log.info("[接口 BeanNameAware.setBeanName()]");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        log.info("【接口 BeanClassLoaderAware.setBeanClassLoader()】");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.info("【接口 BeanFactoryAware.setBeanFactory】");
    }

    @Override
    public void setEnvironment(Environment environment) {
        log.info("【接口 EnvironmentAware.setEnvironment】");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("【接口 InitializingBean.afterPropertiesSet】");
    }
    @Override
    public void destroy() throws Exception {
        log.info("【接口 DisposableBean.destroy】");
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}
