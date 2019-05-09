package com.wk.demo.optionalInject;

import com.google.inject.*;
import com.wk.demo.threadDemo.Service;
import com.wk.demo.threadDemo.WwwService;

//选项注入
public class OptionalDemo {

    @Inject(optional = true)
    Service service = new WwwService();
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {

            }
        });

        OptionalDemo instance = injector.getInstance(OptionalDemo.class);
        //获取注入的service,如果没有注入就是用手动建立的WwwService
        instance.service.execute();
    }

}
