package com.wk.beans;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * 不同的xxxAware对应特定的xxxAwareProcessor进行处理
 */
public class AwareBean implements ApplicationContextAware,EmbeddedValueResolverAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String value = resolver.resolveStringValue("Hello ${os.name}");
        System.out.println("resolver value is "+value);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public String toString() {
        return "AwareBean{" +
                "applicationContext=" + applicationContext +
                '}';
    }
}
