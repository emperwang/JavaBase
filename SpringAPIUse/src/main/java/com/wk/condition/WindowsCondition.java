package com.wk.condition;


import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        context.getClassLoader(); //获取类加载器
        context.getBeanFactory(); // 获取bean工厂
        Environment environment = context.getEnvironment();//获取环境变量参数
        BeanDefinitionRegistry registry = context.getRegistry();//可以注册器，可以注册一个bean，也可以销毁一个bean
        //判断是否注册了某个bean
        boolean b = registry.containsBeanDefinition("person");
        registry.getBeanDefinitionNames();  //获取已定义的bean的名字
        //registry.registerBeanDefinition();  //注册一个bean
        //registry.removeBeanDefinition("person");  //从容器中移除某个bean
        //context.getResourceLoader(); //加载一个resource
        String property = environment.getProperty("os.name");
        if (property.contains("Window")){  //如果是windows环境，那么注册bill这个bean
            return true;
        }
        return false;
    }
}
