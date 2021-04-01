package com.wk.dp.structural.facade;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 14:46
 * @Description
 */
public class ApplicationContextFacade implements ApplicationContext {
    private ApplicationContextDefault applicationContextDefault;
    public ApplicationContextFacade(ApplicationContextDefault applicationContextDefault){
        this.applicationContextDefault = applicationContextDefault;
    }
    /*
    门面模式: 其中ApplicationContext 是对外的功能接口
             ApplicationContextFacade是真实的功能实现,但是其具体实现是委托给ApplicationContextDefault来做的
             之所以设置门面模式, 即为了隐藏ApplicationContextDefault中的一些实现,只给用户看部分实现
     */
    @Override
    public String getContextName() {
        return applicationContextDefault.getContextName();
    }

    @Override
    public String getAttribute(String name) {
        return applicationContextDefault.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, String value) {
        applicationContextDefault.setAttribute(name, value);
    }
}
