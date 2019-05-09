package com.wk.demo.APIdemo1;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

import java.util.Map;
import java.util.Set;

/**
 * 一个项目中可以有多个module,根据不同的module生成不同的注入关系
 */
public class AddClient {
    public static void main(String[] args) {
        //通过Guice去和各种module打交道
        //根据module指定的绑定关系, 生成注入
        Injector injector = Guice.createInjector(new AddModule());
        //得到所有的绑定关系
        Map<Key<?>, Binding<?>> bindings = injector.getAllBindings();
        //遍历所有的绑定关系
        Set<Map.Entry<Key<?>, Binding<?>>> entries = bindings.entrySet();
        for (Map.Entry entry:entries){
            System.out.println(entry.getKey().toString());
            System.out.println(entry.getValue().toString());
        }
        //得到SinpleAdd的实例
        AddFunc add = injector.getInstance(AddFunc.class);
        System.out.println(add.add(10,5));
    }
}
