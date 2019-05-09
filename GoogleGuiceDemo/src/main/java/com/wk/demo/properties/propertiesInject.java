package com.wk.demo.properties;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import java.util.Properties;

//properties 属性注入
public class propertiesInject {

    @Inject
    @Named("web")
    private String web;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                Properties properties = new Properties();
                properties.put("web", "www.baidu.com");
                Names.bindProperties(binder, properties);
            }
        });

        propertiesInject instance = injector.getInstance(propertiesInject.class);
        System.out.println(instance.web);
    }
}
