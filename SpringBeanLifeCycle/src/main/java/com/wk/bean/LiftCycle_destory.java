package com.wk.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

public class LiftCycle_destory implements DestructionAwareBeanPostProcessor {
    private Logger log = LoggerFactory.getLogger(LiftCycle_destory.class);
    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        log.info("【接口 LiftCycle_destory.postProcessBeforeDestruction】");
    }
}
