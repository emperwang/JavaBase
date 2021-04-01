package com.wk.dp.structural.facade;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:46
 * @Description
 */
public interface ApplicationContext {

    String getContextName();
    String getAttribute(String name);
    void setAttribute(String name,String value);
}
