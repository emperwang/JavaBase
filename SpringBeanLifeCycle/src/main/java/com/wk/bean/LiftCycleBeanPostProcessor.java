package com.wk.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *  自定义后处理器
 */
public class LiftCycleBeanPostProcessor implements BeanPostProcessor {
    private Logger log = LoggerFactory.getLogger(LiftCycleBeanPostProcessor.class);
    /**
     *  初始化前处理器
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("【接口 LiftCycleBeanPostProcessor.postProcessBeforeInitialization】");
        return bean;
    }

    /**
     *  初始化后 处理器
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("【接口 LiftCycleBeanPostProcessor.postProcessAfterInitialization】");
        return bean;
    }
}
