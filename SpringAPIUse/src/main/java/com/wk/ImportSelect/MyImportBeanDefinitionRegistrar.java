package com.wk.ImportSelect;

import com.wk.beans.RainBow;
import com.wk.beans.Red;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 手动进行bean的注册
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     *
     * @param importingClassMetadata 标有@Import注解的bean的所有注册信息
     * @param registry BeanDefinition类注册方法
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        /**
         * 业务逻辑，如果有容器中已经有了red，blue，那么就注册RainBow类
         */
        boolean red = registry.containsBeanDefinition("com.wk.beans.Red");
        boolean blue = registry.containsBeanDefinition("com.wk.beans.Blue");
        if (red && blue){
            //创建一个BeanDefinition
            RootBeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class);
            //注册bean并指定bean的名字是rainBow
            registry.registerBeanDefinition("rainBow",beanDefinition);
        }
    }
}
