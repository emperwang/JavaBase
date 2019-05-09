package com.wk.demo.APIdemo1;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

/**
 * 具体的绑定操作
 */
public class AddModule implements Module {
    @Override
    public void configure(Binder binder) {
        //把AddFunc绑定到SimpleAdd , 并指定了单例模式
        binder.bind(AddFunc.class).to(SimpleAdd.class).in(Scopes.SINGLETON);
    }
}
