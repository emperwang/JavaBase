package com.wk.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;

public class LifeCycle implements EmbeddedValueResolverAware,ResourceLoaderAware,
        ApplicationEventPublisherAware,MessageSourceAware,ApplicationContextAware{
    private Logger log = LoggerFactory.getLogger(LifeCycle.class);
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        log.info("【接口 EmbeddedValueResolverAware.setEmbeddedValueResolver】");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        log.info("【接口 ResourceLoaderAware.setResourceLoader】");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        log.info("【接口 ApplicationEventPublisherAware.setApplicationEventPublisher】");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        log.info("【接口 MessageSourceAware.setMessageSource】");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("【接口 ApplicationContextAware.setApplicationContext】");
    }
}
