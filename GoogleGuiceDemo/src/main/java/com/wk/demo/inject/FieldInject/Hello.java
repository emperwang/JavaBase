package com.wk.demo.inject.FieldInject;

import com.google.inject.ImplementedBy;

/**
 * implementdBy:表示接口的实现类
 */
@ImplementedBy(HelloImple.class)
public interface Hello {
    void sayHello();
}
