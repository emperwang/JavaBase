package com.wk.guice.demo3;

import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

// 使用注释代替module
@ImplementedBy(value = HelloServiceImpl.class)
   // 不加此注释,每次获取一个实例都是不同的;此注解表示单例
public interface HelloService {

   public void printInfo();
}
