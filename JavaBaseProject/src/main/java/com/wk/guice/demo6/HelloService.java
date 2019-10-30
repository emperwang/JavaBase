package com.wk.guice.demo6;

import com.google.inject.ProvidedBy;

@ProvidedBy(value = HelloProvider.class)
public interface HelloService {
   public void printInfo();
}
