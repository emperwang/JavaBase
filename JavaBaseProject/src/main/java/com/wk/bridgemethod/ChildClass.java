package com.wk.bridgemethod;

/**
 * @author: ekiawna
 * @Date: 2021/2/23 13:59
 * @Description
 */
public class ChildClass extends PPClass<String> {
    /*
        在此实现类中，jvm会多生成一个函数，此韩式就称为 bridgeMethod
        public volatile synchetic  id (java.lang.Object arg0 )
     */

    @Override
   public String id(String x) {
        return x;
    }
}
