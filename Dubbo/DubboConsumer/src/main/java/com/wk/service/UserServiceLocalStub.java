package com.wk.service;

import com.wk.api.bean.User;
import com.wk.api.service.UserService;

/**
 * 本地存根方法
 * 此实例：表示当传入的参数不为null时，再去调用远程的方法
 */
public class UserServiceLocalStub implements UserService {

    private UserService userService;

    /**
     *  dubbo会自动注入远程方法的代理
     * @param userService
     */
    public UserServiceLocalStub(UserService userService){
        System.out.println("local stub construction");
        this.userService = userService;
    }
    @Override
    public User getById(Integer id) {
        System.out.println("locals stub  getByid");
        if (id != null){
            User byId = userService.getById(id);
            return byId;
        }
        return null;
    }
}
