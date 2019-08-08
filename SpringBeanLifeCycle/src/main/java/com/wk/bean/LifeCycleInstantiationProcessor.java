package com.wk.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

/**
 *  自定义bean实例化后处理器适配器
 *      注意：这里是实例化
 */
public class LifeCycleInstantiationProcessor extends
        InstantiationAwareBeanPostProcessorAdapter{
    private Logger log = LoggerFactory.getLogger(LifeCycleInstantiationProcessor.class);
    /**
     *  实例化前调用
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        log.info("【接口 LifeCycleInstantiationProcessor.postProcessBeforeInstantiation】");
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    /**
     *  实例化后调用
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        log.info("【接口 LifeCycleInstantiationProcessor.postProcessAfterInstantiation】");
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    /**
     *  设置field值时调用
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        log.info("【接口 LifeCycleInstantiationProcessor.postProcessPropertyValues】");
        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
    }

    /***
     *  初始化前调用
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
/*    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("【接口 LifeCycleInstantiationProcessor.postProcessBeforeInitialization】");
        return super.postProcessBeforeInitialization(bean, beanName);
    }

    *//**
     *  初始化后调用
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     *//*
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("【接口 LifeCycleInstantiationProcessor.postProcessAfterInitialization】");
        return super.postProcessAfterInitialization(bean, beanName);
    }*/
}
