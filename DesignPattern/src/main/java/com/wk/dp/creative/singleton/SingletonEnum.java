package com.wk.dp.creative.singleton;

/**
 * @author: ekiawna
 * @Date: 2021/3/31 17:35
 * @Description
 */
public enum SingletonEnum {
    SINGLETON;

    private User user;

    private SingletonEnum(){
        user = new User();
    }

    public User getUser(){
        return user;
    }
}
