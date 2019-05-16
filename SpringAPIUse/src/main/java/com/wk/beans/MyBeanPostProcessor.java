package com.wk.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    //初始化之前调用
    /**
     * @param bean  ioc容器中注册的bean的实例
     * @param beanName bean的名字
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization............:"+beanName);
        return bean;
    }
    //初始化之后调用
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization............:"+beanName);
        return bean;
    }
}
