package com.wk.service;

import com.wk.api.bean.User;
import com.wk.api.service.UserService;

public class UserServiceInvoke {
    UserService userService;

    public User getUser(){
        User byId = userService.getById(1);
        return byId;
    }
}
