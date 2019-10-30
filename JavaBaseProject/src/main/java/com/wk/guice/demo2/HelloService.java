package com.wk.guice.demo2;

import com.google.inject.ImplementedBy;

// 使用注释代替module
@ImplementedBy(value = HelloServiceImpl.class)

public interface HelloService {

   public void printInfo();
}
